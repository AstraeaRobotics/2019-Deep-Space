/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import java.net.InetAddress;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import java.net.*;
import java.io.*;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxFrames;
import com.revrobotics.CANSparkMaxLowLevel;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;

public class Robot extends SampleRobot {
  double targetPositionRotations;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  CANSparkMax leftLead = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
  CANSparkMax leftFollow = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushed);
  //CANSparkMax winch = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushed);
  CANSparkMax rightLead = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
  CANSparkMax rightFollow= new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushed);
  private DifferentialDrive hdrive = new DifferentialDrive(leftLead, rightLead);
  private TalonSRX tempHatchMotor = new TalonSRX(7);
  private VictorSPX cargointakeDrive = new VictorSPX(10);
  private final Joystick m_stick = new Joystick(2);
  private final Joystick op = new Joystick(1);
  int absolutePosition;
  int x=0;
	//DoubleSolenoid hatchDoubleSolenoid = new DoubleSolenoid(6, 7);
	//Solenoid retaining = new Solenoid(0);
	private VictorSPX cargoshooter = new VictorSPX(9);

	/*double axis_5 = m_stick.getRawAxis(5);//Up Right joystick
	double axis_1 = m_stick.getRawAxis(2);//Left Left joystick
	double axis_0 = m_stick.getRawAxis(0);//right right joystick
	double axis_4 = m_stick.getRawAxis(4);//r2
	double axis_3 = m_stick.getRawAxis(3);//L2*/

	double ultscalingfactor = 5000/4.885; //mm/V
	Solenoid lightring = new Solenoid(7);

  public Robot() {
	  hdrive.setExpiration(0.1);
	}

  @Override
  public void robotInit() {
    /*m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Au
    to modes", m_chooser);*/
	lightring.set(true);
	leftFollow.follow(leftLead);
	rightFollow.follow(rightLead);
	leftLead.setOpenLoopRampRate(0.25);                                                                                                                      
	rightLead.setOpenLoopRampRate(0.25);
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

  
  @Override
  public void autonomous() {
    while(!isOperatorControl()&& isEnabled()) {
     /*axis_5 = 0.8*m_stick.getRawAxis(5);//Up Right joystick
      axis_1 = 0.8*m_stick.getRawAxis(1);//Left Left joystick
      axis_0 = m_stick.getRawAxis(0);//right right joystick
      axis_4 = m_stick.getRawAxis(4);//r2
      axis_3 = m_stick.getRawAxis(3);//L2

      
      hdrive.tankDrive(-axis_1, -axis_5);*/
      
    //Operator Controls
      /*if (op.getRawButton(2)) {
        System.out.println("Teehee");
        cargointakeDrive.set(ControlMode.PercentOutput, -0.50);
        cargoshooter.set(ControlMode.PercentOutput, -0.7);
      }

    else  if (op.getRawButtonPressed(5))
      {
        tempHatchMotor.set(ControlMode.PercentOutput, 0.15);
      }
   
    else if (op.getRawButtonPressed(6))
     {
      tempHatchMotor.set(ControlMode.PercentOutput, -0.15);
      //cargointakeDrive.set(ControlMode.PercentOutput,-0.2);
     // cargointakeDrive.set(ControlMode.PercentOutput,1);
     }
    else  if (op.getRawButtonPressed(7))
      {
        

        cargoshooter.set(ControlMode.PercentOutput, -0.6);

      }
      else if (op.getRawButtonPressed(8)){
        cargoshooter.set(ControlMode.PercentOutput, 0.8);
      }*/
      if(op.getRawButton(3)){
        tempHatchMotor.set(ControlMode.PercentOutput,-0.15);
        absolutePosition = tempHatchMotor.getSensorCollection().getPulseWidthPosition();
       System.out.println( "Encoder: " +absolutePosition);
      //  System.out.println("HATCH DOWN");
//System.out.println("WORKING!!!!");      
      }
      
      /*if(op.getRawButtonReleased(2)||op.getRawButtonReleased(3)||op.getRawButtonReleased(5)||op.getRawButtonReleased(6)||op.getRawButtonReleased(7)||op.getRawButtonReleased(8)){
        tempHatchMotor.set(ControlMode.PercentOutput,0);
        cargoshooter.set(ControlMode.PercentOutput,0);
        cargointakeDrive.set(ControlMode.PercentOutput, 0);
    //   System.out.println("TURN OFF ALL");
      }*/
    }
  }
   
  /*public double getPiValues() {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("Vision");
    NetworkTableEntry returnValue = table.getEntry("");

    return returnValue.getDouble(0.0);
  }

  public void autoAlign() {
    if(getPiValues() < 0) {
      while(getPiValues() < 0) {
        hdrive.curvatureDrive(.5, 1, false);
      }
    } else {
      while(getPiValues() > 0) {
        hdrive.curvatureDrive(.5, -1, false);
      }
    }
  }*/

  private int move_t = 0;
  @Override
  public void operatorControl() {
    hdrive.setSafetyEnabled(true);
    boolean backwards = false;
  
   System.out.println("OUTSIDE OP");
    while (isOperatorControl() && isEnabled()) {
      //System.out.println("Reading  " + getInches(ult1));
      //System.out.println("Reading 2 " + getInches(ult2));   
      /*  if(m_stick.getRawAxis(2)>0.3){
          if(m_stick.getRawAxis(4)>m_stick.getRawAxis(3)){
          hdrive.tankDrive(-0.5*m_stick.getRawAxis(4)-m_stick.getRawAxis(2),-0.5*m_stick.getRawAxis(4) );
          }
          else if(m_stick.getRawAxis(4)<m_stick.getRawAxis(3)){
            hdrive.tankDrive(-0.5*m_stick.getRawAxis(3)+m_stick.getRawAxis(2),-0.5*m_stick.getRawAxis(3) );
          }
        }
       else if(m_stick.getRawAxis(2)<-0.3){
          if(m_stick.getRawAxis(4)>m_stick.getRawAxis(3)){
          hdrive.tankDrive(-0.5*m_stick.getRawAxis(4),-0.5*m_stick.getRawAxis(4)-m_stick.getRawAxis(2) );
          }
          else if(m_stick.getRawAxis(4)<m_stick.getRawAxis(3)){
            hdrive.tankDrive(-0.5*m_stick.getRawAxis(3),-0.5*m_stick.getRawAxis(3)+m_stick.getRawAxis(2) );
            
          }
        }
        else{
          if(m_stick.getRawAxis(4)>m_stick.getRawAxis(3)){
            hdrive.tankDrive(-0.5*m_stick.getRawAxis(4),-0.5*m_stick.getRawAxis(4));
            }
            else if(m_stick.getRawAxis(4)<m_stick.getRawAxis(3)){
              hdrive.tankDrive(-0.5*m_stick.getRawAxis(3),-0.5*m_stick.getRawAxis(3) );
            }
        }*/
		hdrive.curvatureDrive(-0.75*m_stick.getRawAxis(5),m_stick.getRawAxis(2),m_stick.getRawButton(7));
      //System.out.println("Drive 1");
      /*axis_5 = 0.8*m_stick.getRawAxis(5);//Up Right joystick
      axis_1 = 0.8*m_stick.getRawAxis(1);//Left Left joystick
      axis_0 = 0.8*m_stick.getRawAxis(0);//right right joystick
      axis_4 = m_stick.getRawAxis(4);//r2
      axis_3 = m_stick.getRawAxis(3);//L2

      if(backwards) {
        hdrive.tankDrive(axis_5, axis_1);
      }
      else { 
        hdrive.tankDrive(-axis_1, -axis_5);
      }
      if(m_stick.getRawButton(1)) {
        backwards = true;
      }
      else if(m_stick.getRawButton(4)){
        backwards = false;
      }*/

  
                                                                                                                                                                                                                                                                                                                                                                         
    //Operator Control
      
    /*if (op.getRawButton(2))
    {
        System.out.println("Teehee");
        cargointakeDrive.set(ControlMode.PercentOutput, -0.50);
        cargoshooter.set(ControlMode.PercentOutput, -0.7);
    }
      
    else if (op.getRawButton(1)){
      cargointakeDrive.set(ControlMode.PercentOutput, 0.45);      
    }
    else if (op.getRawButtonPressed(5))
      {
        tempHatchMotor.set(ControlMode.PercentOutput, 0.15);
      }

      else if (op.getRawButton(3)) {
        winch.set(0.3);
      }
   
      else if(op.getRawButton(4)) {
        winch.set(-0.3);
      }*/
  
      
    
    if (op.getRawButtonPressed(6)) // 6 (1)
    {
      tempHatchMotor.set(ControlMode.PercentOutput, -0.15);
      ///System.out.print("Encoder position: " + tempHatchMotor.getSensorCollection().getQuadraturePosition());
      //System.out.print("Encoder velocity: " + tempHatchMotor.getSensorCollection().getQuadratureVelocity());
      //cargointakeDrive.set(ControlMode.PercentOutput,-0.2);
      //cargointakeDrive.set(ControlMode.PercentOutput,1);
    } 

     else if(op.getRawButton(6)) { // original button id 7 (2)
       tempHatchMotor.set(ControlMode.PercentOutput, 0.15);
     }
     
     else if(op.getRawButton(5)){ // 8 (3)
       tempHatchMotor.set(ControlMode.PercentOutput, -0.15);
       cargointakeDrive.set(ControlMode.PercentOutput,0);
       cargoshooter.set(ControlMode.PercentOutput, 0);
    }
    else if(op.getRawButtonPressed(7)){

      cargointakeDrive.set(ControlMode.PercentOutput,0.6);
      cargoshooter.set(ControlMode.PercentOutput, 0.7);
    }
    else if(op.getRawButtonPressed(8)){
      
      cargointakeDrive.set(ControlMode.PercentOutput,-0.6);
      cargoshooter.set(ControlMode.PercentOutput, -0.7);
    }
/*
    if(move_t -  tempHatchMotor.getSensorCollection().getQuadraturePosition() != 0){
    //if(tempHatchMotor.getSensorCollection().getQuadratureVelocity() != 0) {
      move_t =tempHatchMotor.getSensorCollection().getQuadraturePosition();
      System.out.println("Encoder position: " + move_t);
      //System.out.println("Encoder velocity: " + tempHatchMotor.getSensorCollection().getQuadratureVelocity());
    //}
    }*/
  
     /*else  if (op.getRawButtonPressed(7)) {
        cargoshooter.set(ControlMode.PercentOutput, -0.7);

      }
      else if (op.getRawButtonPressed(8)){
        cargoshooter.set(ControlMode.PercentOutput, 0.5);
      }
      /*else if(op.getRawButton(3)){
        tempHatchMotor.set(ControlMode.PercentOutput,-0.15);
        absolutePosition = tempHatchMotor.getSensorCollection().getPulseWidthPosition();
     //  System.out.println( "Encoder: " +absolutePosition);
      //  System.out.println("HATCH DOWN");
//System.out.println("WORKING!!!!");      
      }*/
      
      if(op.getRawButtonReleased(1)||op.getRawButtonReleased(2)||op.getRawButtonReleased(3)||op.getRawButtonReleased(5)||op.getRawButtonReleased(6)||op.getRawButtonReleased(7)||op.getRawButtonReleased(8)){
        tempHatchMotor.set(ControlMode.PercentOutput,0);
        cargoshooter.set(ControlMode.PercentOutput,0);
        cargointakeDrive.set(ControlMode.PercentOutput, 0);
    //   System.out.println("TURN OFF ALL");
      }      
    }
  }

    
  /**
   * Runs during test mode.
   */
  @Override
  public void test() {
  }

  public double getInches(AnalogInput a) {
    double volts = a.getValue();
    volts = volts/812.5;
    return volts*ultscalingfactor/25.4;
  }
}
