package frc.robot.commands;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.net.InetAddress;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.Constants;

public class DriveCommandTeleop extends Command { 
    private Robot robot;
    private OI oi;
    private DifferentialDrive robotDrive;
    private CANSparkMax omniMotor;
    private GenericHID driverGamepad;

    public DriveCommandTeleop(OI oi, Robot robot) {
        // Use requires() here to declare subsystem dependencies
        requires(robot.driveSubsystem);
        this.robot = robot;
        this.oi = oi;
        robotDrive = oi.getRobotDrive();
        omniMotor = oi.getOmniMotor();
        driverGamepad = oi.getDriverGamepad();
    }

    protected double getForwardDrive() {
        return oi.getDriverGamepad().getRawAxis(4) - oi.getDriverGamepad().getRawAxis(3);
    }

    @Override
    protected void execute() {
        if(driverGamepad.getRawAxis(2)>Constants.controlStickDeadzone){
            robotDrive.arcadeDrive(driverGamepad.getRawAxis(5)*Constants.driveSpeed, -.5);
            omniMotor.set(driverGamepad.getRawAxis(2)*Constants.driveSpeed);
        }else if(driverGamepad.getRawAxis(2)<-Constants.controlStickDeadzone){
            robotDrive.arcadeDrive(driverGamepad.getRawAxis(5)*-Constants.driveSpeed, .5);
            omniMotor.set(driverGamepad.getRawAxis(2));
        }else{
            robotDrive.arcadeDrive(driverGamepad.getRawAxis(5)*Constants.driveSpeed, driverGamepad.getRawAxis(0)*Constants.turnSpeed);
            omniMotor.set(0);
        }
	    
	usePi("raspifront");
	usePi(/*SECOND PI ADDRESS*/);
	
            
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    public void usePi(String ip) {
        NetworkTable table = NetworkTable.getTable("Vision");
	double angle = table.getNumber("LeftRightValue", 0);
        while(oi.getDriverGamepad().getRawButton(19)) {
	    if(angle > 0) {
                while(angle > 0) {
                    omniMotor.set(.1);
                    angle = table.getNumber("LeftRightValue", 0);
                }
            } else if(angle < 0) {
                while(angle < 0) {
                    omniMotor.set(-.1);
                    angle = table.getNumber("LeftRightValue", 0);
                }
            }
	}
    }
}
