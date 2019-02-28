import cv2
import numpy as np
import math
import sys
import keyboard
from networktables import NetworkTables


"""
This method filters the image to find tape contours
    - Input: image
    - Output: list of contours
"""
def detectTape(frame):
    whiteRange = np.array([[0, 0, 200], [255, 50, 255]])
    tapeArea = 300
    tape = []

    gaussianBlur = cv2.GaussianBlur(frame.copy(), (5,5), 0)
    ret, thresh = cv2.threshold(gaussianBlur,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    hsvFrame = cv2.cvtColor(thresh, cv2.COLOR_BGR2HSV)
    whiteFilter = cv2.inRange(hsvFrame, whiteRange[0], whiteRange[1])

    contours, ret = cv2.findContours(whiteFilter, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours:
        approx = cv2.approxPolyDP(contour, 0.1*cv2.arcLength(contour, True), True)
        area = cv2.contourArea(contour)
        if area > 1000 and len(approx) == 4:
            tape.append(contour)
    return tape


"""
This method filters the tape found for the correct tilt and size ratio
    - Input: list of contours
    - Output: list of contours that has been filtered
"""
def filterAngleSize(contours):
    angle1, angle2 = 14.5, 75.5
    angleRange = 8
    hwRatio1, hwRatio2 = 2.75, 0.36
    hwRange = 0.5
    tape = []
    for contour in contours:
        ret, (w, h), angle = cv2.minAreaRect(contour)
        anglePos = abs(angle)
        if (anglePos > (angle1-angleRange) and anglePos < (angle1+angleRange)) or (anglePos > (angle2-angleRange) and anglePos < (angle2+angleRange)):
            if ((h/w) > hwRatio1-hwRange and (h/w) < hwRatio1+hwRange) or ((h/w) > hwRatio2-hwRange and (h/w) < hwRatio2+hwRange):
                tape.append(contour)
    return tape


"""
This method pairs contours that mirror each other to form goals
    - Input: List of tape contours
    - Output: 2D array, the outer array is array of goals inner ones are the two tapes that form the goal
"""
def pairTape(tapeArray):
    distRatio = 0.73125
    distRange = 1
    yRange = 40
    pairs = []

    for tape1 in tapeArray:
        for tape2 in tapeArray:
            [dx1, dy1], ret, ret = cv2.minAreaRect(tape1)
            [dx2, dy2], ret, ret = cv2.minAreaRect(tape2)
            distX = abs(dx1-dx2)

            pts1 = cv2.boxPoints(cv2.minAreaRect(tape1))
            pts2 = cv2.boxPoints(cv2.minAreaRect(tape2))

            disty1 = math.sqrt(pts1[0][1]*pts1[0][1] + pts1[2][1]*pts1[2][1])
            disty2 = math.sqrt(pts2[0][1]*pts2[0][1] + pts2[2][1]*pts2[2][1])

            if distX > 0:
                if (disty1/distX) > (distRatio-distRange) and (disty1/distX) < (distRatio+distRange) and (disty2/distX) > (distRatio-distRange) and (disty2/distX) < (distRatio+distRange):
                    if disty1 > disty2-yRange and disty1 < disty2+yRange:
                        pairs.append([tape1, tape2])
                        tapeArray.remove(tape1)
                        tapeArray.remove(tape2)
    return pairs


"""
This method calls the other methods to manage the tape detection process
    - Input: image to be processed
    - Output: Move Left (-1), Move Right (1), or No goal / goal is centered (0)
"""
def detectGoal(frame):
    contours = detectTape(frame)
    angleSizeArray = filterAngleSize(contours)
    tapePairs = pairTape(angleSizeArray)

    if len(tapePairs) >= 1:
        min_x = sys.maxsize
        max_x = -sys.maxsize
        for goal in tapePairs:
            for tape in goal:
                (x,y,w,h) = cv2.boundingRect(tape)
                frameh, framew, c = frame.shape
                min_x, max_x = min(x, min_x), max(x+w, max_x)
                centerX = min_x + (max_x-min_x)/2
                if centerX/framew > 0.55: return 1
                elif centerX/framew < 0.45: return -1
    else: return 0


"""
This section runs indefinetly, it generates frames from the camera and feeds them into the methods to locate the goals
    - Input: camera input
    - Output: sends info to piNetworkTables
"""
cap = cv2.VideoCapture(0)
NetworkTables.initialize(server='rasppifront')
Vision = NetworkTables.getTable('SmartDashboard')

while not (keyboard.is_pressed('q') and keyboard.is_pressed('ctrl')):
    _, frame = cap.read()
    Vision.putNumber('LeftRightValue', detectGoal(frame))
    cv2.waitKey(1)

cap.release()
