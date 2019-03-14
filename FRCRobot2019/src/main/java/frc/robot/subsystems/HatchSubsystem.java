/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;


/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class HatchSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private TalonSRX hatchMotor = new TalonSRX(RobotMap.hatchMotor);
  private Solenoid panelHolder = new Solenoid(RobotMap.panelHolder);
  private DoubleSolenoid pushSolenoid = new DoubleSolenoid(RobotMap.hatchPushForwardCh, RobotMap.hatchPushReverseCh);


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void initialize(){
    hatchMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    hatchMotor.config_kP(0, 0.250);
  }

  public void enablePanelHolder(boolean active){
    panelHolder.set(active);
  }

  public void pushHatch(boolean out){
    if (out){
      pushSolenoid.set(DoubleSolenoid.Value.kForward);
    }else{
      pushSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }

  public void setMotorSpeed(double motorSpeed){
    hatchMotor.set(ControlMode.PercentOutput, motorSpeed);
  }
}
