/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.*;
import frc.robot.commands.HatchCommandTeleop;

/**
 * Add your docs here.
 */
public class HatchSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private OI oi;
  private Robot robot;

  public HatchSubsystem (OI oi, Robot robot) {
    this.oi = oi;
    this.robot = robot;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    Command command = new HatchCommandTeleop(oi, robot);
    command.start();
  }
}
