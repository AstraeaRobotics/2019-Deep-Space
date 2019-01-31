/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import frc.robot.*;
import frc.robot.commands.*;

/**
 * Add your docs here.
 */
public class I2CDataSubsystem extends Subsystem {
<<<<<<< HEAD
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Robot robot;
    private OI oi;
    private Robot.Mode system;
    private I2C colorSensorLeft;
    private I2C colorSensorCenter;
    private I2C colorSensorRight;

    protected final Byte COMMAND_REGISTER_BIT = 0x80;
    protected final Byte MULTI_BYTE_BIT = 0x20;
    protected final static int RDATA_REGISTER  = 0x16;
    protected final static int GDATA_REGISTER  = 0x18;
    protected final static int BDATA_REGISTER  = 0x1A;

    public I2CDataSubsystem(OI oi, Robot robot, Robot.Mode system) {
        this.oi = oi;
        this.robot = robot;
        this.system = system;
    }

    @Override
    public void initDefaultCommand() {}
=======
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private Robot robot;
  private OI oi;
  private robot.Mode system;
  private I2C colorSensorLeft;
  private I2C colorSensorCenter;
  private I2C colorSensorRight;

  public I2CDataSubsystem(OI oi, Robot robot, robot.Mode system) {
    this.oi = oi;
    this.robot = robot;
    this.system = system;
  }

  @Override
  public void initDefaultCommand() {}
>>>>>>> d157aacb9d5c8a1e14c0f3ff7400241756fb81f8

    public double getColorMovement() {
        private ByteBuffer buffyLeft = ByteBuffer.allocate(8);
        private ByteBuffer buffyCenter = ByteBuffer.allocate(8);
        private ByteBuffer buffyRight = ByteBuffer.allocate(8);

        if (system == robot.Mode.HATCH) {
            colorSensorLeft = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorLeftPortHatch);
            colorSensorLeft.write(COMMAND | 0x00, 0b00000011);
            colorSensorCenter = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorCenterPortHatch);
            colorSensorCenter.write(COMMAND | 0x00, 0b00000011);
            colorSensorRight = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorRightPortHatch);
            colorSensorRight.write(COMMAND | 0x00, 0b00000011);
        } else if (system == Robot.Mode.CANNON) {
            colorSensorLeft = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorLeftPortCannon);
            colorSensorLeft.write(COMMAND | 0x00, 0b00000011);
            colorSensorCenter = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorCenterPortCannon);
            colorSensorCenter.write(COMMAND | 0x00, 0b00000011);
            colorSensorRight = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorRightPortCannon);
            colorSensorRight.write(COMMAND | 0x00, 0b00000011);
        }
        
        colorSensorLeft.read(COMMAND | MULTI_BYTE_BIT | RDATA_REGISTER, 6, buffyLeft);
        private short red = buffyLeft.getShort(0);
        private short green = buffyLeft.getShort(2);
        private short blue = buffyLeft.getShort(4);
        private short whiteLeft = (short)(red + green + blue)/3;

        colorSensorCenter.read(COMMAND | MULTI_BYTE_BIT | RDATA_REGISTER, 6, buffyCenter);
        private short red = buffyLeft.getShort(0);
        private short green = buffyLeft.getShort(2);
        private short blue = buffyLeft.getShort(4);
        private short whiteCenter = (short)(red + green + blue)/3;

        colorSensorRight.read(COMMAND | MULTI_BYTE_BIT | RDATA_REGISTER, 6, buffyRight);
        private short red = buffyLeft.getShort(0);
        private short green = buffyLeft.getShort(2);
        private short blue = buffyLeft.getShort(4);
        private short whiteRight = (short)(red + green + blue)/3;

        if (whiteLeft > whiteCenter) {
            
        }

    }
}
