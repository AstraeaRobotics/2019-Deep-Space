/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

  //JOYSTICK PORTS
  public static int driveGamepad = 0;

  //CAN PORTS
  public static int leftDriveMotor = 0;
  public static int rightDriveMotor = 1;
  public static int hDriveMotor = 2;
  public static int hatchMotor = 3;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

  //Pneumatic Shtuff
  public static int hatchPneumaticForward = 0;
  public static int hatchPneumaticBackward = 1;
  
  public static int compressor = 0;

  // Shooter and Intake for Cargo
  public static int cargoVictor1p = 0;
  public static int cargoVictor2p = 0;

  // All the ramp things :|
  public static int rampBAGMotor = 0;
  public static int ramp775Pro = 0;
  public static int rampDigitalInput1 = 0;
  public static int rampDigitalInput2 = 0;
  public static int rampPneumatics = 0;

  public static int alignmentLSensorIN = 0;
  public static int alignmentLSensorOUT = 0;
  public static int alignmentMSensorIN = 0;
  public static int alignmentMSensorOUT = 0;
  public static int alignmentRSensorIN = 0;
  public static int alignmentRSensorOUT = 0;

  //Verrrrrry arbitrary number here that will need to be edited
  public static int rampEncoderTicks = 30;
}