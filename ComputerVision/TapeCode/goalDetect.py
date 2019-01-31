import cv2
import numpy as np
import keyboard

"""
Finds reflective contours with 4 sides above a certain sides
"""
def detectTape(frame):
    #Sets the range of acepted colors in HSV
    whiteRange = np.array([[0, 0, 230], [255, 25, 255]])
    tape = [] #Hold contours being returned

    #Blurs the image to remove noise and smooth the details
    gaussianBlur = cv2.GaussianBlur(frame, (5,5), 0)
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
    tape = []
    for contour in contours:
        ret, (w, h), angle = cv2.minAreaRect(contour)
        if angle > 10 and angle < 20 and (h/w) > 2 and (h/w) < 4:
            tape.append(contour)
    return tape

def pairTape(tapeArray):
    pairs = []
    for tape in tapeArray:
        

#Starts capturing video in a VideoCapture object called cap
cap = cv2.VideoCapture(0)

#While the q button is  not pressed, the while loop runs
while not keyboard.is_pressed('q'):
    #ret is a placeholder (not used), reads a frame from cap then flips it horizontally
    ret, frame = cap.read()
    frame = cv2.flip(frame, 1)

    contours = detectTape(frame.copy()) #gets an array of tape contours
    filteredAngleSize = filterAngleSize(contours) #filters array by angle and size (skinny)
    tapePairs = pairTape(filterAngleSize) #finds tape pairs and returns in a 2D array

    for pair in tapePairs:
        approx = cv2.approxPolyDP(pair, 0.1*cv2.arcLength(pair, True), True)
        cv2.drawContours(frame, [approx], 0, (255, 0, 255), 5) #draws pink contours onto frame
        cv2.putText(frame, "Goal", (approx.ravel()[0], approx.ravel()[1]), cv2.FONT_HERSHEY_COMPLEX, 1, (0,0,0))

    #Display the pictures
    cv2.imshow('Tape-Detection: Final', frame) #Display the original image with the lines on top
    cv2.waitKey(1) #waitKey() is needed to update the frames of the imshow()

#Ends the capture and destroys the windows
cap.release()
cv2.destroyAllWindows()
