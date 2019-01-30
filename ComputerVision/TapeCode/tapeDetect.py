"""
Sam Kriegsman

-The blue shaded area on the picture shows all the reflective parts in the Image (only purpose is for testing by showing reflective areas)
-The pink boxes show the contours found in the blue shaded ares if any
-The pink boxes labeled 'Tape' in the picture are the ones that have 4 sides and are of a specific size

"A vision target is a pair of 5½ in. (~14 cm) long by 2 in. (~5 cm) wide strips of 3M 8830 Scotchlite Reflective Material. Strips are
angled toward each other at ~14.5 degrees with a tolerance of approximately ±1 degree in respect to the
part to which it’s adhered (but please note, as stated earlier in this manual that “every effort is made to
ensure that ARENAS are consistent from event to event. However, ARENAS are assembled in different
venues by different event staff and some small variations occur”) and such that there’s an 8-in. (~20 cm)
gap at their closest points." (page 30)
"""

import cv2
import numpy as np
import keyboard

#Starts capturing video in a VideoCapture object called cap
cap = cv2.VideoCapture(0)

#While the q button is  not pressed, the while loop runs
while not keyboard.is_pressed('q'):
    #ret is a placeholder (not used), reads a frame from cap then flips it horizontally
    ret, frame = cap.read()
    frame = cv2.flip(frame, 1)

    #Sets the range of acepted colors in HSV
    whiteRange = np.array([[0, 0, 230], [255, 25, 255]])

    #Blurs the image to remove noise and smooth the details
    gaussianBlur = cv2.GaussianBlur(frame, (5,5), 0)
    #Converts from BGR to HSV to filter colors in the next step
    hsvFrame = cv2.cvtColor(gaussianBlur, cv2.COLOR_BGR2HSV)
    #Filters for white only and turns other colors black
    whiteFilter = cv2.inRange(hsvFrame, whiteRange[0], whiteRange[1])

    #Takes the frame that has been filtered for white and finds lines in it
    #cv2.HoughLinesP(image, distResolution, angleResolution, threshold, minLineLength, maxLineGap)
    lines = cv2.HoughLinesP(whiteFilter, 1, np.pi / 180, 10, maxLineGap=10)
    if lines is not None: #Runs if lines were found
        for line in lines: #For each line found
            x1, y1, x2, y2 = line[0] #Gets the lines coordinates
            #Draws the line onto the frame with thickness 5 and blue color
            cv2.line(frame, (x1, y1), (x2, y2), (200, 50, 0), 4)

    #Takes the white filtered frame and detects contours, then draws them over the line detection from above
    #contours, hierarchy = cv2.findContours(image, .....)
    #CHAIN_APPROX_SIMPLE simplifies the contours by removing uneeded points making it more memory efficient than CHAIN_APPROX_NONE
    contours, ret = cv2.findContours(whiteFilter, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    for contour in contours: #For each contour
        approx = cv2.approxPolyDP(contour, 0.1*cv2.arcLength(contour, True), True)
        area = cv2.contourArea(contour) #Find contours area
        if area > 1000 and len(approx) == 4: #only use those with a big area and 4 sides to eliminate noise
            #https://namkeenman.wordpress.com/2015/12/18/open-cv-determine-angle-of-rotatedrect-minarearect/
            print(cv2.minAreaRect(contour)) #prints (x,y), (w,h), angle
            cv2.drawContours(frame, [approx], 0, (147, 20, 255), 5) #draws pink contours onto frame
            x = approx.ravel()[0]
            y = approx.ravel()[1]
            cv2.putText(frame, "Tape", (x, y), cv2.FONT_HERSHEY_COMPLEX, 1, (0,0,0))

    #Display the pictures
    cv2.imshow('Tape-Detection: Final', frame) #Display the original image with the lines on top
    #cv2.imshow('Tape-Detection: No Image Recog', whiteFilter) #Display the filtered image that computer uses for contours and lines

    #waitKey() is needed to update the frames of the imshow()
    cv2.waitKey(1);

#Ends the capture and destroys the windows
cap.release()
cv2.destroyAllWindows()
