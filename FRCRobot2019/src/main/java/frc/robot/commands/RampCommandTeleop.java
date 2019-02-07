/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.*;
import frc.robot.*;
import frc.robot.subsystems.*;

public class RampCommandTeleop extends Command {

  TalonSRX BAG_Motor = new TalonSRX(RobotMap.rampBAGMotor); // COULD CHANGE TO SPARKMAX
  Encoder AMT103 = new Encoder(RobotMap.rampDigitalInput1, RobotMap.rampDigitalInput2,false, Encoder.EncodingType.k4X);
  TalonSRX _775Pro = new TalonSRX(RobotMap.ramp775Pro);
  DoubleSolenoid Pneumatics = new DoubleSolenoid(2,3); // or double solenoid?

  public RampCommandTeleop(OI oi, Subsystem sub, Robot robot) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);


    // AMT103.setMaxPeriod(.1);
    // AMT103.setMinRate(10);
    // AMT103.setDistancePerPulse(5);
    // AMT103.setReverseDirection(false);
    // AMT103.setSamplesToAverage(7);

    requires(sub);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true; // Jump to end method
  }

  /*
    order:
      1. bag motor foot opens
      2. ???? knocks down ramp
      3. cylinder on drive base pushes ramp up + 2 flaps open up simultaneously (solenoid)
      4. winch pulls ramp up (775 pro)
  */

  // Called once after isFinished returns true
  @Override
  protected void end() { // No exact numbers currently.
      //BAG_Motor.set(ControlMode.PercentOutput, 1); 
      // Knock down ramp...
      Pneumatics.set(DoubleSolenoid.Value.kOff); // idk what we're using yet so no code.
      //_775Pro.set(1);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  protected void PIDLower () {
    boolean keepGoing = true;
    int pulseWidth = _775Pro.getSensorCollection().getPulseWidthPosition();
    int end = pulseWidth + RobotMap.ticksInAngle;
    while (keepGoing) {
      if (pulseWidth - end >= 3) {
        _775Pro.set(ControlMode.PercentOutput, RobotMap.rampPIDConstant * (pulseWidth - end));
      } else {
        keepGoing = false;
      }
    }
  }

  protected void PIDRaise () {
    boolean keepGoing = true;
    int pulseWidth = _775Pro.getSensorCollection().getPulseWidthPosition();
    int end = pulseWidth + RobotMap.ticksInAngle;
    while (keepGoing) {
      if (pulseWidth - end >= 3) {
        _775Pro.set(ControlMode.PercentOutput, -1 * RobotMap.rampPIDConstant * (pulseWidth - end));
      } else {
        keepGoing = false;
      }
    }
  }
}
