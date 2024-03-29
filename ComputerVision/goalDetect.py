import cv2
import numpy as np
import keyboard
import math
import sys

"""
Finds tape as seen in tapeDetect.py then filters the tape to find goals
http://answers.opencv.org/question/28489/how-to-compare-two-contours-translated-from-one-another/
https://www.digitalocean.com/community/tutorials/how-to-use-the-python-debugger
"""
def detectTape(frame):
    #Sets the range of acepted colors in HSV
    whiteRange = np.array([[0, 0, 230], [255, 25, 255]])
    tapeArea = 300
    tape = [] #Hold contours being returned

    #Blurs the image to remove noise and smooth the details
    gaussianBlur = cv2.GaussianBlur(frame.copy(), (5,5), 0)
    #Converts from BGR to HSV to filter colors in the next step
    hsvFrame = cv2.cvtColor(gaussianBlur, cv2.COLOR_BGR2HSV)
    #Filters for white only and turns other colors black
    whiteFilter = cv2.inRange(hsvFrame, whiteRange[0], whiteRange[1])

    #Takes the white filtered frame and detects contours, then draws them over the line detection from above
    #contours, hierarchy = cv2.findContours(image, .....)
    #CHAIN_APPROX_SIMPLE simplifies the contours by removing uneeded points making it more memory efficient than CHAIN_APPROX_NONE
    contours, ret = cv2.findContours(whiteFilter, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours: #For each contour
        approx = cv2.approxPolyDP(contour, 0.1*cv2.arcLength(contour, True), True)
        area = cv2.contourArea(contour) #Find contours area
        if area > 1000 and len(approx) == 4: #only use those with a big area and 4 sides to eliminate noise
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
            print('\033[93m' + "ANGLE ANGLE ANGLE ANGLE ANGLE")
            if ((h/w) > hwRatio1-hwRange and (h/w) < hwRatio1+hwRange) or ((h/w) > hwRatio2-hwRange and (h/w) < hwRatio2+hwRange):
                tape.append(contour)
                print('\033[93m' + "HW HW HW HW HW HW HW")
    return tape

def pairTape(tapeArray):
    distRatio = 0.73125
    distRange = 1

    pairs = []
    for tape1 in tapeArray:
        for tape2 in tapeArray:
            #Calculates the x-distance
            [dx1, dy1], ret, ret = cv2.minAreaRect(tape1)
            [dx2, dy2], ret, ret = cv2.minAreaRect(tape2)
            distX = abs(dx1-dx2)

            #Calculates the y-distance (hypotenous of tape)
            pts1 = cv2.boxPoints(cv2.minAreaRect(tape1))
            pts2 = cv2.boxPoints(cv2.minAreaRect(tape2))

            disty1 = math.sqrt(pts1[0][1]*pts1[0][1] + pts1[2][1]*pts1[2][1])
            disty2 = math.sqrt(pts2[0][1]*pts2[0][1] + pts2[2][1]*pts2[2][1])

            if(distX > 0):
                print('\033[96m' + "Dist is > 0")
                if (disty1/distX) > (distRatio-distRange) and (disty1/distX) < (distRatio+distRange) and (disty2/distX) > (distRatio-distRange) and (disty2/distX) < (distRatio+distRange):
                    print('\033[93m' + "DIST DIST DIST DIST DIST -->", disty1/distX, " --> ", disty2/distX)
                    pairs.append([tape1, tape2])
                    tapeArray.remove(tape1)
                    tapeArray.remove(tape2)

    return pairs

#Starts capturing video in a VideoCapture object called cap
cap = cv2.VideoCapture(0)
frameNumber = 0

#While the q button is  not pressed, the while loop runs
while not keyboard.is_pressed('q'):
    #ret is a placeholder (not used), reads a frame from cap then flips it horizontally
    ret, frame = cap.read()
    frame = cv2.flip(frame, 1)

    frameNumber += 1 #increment frame counter
    print()
    print()

    contours = detectTape(frame) #gets an array of tape contours
    angleSizeArray = filterAngleSize(contours) #filters array by angle and size (skinny)
    tapePairs = pairTape(angleSizeArray) #finds tape pairs and returns in a 2D array

    for contour in contours: #shows tape that was detected even if it isnt a goal
        approx = cv2.approxPolyDP(contour, 0.1*cv2.arcLength(contour, True), True)
        cv2.drawContours(frame, [approx], 0, (255, 0, 255), 2)

    if len(tapePairs) >= 1: #If a goal was found
        print('\033[92m' + "--- GOAL WAS LOCATED ---") #prints success in green
        min_y, min_x = sys.maxsize, sys.maxsize
        max_y, max_x = -sys.maxsize - 1, -sys.maxsize - 1
        for goal in tapePairs: #For each goal
            for tape in goal: #for every tape in the pair find the x,y of the bounding box
                (x,y,w,h) = cv2.boundingRect(tape)
                min_x, max_x = min(x, min_x), max(x+w, max_x)
                min_y, max_y = min(y, min_y), max(y+h, max_y)
            cv2.rectangle(frame, (min_x, min_y), (max_x, max_y), (0, 255, 0), 2) #adds rectangle around goal onto the frame
            cv2.putText(frame, "Goal", (min_x, min_y), cv2.FONT_HERSHEY_COMPLEX, 1, (0,0,0)) #adds label tot he goal

    else: #if the frame had no goals
        #Prints error message in red
        print('\033[91m' + "ERR: No Goals in Frame -->  ", frameNumber)

    #Display the pictures
    cv2.imshow('Goal-Detection: Final', frame) #Display the original image with the lines on top
    cv2.waitKey(1) #waitKey() is needed to update the frames of the imshow()

#Ends the capture and destroys the windows
cap.release()
cv2.destroyAllWindows()
print('\033[0m') # resets font color in terminal
