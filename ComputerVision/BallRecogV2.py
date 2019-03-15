import cv2
import numpy as np
import keyboard
import math
import random
from networktables import NetworkTables

# maps one val range to another
def translate(value, leftMin, leftMax, rightMin, rightMax):
    # Figure out how 'wide' each range is
    leftSpan = leftMax - leftMin
    rightSpan = rightMax - rightMin

    # Convert the left range into a 0-1 range (float)
    valueScaled = float(value - leftMin) / float(leftSpan)

    # Convert the 0-1 range into a value in the right range.
    return rightMin + (valueScaled * rightSpan)


# keeps value between low and hi val
def clamp(innn,lo,hi):
    if(innn>=lo and innn<=hi):
        return innn
    else:
        if(innn<lo):
            return lo
        if(innn>hi):
            return hi

#Iterates through circle and finds one with largest Radius
def lgst(listCirc):
    maxV = listCirc[0][2]
    maxIndx = 0
    cnt = 0
    for i in listCirc:
        if(i[2]>maxV):
            maxV = i[2]
            maxIndx = cnt
        cnt+=1
    return maxIndx

#Magic Code that makes image brighter and darker
def adjust_gamma(image, gamma=1.0):
	# build a lookup table mapping the pixel values [0, 255] to
	# their adjusted gamma values
	invGamma = 1.0 / gamma
	table = np.array([((i / 255.0) ** invGamma) * 255
		for i in np.arange(0, 256)]).astype("uint8")

	# apply gamma correction using the lookup table
	return cv2.LUT(image, table)


# Keep the value 0 for Laptop, 1 for Logitech Cameras
cap = cv2.VideoCapture(0)
NetworkTables.initialize(server='rasppifront')
Vision = NetworkTables.getTable('SmartDashboard')

move_Direction = 0
#Allowance For Ball Centering
center_Thresh = 120

# Image Blur Vals - MUST BE ODD NON-ZERO INTEGER
blurVal_1 = 5
blurVal_2 = 1

# Keyboard pressed Exit Loop
while not keyboard.is_pressed('esc'):
    move_Direction = 5

    #Inital Video Capture read
    _,frame = cap.read()

    #Randomly adjusts Gamma values to average out Dark and LIght Enviornments
    #frame = adjust_gamma(frame, gamma=clamp(3*random.random(),0.7,2.5))

    # 0 to 1 = dark \\ 1+ = brighter
    frame = adjust_gamma(frame, gamma=3)
    image = frame.copy()

    #Gets Height and Width
    h,w,_ = image.shape

    #Creates a blurred HSV image from BGR imput image
    hsv = cv2.GaussianBlur(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV),(blurVal_1,blurVal_1),0)

    #Keep values between [(1-3), (70-100), (70-100)]
    lower_Orange = np.array([2, 80, 80])
    #Keep values between [(15-19), (250-255), (250-255)]
    upper_Orange = np.array([17, 255, 255])

    #Masks all values outside Orange Ranges
    temp_Mask = cv2.inRange(hsv,lower_Orange,upper_Orange)
    overlay_Mask = cv2.cvtColor(temp_Mask, cv2.COLOR_GRAY2BGR)

    #applys mask to image
    temp_ColorMask = cv2.bitwise_and(frame, overlay_Mask)
    colorMask = cv2.cvtColor(temp_ColorMask,cv2.COLOR_BGR2GRAY)

    #Creates different blurry Adaptive threshed images
    threshImg1 = cv2.adaptiveThreshold(colorMask,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)
    threshImg2 = cv2.adaptiveThreshold(cv2.GaussianBlur(colorMask,(blurVal_2,blurVal_2),0), 255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)
    #threshImg3 = cv2.adaptiveThreshold(cv2.GaussianBlur(colorMask,(5,5),0), 255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)


    avgX = 0
    avgY = 0
    avgR = 0

    count_Circ = 0
    imgList = np.array([threshImg1, threshImg2])

    #Runs through all the added Images
    for i in imgList:
        circles = cv2.HoughCircles(i,cv2.HOUGH_GRADIENT,1.2,5, param1=210,param2=135,minRadius=1,maxRadius = 200)

        if circles is not None:
            count_Circ+=1

            #Magic shit, no touchy
            circles = np.round(circles[0, :]).astype("int")

            #Gets the largest value
            biggest_Circle = lgst(circles)
            x=circles[biggest_Circle][0]
            y=circles[biggest_Circle][1]
            r=circles[biggest_Circle][2]

            avgR+=r
            avgX+=x
            avgY+=y

            #cv2.circle(image, (math.floor(avgX), math.floor(avgY)), math.floor(avgR), (255,255,0), 5)

    if(not count_Circ==0):
        avgR/=count_Circ
        avgY/=count_Circ
        avgX/=count_Circ
        # not -1, 0 or 1 for robot to get no input
        move_Direction = 5
        if(avgX<(w/2)-center_Thresh):
            move_Direction = 1
        if(avgX>(w/2)+center_Thresh):
            move_Direction = -1
        if(avgX>(w/2)-center_Thresh and avgX<(w/2)+center_Thresh):
            move_Direction = 0




        #cv2.circle(image, (math.floor(avgX), math.floor(avgY)), math.floor(avgR), (0,0,0), 3)

    Vision.putNumber('LeftRightValue', move_Direction)
    #print(move_Direction)
    #cv2.imshow('masked Image', tColorMask)
    #cv2.imshow('threshed',threshImg2)
    #cv2.imshow('lotta Circz',image)

    cv2.waitKey(1)

cap.release()
cv2.destroyAllWindows()
