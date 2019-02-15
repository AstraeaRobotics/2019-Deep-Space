package frc.robot.commands;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.net.InetAddress;
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

    public DriveCommandTeleop(OI oi, Robot robot) {
        // Use requires() here to declare subsystem dependencies
        requires(robot.driveSubsystem);
        this.robot = robot;
        this.oi = oi;
        robotDrive = oi.getRobotDrive();
        omniMotor = oi.getOmniMotor();
    }

    protected double getForwardDrive() {
        return oi.getDriverGamepad().getRawAxis(4) - oi.getDriverGamepad().getRawAxis(3);
    }

    @Override
    protected void execute() {
            robotDrive.arcadeDrive(getForwardDrive()*Constants.turnSpeed, oi.getDriverGamepad().getRawAxis(2)*Constants.turnSpeed);
            omniMotor.set(oi.getDriverGamepad().getRawAxis(0)*(getForwardDrive()/255)*Constants.driveSpeed);


    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    public void usePi(String ip) {
        NetworkTable table = NetworkTable.getTable("Vision");
	    double angle = table.getNumber("angle", 0);
        if(angle > 0) {
            while(angle > 0) {
                omniMotor.set(.1);
                angle = table.getNumber("angle", 0);
            }
        } else if(angle < 0) {
            while(angle < 0) {
                omniMotor.set(-.1);
                angle = table.getNumber("angle", 0);
            }
        }
    }
}
