/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import frc.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PWMVictorSPX;

import com.ctre.phoenix.motorcontrol.ControlMode;


/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class CargoSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSPX cargoShooter = new VictorSPX(RobotMap.cargoShooter);
  private VictorSPX cargoIntakeDrive = new VictorSPX(RobotMap.cargoIntakeDrive);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void setMotorSpeed(double motorSpeed){
    cargoShooter.set(ControlMode.PercentOutput, motorSpeed);
  }

  public void setIntakeMotorSpeed(double motorSpeed){
    cargoIntakeDrive.set(ControlMode.PercentOutput, motorSpeed);
  }
}
