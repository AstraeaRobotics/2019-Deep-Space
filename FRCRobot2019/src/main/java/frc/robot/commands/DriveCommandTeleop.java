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
    private double driveModifier = 255;
    private double spinModifier = 255;

    public DriveCommandTeleop(OI oi, Robot robot) {
        // Use requires() here to declare subsystem dependencies
        requires(robot.driveSubsystem);
        this.robot = robot;
        this.oi = oi;
        robotDrive = oi.getRobotDrive();
        omniMotor = oi.getOmniMotor();
    }

    public void getModifiers() {
        driveModifier = 255 - oi.getDriverGamepad().getRawAxis(3);
        spinModifier = 255 - oi.getDriverGamepad().getRawAxis(4);
    }

    @Override
    protected void execute() {
        getModifiers();
        robotDrive.arcadeDrive(0, oi.getDriverGamepad().getRawAxis(2) * spinModifier);
        robotDrive.tankDrive(oi.getDriverGamepad().getRawAxis(1) * driveModifier, oi.getDriverGamepad().getRawAxis(1) * driveModifier);
        omniMotor.set(oi.getDriverGamepad().getRawAxis(0)*0.8); 
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}