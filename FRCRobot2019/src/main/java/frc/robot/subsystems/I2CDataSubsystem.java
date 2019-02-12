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
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Robot robot;
    private OI oi;
    private I2C colorSensorLeft;
    private I2C colorSensorCenter;
    private I2C colorSensorRight;

    protected final int CMD = 0x80;
    protected final int MULTI_BYTE_BIT = 0x20;
    protected final static int RDATA_REGISTER  = 0x16;
    protected final static int GDATA_REGISTER  = 0x18;
    protected final static int BDATA_REGISTER  = 0x1A;
    protected final static int PON   = 0b00000001;
    protected final static int AEN   = 0b00000010;
    protected final static int PEN   = 0b00000100;
    private final double integrationTime = 10;

    public I2CDataSubsystem(OI oi, Robot robot) {
        this.oi = oi;
        this.robot = robot;
    }

    @Override
    public void initDefaultCommand() {}

    public double getColorMovement() {
        //....
        //reeeeeeeeeeeeeeeeee
        final ByteBuffer buffyLeft = ByteBuffer.allocate(8);
        final ByteBuffer buffyCenter = ByteBuffer.allocate(8);
        final ByteBuffer buffyRight = ByteBuffer.allocate(8);

        //public short red1 = 0, green1 = 0, blue1 = 0, red2 = 0, green2 = 0, blue2 = 0, red3 = 0, green3 = 0, blue3 = 0;

      
        colorSensorLeft = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorLeft); 
        colorSensorCenter = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorCenter);
        colorSensorRight = new I2C(I2C.Port.kOnboard, RobotMap.colorSensorRight);

        colorSensorLeft.write(CMD | 0x00, PON | AEN); /// Powers on Color Sensor
        colorSensorLeft.write(CMD | 0x01, (int) (256-integrationTime/2.38));
        colorSensorLeft.write(CMD | 0x0E, 0b1111);

        colorSensorCenter.write(CMD | 0x00, PON | AEN);
        colorSensorCenter.write(CMD | 0x00, (int)(256-integrationTime/2.38));
        colorSensorCenter.write(CMD | 0x0E, 0b1111);

        colorSensorRight.write(CMD | 0x00, PON | AEN);
        colorSensorRight.write(CMD | 0x00, (int)(256-integrationTime/2.38));
        colorSensorRight.write(CMD | 0x0E, 0b1111);        
        
        colorSensorLeft.read(CMD | MULTI_BYTE_BIT | RDATA_REGISTER, 8, buffyLeft);
        short red1 = buffyLeft.getShort(0);
        if(red1<0){red1+=-65536;};
        short green1 = buffyLeft.getShort(2);
        if(green1<0){green1+=-65536;};
        short blue1 = buffyLeft.getShort(4);
        if(blue1<0){blue1+=-65536;};
        short whiteLeft = (short)((red1 + green1 + blue1)/3);

        colorSensorCenter.read(CMD | MULTI_BYTE_BIT | RDATA_REGISTER, 8, buffyCenter);
        short red2 = buffyCenter.getShort(0);
        if(red2<0){red2+=-65536;};
        short green2 = buffyCenter.getShort(2);
        if(green2<0){green2+=-65536;};
        short blue2 = buffyCenter.getShort(4);
        if(blue2<0){blue2+=-65536;};
        short whiteCenter = (short)((red2 + green2 + blue2)/3);

        colorSensorRight.read(CMD | MULTI_BYTE_BIT | RDATA_REGISTER, 8, buffyRight);
        short red3 = buffyRight.getShort(0);
        if(red3<0){red3+=-65536;};
        short green3 = buffyRight.getShort(2);
        if(green3<0){green3+=-65536;};
        short blue3 = buffyRight.getShort(4);
        if(blue3<0){blue3+=-65536;};
        short whiteRight = (short)((red3 + green3 + blue3)/3);

        if (whiteRight > whiteLeft && whiteLeft > whiteCenter) {
            
        } else if (whiteLeft > whiteCenter){
            
        }


        return 0.0;
    }

    public I2C tcaselect(uint8_t i) {
        if (i < 8) {
            I2C i2c = new I2C(I2C.Port.kOnboard, 0x70);
            i2c.write(1 << i);
        } else {
            return null;
        }
    }
}
