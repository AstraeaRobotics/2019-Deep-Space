/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PIDController;
import frc.robot.*;
import frc.robot.commands.*;

/**
 * Add your docs here.
 */
public class RampSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private Robot robot;
  private OI oi;
  private TalonSRX BAG_Motor;
  private Encoder AMT103;
  private TalonSRX _775Pro;
  private Solenoid Pneumatics;
  private PIDController PIDController;
  
  public RampSubsystem(OI oi, Robot robot) {
    this.robot = robot;
    this.oi = oi;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    Command command = new RampCommandTeleop(oi, this, robot);
    command.start();
  }
}
