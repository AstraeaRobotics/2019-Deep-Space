import cv2
import numpy as np
import random

cap = cv2.VideoCapture(0)

if not cap.isOpened:
	raise IOError("Cannot open Webcam")
	
while True:
	ret, frame = cap.read()
	#cv2.imshow('Input', frame)
	
	
	imgray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	im3 = cv2.adaptiveThreshold(imgray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 5, 10)
	ret2,thresh = cv2.threshold(imgray,220,255,0)
	im2,contours,hierarchy = cv2.findContours(im3,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
	#mask = np.zeros(imgray.shape, np.uint8)
	#mean_val = cv2.mean(frame,mask = mask)
	#for i in contours:
		#np.squeeze(i)
		#cx = random.randint(0,255)
		
		#cy = random.randint(0,255)
		#cz = random.randint(0,255)
	cv2.drawContours(frame, contours, -1, (0,0,0), 1)
	#cv2.imshow('Input', im3)
	#cv2.imshow('1', imgray)
	cv2.imshow('2', frame)
	cv2.imshow('just lines', im2)
	
	c = cv2.waitKey(1);
	if (c == 27):
		break
			
cap.release()
cv2.destroyAllWindows()
