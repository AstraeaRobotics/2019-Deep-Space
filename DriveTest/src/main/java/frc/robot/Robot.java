/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import main.java.frc.robot.Constants;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxFrames;
import com.revrobotics.CANSparkMaxLowLevel;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.*;

/**
* This is a demo program showing the use of the RobotDrive class. The
* SampleRobot class is the base of a robot application that will automatically
* call your Autonomous and OperatorControl methods at the right time as
* controlled by the switches on the driver station or the field controls.
*
* <p>The VM is configured to automatically run this class, and to call the
* functions corresponding to each mode, as described in the SampleRobot
* documentation. If you change the name of this class or the package after
* creating this project, you must also update the build.properties file in the
* project.
*
* <p>WARNING: While it may look like a good choice to use for your code if
* you're inexperienced, don't. Unless you know what you are doing, complex code
* will be much more difficult under this system. Use TimedRobot or
* Command-Based instead if you're new.
*/
public class Robot extends SampleRobot {
	double targetPositionRotations;
	private CANSparkMax omniMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private DifferentialDrive hdrive = new DifferentialDrive(new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless), new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless));
	private TalonSRX tempHatchMotor = new TalonSRX(7);
	private VictorSPX cargointakeDrive = new VictorSPX(8);
	// private CANSparkMax driveomni = new CANSparkMax(2, );
	private final Joystick m_stick = new Joystick(0);
	private final Joystick op = new Joystick(1);
	int absolutePosition;
	int x=0;
	DoubleSolenoid hatchDoubleSolenoid = new DoubleSolenoid(1, 2);
	Solenoid retaining = new Solenoid(0);
	private VictorSPX cargoshooter = new VictorSPX(9);
	private AnalogInput ult1 = new AnalogInput(0);
	private AnalogInput ult2 = new AnalogInput(1);
	double ultscalingfactor = 5000/4.885;
	
	public Robot() {
		hdrive.setExpiration(0.1);
		// absolutePosition = tempHatchMotor.getSensorCollection().getPulseWidthPosition();
	}
	
	@Override
	public void robotInit() {
		tempHatchMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		tempHatchMotor.config_kP(0, 0.250);
		//CameraServer.getInstance().startAutomaticCapture();
		// tempHatchMotor.setInverted(.kMotorInvert);
		/*m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
		m_chooser.addOption("My Auto", kCustomAuto);
		SmartDashboard.putData("Au
		to modes", m_chooser);*/
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
	
	/**
	* This autonomous (along with the chooser code above) shows how to select
	* between different autonomous modes using the dashboard. The sendable
	* chooser code works with the Java SmartDashboard. If you prefer the
	* LabVIEW Dashboard, remove all of the chooser code and uncomment the
	* getString line to get the auto name from the text box below the Gyro
	*
	* <p>You can add additional auto modes by adding additional comparisons to
	* the if-else structure below with additional strings. If using the
	* SendableChooser make sure to add them to the chooser code above as well.
	*
	* <p>If you wanted to run a similar autonomous mode with an TimedRobot
	* you would write:
	*
	* <blockquote><pre>{@code
	* Timer timer = new Timer();
	*
	* // This function is run once each time the robot enters autonomous mode
	* public void autonomousInit() {
	*     timer.reset();
	*     timer.start();
	* }
	*
	* // This function is called periodically during autonomous
	* public void autonomousPeriodic() {
	* // Drive for 2 seconds
	*     if (timer.get() < 2.0) {
	*         myRobot.drive(-0.5, 0.0); // drive forwards half speed
	*     } else if (timer.get() < 5.0) {
	*         myRobot.drive(-1.0, 0.0); // drive forwards full speed
	*     } else {
	*         myRobot.drive(0.0, 0.0); // stop robot
	*     }
	* }
	* }</pre></blockquote>
	*/
	@Override
	public void autonomous() {
		//String autoSelected = m_chooser.getSelected();
		// String autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		//System.out.println("Auto selected: " + autoSelected);
		
		// MotorSafety improves safety when motors are updated in loops
		// but is disabled here because motor updates are not looped in
		// this autonomous mode.
		// hdrive.setSafetyEnabled(false);
		
		//switch (autoSelected) {
		//case kCustomAuto:
		//}
	}
	
	/**
	* Runs the motors with arcade steering.
	*
	* <p>If you wanted to run a similar teleoperated mode with an TimedRobot
	* you would write:
	*
	* <blockquote><pre>{@code
	* // This function is called periodically during operator control
	* public void teleopPeriodic() {
	*     myRobot.arcadeDrive(stick);
	* }
	* }</pre></blockquote>
	*/
	@Override
	public void operatorControl() {
		hdrive.setSafetyEnabled(true);
		// System.out.println("OUTSIDE");
		while (isOperatorControl() && isEnabled()) {
			System.out.println("SENSOR LOOP IN PROGRESS");
			System.out.println("Reading 1 " + getInches(ult1));
			System.out.println("Reading 2" + getInches(ult2));
			if(op.getRawButtonPressed(1)) {
				while(Math.abs(getInches(ult1)-getInches(ult2)) < 0.5) {
					hdrive.arcadeDrive(0, 0.1*(getInches(ult1)-getInches(ult2)));
				}
			}
					
			// Drive arcade style
			//hdrive.arcadeDrive(-m_stick.getRawAxis(5), m_stick.getRawAxis(0));
			//double retardation = (m_stick.getRawAxis(2));
			// omniMotor.set(retardation);
			if(m_stick.getRawAxis(2)>.3)
			{				
				hdrive.arcadeDrive(
          			-m_stick.getRawAxis(5)*Constants.driveSpeed,
          			-0.5
        		);
				omniMotor.set(m_stick.getRawAxis(2)*Constants.omniSpeed);
			}
			else if(m_stick.getRawAxis(2)<-.3)
			{
        hdrive.arcadeDrive(
          -m_stick.getRawAxis(5)*Constants.driveSpeed,
          0.5
        );
				omniMotor.set(m_stick.getRawAxis(2*Constants.omniSpeed));
			}
			else 
			{
				/*hdrive.arcadeDrive(
          -m_stick.getRawAxis(5)*Constants.driveSpeed,
          m_stick.getRawAxis(0)*Constants.turnSpeed
		);*/
				hdrive.curvatureDrive(m_stick.getY(), m_stick.getX(), false);
				
				omniMotor.set(0);
			}
			//
			
			
			//System.out.println("AFTER");
			
			
			// if(op.getRawButtonPressed(1)){
			//targetPositionRotations = -6000.0 * 4096;
			// tempHatchMotor.set(ControlMode.PercentOutput,0.15);
			//tempHatchMotor.set(ControlMode.Position, targetPositionRotations);
			//System.out.println("ENCODER LOOP");
			//  System.out.println("Error: " + tempHatchMotor.getClosedLoopError(0)+"TargetPosition: "+targetPositionRotations + "Encoder Value: " + tempHatchMotor.getSelectedSensorPosition(0));
			//  System.out.println("HATCH UP");
			//  }
			if (op.getRawButtonPressed(2))
			{
				cargointakeDrive.set(ControlMode.PercentOutput, 1);
				cargoshooter.set(ControlMode.PercentOutput, -0.5*Constants.shooterSpeed);
			}
      else if (op.getRawButtonPressed(1))
      {
				retaining.set(true);
			}
      else if (op.getRawButtonPressed(4))
      {
				retaining.set(false);
			}
			else if (op.getRawButtonPressed(11))
			{
				hatchDoubleSolenoid.set(DoubleSolenoid.Value.kForward);
			}
      else if (op.getRawButtonPressed(12))
      {
				hatchDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);
			}
			else if (op.getRawButtonPressed(3))
			{
				cargointakeDrive.set(ControlMode.PercentOutput, -1);
			}
			else if (op.getRawButtonPressed(5))
			{
				tempHatchMotor.set(ControlMode.PercentOutput, 0.15*Constants.hatchSpeed);
			}
			else if (op.getRawButtonPressed(8))
			{
				cargoshooter.set(ControlMode.PercentOutput, 1);
			}
			else if (op.getRawButtonPressed(6))
			{
				tempHatchMotor.set(ControlMode.PercentOutput, -0.15*Constants.hatchSpeed);
				//cargointakeDrive.set(ControlMode.PercentOutput,-0.2);
				// cargointakeDrive.set(ControlMode.PercentOutput,1);
			}
			else if (op.getRawButtonPressed(7))
			{
				cargoshooter.set(ControlMode.PercentOutput, -1*Constants.shooterSpeed);
			}
			
			else if(op.getRawButtonPressed(3)){
				tempHatchMotor.set(ControlMode.PercentOutput, -0.15*Constants.hatchSpeed);
				absolutePosition = tempHatchMotor.getSensorCollection().getPulseWidthPosition();
				//  System.out.println( "Encoder: " +absolutePosition);
				//  System.out.println("HATCH DOWN");
				//System.out.println("WORKING!!!!");      
			}
			
			else if (op.getRawButtonReleased(2)||op.getRawButtonReleased(3)||op.getRawButtonReleased(5)||op.getRawButtonReleased(6)||op.getRawButtonReleased(7)||op.getRawButtonReleased(8)){
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
		double volts = a.getAverageVoltage();
		return volts*ultscalingfactor/25.4;
	}
}
