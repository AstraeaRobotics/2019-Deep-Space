import cv2
import numpy as np
import random

cap = cv2.VideoCapture(0)

if not cap.isOpened:
	raise IOError("Cannot open Webcam")

while True:
	# initialize the camera and grab a reference to the raw camera capture
	#camera = PiCamera()
	#camera.resolution = (640, 480)
	#camera.rotation = 180
	#camera.framerate = 20
	#rawCapture = PiRGBArray(camera, size=(640, 480))
	 
	# allow the camera to warmup
	#time.sleep(0.05)
	ret, frame = cap.read()
	# capture frames from the camera
	#for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
		# grab the raw NumPy array representing the image, then initialize the timestamp
		# and occupied/unoccupied text
	image = frame.copy()
	output = image.copy()

		#ap = argparse.ArgumentParser()
		#ap.add_argument("-i", "--imageframe", required = True, help = "Path to the image")
		#args = vars(ap.parse_args())
	gray2 = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
	#imgray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	gray = cv2.adaptiveThreshold(gray2, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 5, 10)

		# detect circles in the image
	circles = cv2.HoughCircles(gray, cv2.HOUGH_GRADIENT, 1.1, 100)
	 
		# ensure at least some circles were found
	if circles is not None:
			# convert the (x, y) coordinates and radius of the circles to integers
		circles = np.round(circles[0, :]).astype("int")
	 
			# loop over the (x, y) coordinates and radius of the circles
		for (x, y, r) in circles:
				# draw the circle in the output image, then draw a rectangle
				# corresponding to the center of the circle
			cv2.circle(output, (x, y), r, [255,255,0], -1)
			cv2.rectangle(output, (x - r, y - r), (x + r, y + r), (0, 128, 255,50), 1)
		# show the output image
	cv2.imshow("output", np.hstack([image, output]))
		#cv2.waitKey(0)
	c = cv2.waitKey(1);
	if (c == 27):
		break
			
cap.release()
cv2.destroyAllWindows()


	
	
 
	# clear the stream in preparation for the next frame
	#rawCapture.truncate(0)
 
	# if the `q` key was pressed, break from the loop
