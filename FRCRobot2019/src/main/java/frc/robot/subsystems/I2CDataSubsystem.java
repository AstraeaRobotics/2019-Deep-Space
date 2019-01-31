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
import frc.robot.commands.*;

/**
 * Add your docs here.
 */
public class I2CDataSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private Robot robot;
  private OI oi;
  private Robot.Mode system;
  private I2C colorSensorLeft;
  private I2C colorSensorCenter;
  private I2C colorSensorRight;

  public I2CDataSubsystem(OI oi, Robot robot, Robot.Mode system) {
    this.oi = oi;
    this.robot = robot;
    this.system = system;
  }

  @Override
  public void initDefaultCommand() {}

    public double getColorMovement() {
        private ByteBuffer buffyLeft = ByteBuffer.allocate(8);
        private ByteBuffer buffyCenter = ByteBuffer.allocate(8);
        private ByteBuffer buffyRight = ByteBuffer.allocate(8);

        if (system == Robot.Mode.HATCH) {
            colorSensorLeft = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorLeftPortHatch);
            colorSensorLeft.write(0x80 | 0x00, 0b00000011);
            colorSensorCenter = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorCenterPortHatch);
            colorSensorCenter.write(0x80 | 0x00, 0b00000011);
            colorSensorRight = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorRightPortHatch);
            colorSensorRight.write(0x80 | 0x00, 0b00000011);
        }
        
        colorSensorLeft.read(0x80 | 0x20 | 0x16, 8, buffyLeft);
        ByteBuffer compBuffer = ByteBuffer.wrap(buffyLeft);
        compBuffer.order(ByteOrder.BIG_ENDIAN);
        
        colorSensorLeft.read(0x80 | 0x20 | 0x16, 8, buffyLeft);
        colorSensorLeft.read(0x80 | 0x20 | 0x16, 8, buffyLeft);

    }
}
