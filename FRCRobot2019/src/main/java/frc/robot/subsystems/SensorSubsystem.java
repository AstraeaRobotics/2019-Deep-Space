/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class SensorSubsystem extends Subsystem {
  private AnalogInput ult1 = new AnalogInput(0);
	private AnalogInput ult2 = new AnalogInput(1);
	double ultscalingfactor = 5000/4.885;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public double getInches1() {
		double volts = ult1.getAverageVoltage();
		return volts*ultscalingfactor/25.4;
  }
  
  public double getInches2(){
    double volts = ult2.getAverageVoltage();
    return volts*ultscalingfactor/25.4;

  }
}
