/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.OI;
import frc.robot.Constants;
import frc.robot.subsystems.SensorSubsystem;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxFrames;
import com.revrobotics.CANSparkMaxLowLevel;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveSubsystem extends Subsystem {
  private CANSparkMax omniMotor = new CANSparkMax(RobotMap.omniMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
  private DifferentialDrive hDrive = new DifferentialDrive(
    new CANSparkMax(RobotMap.leftMotor, CANSparkMaxLowLevel.MotorType.kBrushless),
    new CANSparkMax(RobotMap.rightMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
  );
  private OI m_oi;
  private SensorSubsystem m_SensorSubsystem;
  public DriveSubsystem(OI m_oi, SensorSubsystem m_SensorSubsystem){
    this.m_oi = m_oi;
    this.m_SensorSubsystem = m_SensorSubsystem;
  }

  public CANSparkMax getOmniMotor(){
    return omniMotor;
  }

  public DifferentialDrive getHDrive(){
    return hDrive;
  }

  public void drive(){
    if (m_oi.operator_gamepad.getRawButtonPressed(1)){
      hDrive.arcadeDrive(0, .1*(m_SensorSubsystem.getInches1() - m_SensorSubsystem.getInches2()));
    } else { // Drive code;
      if (/*m_oi.driver_gamepad.getRawAxis(2)*/ m_oi.readOmniAxis() > frc.robot.Constants.omniDeadzone){
          hDrive.arcadeDrive(
            -m_oi.readForwardAxis()*Constants.driveSpeed,
            -0.5
          );
          omniMotor.set(m_oi.readOmniAxis()*Constants.omniSpeed);
      } else if (/*m_oi.driver_gamepad.getRawAxis(2)*/ m_oi.readOmniAxis() < frc.robot.Constants.omniDeadzone){
        hDrive.arcadeDrive(
          -m_oi.readForwardAxis()*Constants.driveSpeed,
          0.5
        );
        omniMotor.set(m_oi.readOmniAxis()*Constants.omniSpeed);
      } else {
        hDrive.arcadeDrive(
          -m_oi.readForwardAxis()*Constants.driveSpeed,
          m_oi.readTurnAxis()*Constants.turnSpeed
        );
        omniMotor.set(0);
      }
    }
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
