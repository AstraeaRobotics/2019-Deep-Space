/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.*;

public class HatchCommandTeleop extends Command {
  private Robot robot;
  private OI oi;
  private TalonSRX motor;
  private DoubleSolenoid pneumatic;
  private Compressor c;
  private Encoder pidEncoder;
  Counter count;

  public HatchCommandTeleop(OI oi, Robot robot) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(robot.hatchSubsystem);
    this.oi = oi;
    this.robot = robot;
    this.motor = new TalonSRX(RobotMap.hatchMotor);
    this.pneumatic = oi.getHatchDoubleSolenoid();
    this.c = oi.getCompressor();
    this.pidEncoder =  oi.getHatchPIDEncoder();
    //this.count = new Counter(pidEncoder);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    while(oi.getDriverGamepad().getRawButton(0)) {
      motor.set(.5);
    }

    while(oi.getDriverGamepad().getRawButton(1)) {
      motor.set(-.5);
    }
  }

  @Override
  protected void initialize() {
    pidEncoder.reset();
    count.setSemiPeriodMode(true);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
