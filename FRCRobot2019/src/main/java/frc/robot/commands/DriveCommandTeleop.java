package frc.robot.commands;

import com.revrobotics.CANSparkMax;

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

    protected int getForwardDrive() {
        return oi.getDriverGamepad().getRawAxis(4) - oi.getDriverGamepad().getRawAxis(3);
    }

    @Override
    protected void execute() {
        if (robot.system == robot.Mode.HATCH || robot.system == robot.Mode.CANNON) {
            robotDrive.arcadeDrive(getForwardDrive()*Constants.turnSpeed, oi.getDriverGamepad().getRawAxis(2)*Constants.turnSpeed);
            omniMotor.set(oi.getDriverGamepad().getRawAxis(0)*(getForwardDrive()/255)*Constants.driveSpeed); 
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}