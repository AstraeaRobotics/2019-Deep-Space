/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class CameraSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void initializeCamera1(){
    new Thread(() -> {
			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
			camera1.setResolution(640, 480);
			CvSink cvsink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
			
			Mat source = new Mat();
			Mat output = new Mat();
			
			while(!Thread.interrupted()) {
				cvsink.grabFrame(source);
				Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
				outputStream.putFrame(output);
			}
		}).start();
  }

  public void initializeCamera2(){
    new Thread(() -> {
			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
			camera2.setResolution(640, 480);
			CvSink cvsink1 = CameraServer.getInstance().getVideo();
			CvSource outputStream1 = CameraServer.getInstance().putVideo("Blur", 640, 480);
			
			Mat source1 = new Mat();
			Mat output1 = new Mat();
			
			while(!Thread.interrupted()) {
				cvsink1.grabFrame(source1);
				Imgproc.cvtColor(source1, output1, Imgproc.COLOR_BGR2GRAY);
				outputStream1.putFrame(output1);
			}
		}).start();
  }
}
