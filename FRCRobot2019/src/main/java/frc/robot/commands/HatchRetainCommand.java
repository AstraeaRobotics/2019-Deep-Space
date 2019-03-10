/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.HatchSubsystem;

/**
 * An example command.  You can replace me with your own command.
 */
public class HatchRetainCommand extends Command {
  private HatchSubsystem m_HatchSubystem = Robot.m_HatchSubsystem;
  boolean enabled = false;

  public HatchRetainCommand(boolean enabled) {
    // Use requires() here to declare subsystem dependencies
    this.enabled = enabled;
    requires(Robot.m_HatchSubsystem);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_HatchSubystem.enablePanelHolder(enabled);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
