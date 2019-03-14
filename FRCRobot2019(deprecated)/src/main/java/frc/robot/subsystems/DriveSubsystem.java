/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.commands.DriveCommandTeleop;

public class DriveSubsystem extends Subsystem {
  private Robot robot;
  private OI oi;
 

  public DriveSubsystem(OI oi, Robot robot) {
    this.robot = robot;
    this.oi = oi;
  }

  @Override
  public void initDefaultCommand() {
    Command command = new DriveCommandTeleop(oi, robot);
    command.start();
    
    // setDefaultCommand(new MySpecialCommand());
  }
}
