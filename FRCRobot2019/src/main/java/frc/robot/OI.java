/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxFrames;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  protected GenericHID driver_gamepad = new Joystick(RobotMap.driveGamepad); //Ports and joystick mapping to be changed later
  //Playstation DualShock 4 Mapping
  // Axis 0:    Left Stick X
  // Axis 1:    Left Stick Y
  // Axis 2:    Right Stick X
  // Axis 3:    Left Bumper
  // Axis 4:    Right Bumper
  // Axis 5:    RIght Stick Y
  //protected Button driveModifier = JoystickButton(driver_gamepad, RobotMap.driveModifierButton);
  //protected Button spinModifier = JoystickButton(driver_gamepad, RobotMap.spinModifierButton);
  protected CANSparkMax driveomni;
  protected DoubleSolenoid hatchDoubleSolenoid;
  protected Compressor compressor;
  protected boolean isAutomated = false;

  public GenericHID getDriverGamepad() {
    return driver_gamepad;   
  }

  public DifferentialDrive getRobotDrive() {
    DifferentialDrive hdrive = new DifferentialDrive(new CANSparkMax(RobotMap.leftDriveMotor, CANSparkMaxLowLevel.MotorType.kBrushless), new CANSparkMax(RobotMap.rightDriveMotor, CANSparkMaxLowLevel.MotorType.kBrushless));
    hdrive.setSafetyEnabled(true);
    hdrive.setExpiration(Constants.expirationTime);
    return hdrive;
  }

  public CANSparkMax getOmniMotor() {
    driveomni = new CANSparkMax(RobotMap.hDriveMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    return driveomni;
  }

  public DoubleSolenoid getHatchDoubleSolenoid() {
    hatchDoubleSolenoid = new DoubleSolenoid(RobotMap.hatchPneumaticForward, RobotMap.hatchPneumaticBackward);
    return hatchDoubleSolenoid;
  }

  public Compressor getCompressor() {
    compressor = new Compressor(RobotMap.compressor);
    return compressor;
  }

  public boolean isAutomated(){
    return isAutomated;
  }

  public void setAutomated(boolean isAutomated){
    this.isAutomated = isAutomated;
  }

  //I am editing the code in some way
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
}
