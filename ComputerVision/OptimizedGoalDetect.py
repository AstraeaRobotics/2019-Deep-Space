import cv2
import numpy as np
import math
import sys
import genFrame

def detectTape(frame):
    whiteRange = np.array([[0, 0, 230], [255, 25, 255]])
    tapeArea = 300
    tape = []

    gaussianBlur = cv2.GaussianBlur(frame.copy(), (5,5), 0)
    hsvFrame = cv2.cvtColor(gaussianBlur, cv2.COLOR_BGR2HSV)
    whiteFilter = cv2.inRange(hsvFrame, whiteRange[0], whiteRange[1])

    cv2.imshow("filter", whiteFilter)

    contours, ret = cv2.findContours(whiteFilter, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours:
        approx = cv2.approxPolyDP(contour, 0.1*cv2.arcLength(contour, True), True)
        area = cv2.contourArea(contour)
        if area > 1000 and len(approx) == 4:
            tape.append(contour)
    return tape

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

def pairTape(tapeArray):
    distRatio = 0.73125
    distRange = 1
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

            if (distX > 0 and disty1/distX) > (distRatio-distRange) and (disty1/distX) < (distRatio+distRange) and (disty2/distX) > (distRatio-distRange) and (disty2/distX) < (distRatio+distRange):
                pairs.append([tape1, tape2])
                tapeArray.remove(tape1)
                tapeArray.remove(tape2)
    return pairs

def getAngleToGoal(x, w):
    return (x/w) * 78 #0 is the left edge, 78 is the far right edge

def detectGoal(frame):
    contours = detectTape(frame)
    angleSizeArray = filterAngleSize(contours)
    tapePairs = pairTape(angleSizeArray)

    if len(tapePairs) >= 1:
        min_x = sys.maxsize
        for goal in tapePairs:
            for tape in goal:
                (x,y,w,h) = cv2.boundingRect(tape)
                framew, frameh = cv2.GetSize(frame)
                min_x = min(x, min_x)
                centerX = min_x + w/2

                return(getAngleToGoal(centerX, framew))


cap = cv2.VideoCapture(0)

while not (keyboard.is_pressed('q') and keyboard.is_pressed('ctrl')):
    _, frame = cap.read()
    print(detectGoal(frame))
    cv2.imshow("FRAME", frame)
    cv2.waitKey(1)

cap.release()
cv2.destroyAllWindows()
