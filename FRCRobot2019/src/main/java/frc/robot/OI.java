/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  protected GenericHID driver_gamepad = new Joystick(RobotMap.driverGamepad);
  public GenericHID getDriverGamepad(){ return driver_gamepad;};
  public double readForwardAxis(){return driver_gamepad.getRawAxis(ControlMap.forwardAxis);}
  public double readOmniAxis(){return driver_gamepad.getRawAxis(ControlMap.omniAxis);}
  public double readTurnAxis(){return driver_gamepad.getRawAxis(ControlMap.turnAxis);}
  public boolean readQuickTurn(){return driver_gamepad.getRawButton(ControlMap.quickTurnButton);}

  protected GenericHID operator_gamepad = new Joystick(RobotMap.operatorGamepad);
  public GenericHID getOperatorGamepad(){ return operator_gamepad;};
  protected Button cargoForwardButton = new JoystickButton(operator_gamepad, ControlMap.cargoForwardButton);
  protected Button cargoReverseButton = new JoystickButton(operator_gamepad, ControlMap.cargoReverseButton);
  protected Button cargoIntakeForwardButton = new JoystickButton(operator_gamepad, ControlMap.cargoIntakeForwardButton);
  protected Button cargoIntakeReverseButton = new JoystickButton(operator_gamepad, ControlMap.cargoIntakeReverseButton);
  protected Button hatchForwardButton = new JoystickButton(operator_gamepad, ControlMap.hatchForwardButton);
  protected Button hatchReverseButton = new JoystickButton(operator_gamepad, ControlMap.hatchReverseButton);
  protected Button hatchRetainButton = new JoystickButton(operator_gamepad, ControlMap.hatchRetainButton);
  protected Button hatchReleaseButton = new JoystickButton(operator_gamepad, ControlMap.hatchReleaseButton);
  protected Button hatchPushButton = new JoystickButton(operator_gamepad, ControlMap.hatchPushButton);
  //protected Button hatchRetractButton = new JoystickButton(operator_gamepad, RobotMap.og_hatchRetractButton);

  public OI(){

    cargoForwardButton.whileHeld(new CargoCommand(1));
    cargoReverseButton.whileHeld(new CargoCommand(-1));

    cargoIntakeForwardButton.whileHeld(new CargoIntakeCommand());
    cargoIntakeReverseButton.whileHeld(new CargoIntakeReverseCommand());

    hatchForwardButton.whileHeld(new HatchArmCommand(1));
    hatchReverseButton.whileHeld(new HatchArmCommand(-1));

    hatchRetainButton.whenPressed(new HatchRetainCommand(true));
    hatchReleaseButton.whenPressed(new HatchRetainCommand(false));

    hatchPushButton.whenPressed(new HatchPushCommand(true));
    hatchPushButton.whenReleased(new HatchPushCommand(false));

  }

  // please help

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
