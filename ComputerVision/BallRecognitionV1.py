import cv2
import numpy as np
import keyboard

#cap = cv2.VideoCapture(0)

def clamp(innn,lo,hi):


    if(innn>=lo and innn<=hi):
        return innn
    else:
        if(innn<lo):
            return lo
        if(innn>hi):
            return hi

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

contBallX = -1
contBallY = -1
contBallR = 0
cap = cv2.VideoCapture(1)
cTrack = np.array([0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0])

while not keyboard.is_pressed('esc'):
    _,frame = cap.read()
    image = frame.copy()
    h,w,c = image.shape
    bF,gF,rF = cv2.split(frame)

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    greyFrame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    greyHsv = cv2.cvtColor(hsv, cv2.COLOR_BGR2GRAY)
    #hsvEdge = cv2.Canny(cv2.GaussianBlur(hsv,(5,5),0),20,90)
    blurFrame = cv2.GaussianBlur(greyFrame,(5,5),0)

    #bgrEdge = cv2.Canny(cv2.GaussianBlur(frame,(5,5),0),20,90)

    greyadapt = cv2.adaptiveThreshold(blurFrame,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)
    hsvadapt = cv2.adaptiveThreshold(greyHsv,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)

    lowerOrange = np.array([2, 100, 100])
    upperOrange = np.array([17, 255, 255])

    tmask = cv2.inRange(hsv,lowerOrange,upperOrange)
    mask = cv2.cvtColor(tmask, cv2.COLOR_GRAY2BGR)

    tColorMask = cv2.bitwise_and(frame, mask)
    colorMask = cv2.cvtColor(tColorMask,cv2.COLOR_BGR2GRAY)

    #maskEdge = cv2.Canny(cv2.GaussianBlur(tColorMask,(5,5),0),20,90)

    threshImg1 = cv2.adaptiveThreshold(colorMask,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY,11,2)
    #cv2.imshow('thresh1',threshImg1)

    #imgList = np.array([greyHsv, greyFrame, greyadapt, hsvadapt,colorMask,threshImg1])

    imgList = np.array([threshImg1])
    cv2.circle(image, (contBallX, contBallY), 5, (0,255,255),-1)
    for i in imgList:
        circles = cv2.HoughCircles(i,cv2.HOUGH_GRADIENT,1.1,10, param1=220,param2=120,minRadius=2,maxRadius = 180)

        if circles is not None:
        	# convert the (x, y) coordinates and radius of the circles to integers
            circles = np.round(circles[0, :]).astype("int")
            circL = lgst(circles)
            x=circles[circL][0]
            y=circles[circL][1]
            r=circles[circL][2]
            #if(r!=0):
            if(contBallX == -1 and contBallY == -1):
                contBallX = x
                contBallY = y

            if(abs(contBallX-x)<=50 and abs(contBallY-y)<=50):
                contBallX = x
                contBallY= y
                print("SAME BALL")
            else:
                print("NOT SAME")

            cv2.circle(image, (contBallX, contBallY), 5, (0,0,255),-1)


            #print(r)

            cv2.circle(image, (x, y), r, (0, 255, 0), 1)
            j=0
            while j<=30:
                j1 = cTrack[j+2]
                j2 = cTrack[j+3]
                cTrack[j]=j1
                cTrack[j+1]=j2
                j+=2
            cTrack[31]=x
            cTrack[32]=y


            xl=clamp(x-r,0,w-1)
            xr=clamp(x+r,1,w)
            yl=clamp(y-r,0,h-1)
            yr=clamp(y+r,1,h)
            crpImg = frame[yl:yr,xl:xr]
            cv2.imshow('124',crpImg)
        		#cv2.rectangle(output, (x - 5, y - 5), (x + 5, y + 5), (0, 128, 255), -1)


    #cX = np.array([])
    #cY = np.array([])
    #cR = np.array([])
    i=0
    while i<=28:
        cv2.line(image,(cTrack[i+1],cTrack[i]),(cTrack[i+3],cTrack[i+2]),(0,0,0),5)
        i+=2

    cv2.imshow('masked Image', tColorMask)
    #cv2.imshow('hsvAdapt',hsvadapt)
    #cv2.imshow('regAdapt',greyadapt)
    #cv2.imshow('edgeHSV',hsvEdge)
    #cv2.imshow('edgeReg', bgrEdge)
    #cv2.imshow('maskedEdges',maskEdge)
    #cv2.imshow('grey',blurFrame)
    cv2.imshow('threshed',threshImg1)
    cv2.imshow('lotta Circz',image)
    #cv2.imshow('123',crpImg)
    '''
    cv2.imshow('cam Uno',hsv)
    cv2.imshow('b',bF)
    cv2.imshow('g',gF)
    cv2.imshow('r',rF)
    '''




    cv2.waitKey(1)

cap.release()
cv2.destroyAllWindows()
