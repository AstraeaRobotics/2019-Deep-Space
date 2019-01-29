import cv2
import numpy as np

cap = cv2.VideoCapture(0)

if not cap.isOpened:
	raise IOError("Cannot open Webcam")
	
while True:
	ret, frame = cap.read()
	image = frame.copy()
	output = frame.copy()
	
	
	hsv = cv2.cvtColor(frame,cv2.COLOR_BGR2HSV)

	lowerOrg = np.array([0,80,80])
	upperOrg = np.array([20,255,255])
	
	mask = cv2.inRange(hsv, lowerOrg, upperOrg)
	mask2 = cv2.cvtColor(mask, cv2.COLOR_GRAY2BGR)
	
	res = cv2.bitwise_and(frame,mask2)
	res2 = cv2.cvtColor(res, cv2.COLOR_BGR2GRAY)
	#cv2.imshow('a',res)
	#assert len(mask.shape) == 2 and issubclass(mask.dtype.type, np.floating)
	#assert len(foreground_rgb.shape) == 3
	#assert len(background_rgb.shape) == 3
	
	#alpha3 = np.stack([mask]*3, axis=2)
	#blended + alpha3 * foreground_rgb + (1. -alpha3) * background_rgb
	#alpha3 = np.sqeeze(np.stack([np.atleast_3d(mask)]*3, axis=2))
	#imgray = cv2.cvtColor(res, cv2.COLOR_BGR2GRAY)
	
	#im3 = cv2.adaptiveThreshold(res2, 197, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 11, 2)
	#cv2.imshow('',im3)
	ret2,thresh = cv2.threshold(res2,27,255,cv2.THRESH_BINARY)
	#cv2.imshow('b',thresh)
	#cv2.imshow('',im3)
	#im2,contours,hierarchy = cv2.findContours(im3,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
	#cv2.drawContours(frame, contours, -1, (255,0,255), 1)
	#cv2.imshow('f',frame)
	#cv2.imshow('m',mask)
	#cv2.imshow('r', res)
	#cv2.imshow('s',frame)
	
	circles = cv2.HoughCircles(thresh, cv2.HOUGH_GRADIENT, 2.45, 20)
	 
		# ensure at least some circles were found
	if circles is not None:
			# convert the (x, y) coordinates and radius of the circles to integers
		circles = np.round(circles[0, :]).astype("int")
	 
			# loop over the (x, y) coordinates and radius of the circles
		for (x, y, r) in circles:
				# draw the circle in the output image, then draw a rectangle
				# corresponding to the center of the circle
			cv2.circle(output, (x, y), r, [255,255,0,10], -1)
			cv2.rectangle(output, (x - 1, y - 1), (x + 1, y + 1), (0, 128, 255,50), 1)
		# show the output image
	cv2.imshow("output", output)
	
	
	c = cv2.waitKey(3);
	
	if (c == 27):
		break
			
			
cv2.destroyAllWindows()
