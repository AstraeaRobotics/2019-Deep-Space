/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Counter;
import frc.robot.*;

public class HatchCommandTeleop extends Command {
  private Robot robot;
  private OI oi;
  private TalonSRX motor;
  private DoubleSolenoid pneumatic;
  private Compressor c;
  private Counter pidEncoder;

  public HatchCommandTeleop(OI oi, Robot robot) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(robot.hatchSubsystem);
    this.oi = oi;
    this.robot = robot;
    this.motor = new TalonSRX(RobotMap.hatchMotor);
    this.pneumatic = oi.getHatchDoubleSolenoid();
    this.c = oi.getCompressor();
    this.pidEncoder = /*INSTANTIATION HERE*/;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    PID();
    pneumatic.set(pidEncoder.get());
  }

  @Override
  protected void initialize() {
    pidEncoder.reset();
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

  protected void PID() {
    double value = pidEncoder.getPeriod();
    double angleRAD = (value/9.739499999999999E-4)*2*(Math.PI);
    double target = (angleRAD*1024)/(2*Math.PI);
    pidEncoder.set(Constants.hatchConstant * (Constants.currentVal - target));
  }
}
