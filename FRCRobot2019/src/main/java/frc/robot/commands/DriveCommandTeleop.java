package frc.robot.commands;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.OI;

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

    @Override
    protected void execute() {
        robotDrive.arcadeDrive(0, oi.getDriverGamepad().getRawAxis(0));
        robotDrive.tankDrive(oi.getDriverGamepad().getRawAxis(5),oi.getDriverGamepad().getRawAxis(5));
        omniMotor.set(oi.getDriverGamepad().getRawAxis(2)*0.8); 
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}