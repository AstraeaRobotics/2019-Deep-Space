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

    protected int getForwardDrive() {
        return oi.getDriverGamepad().getRawAxis(4) - oi.getDriverGamepad().getRawAxis(3);
    }

    @Override
    protected void execute() {
        if (!oi.isAutomated()) {
            robotDrive.arcadeDrive(getForwardDrive(), oi.getDriverGamepad().getRawAxis(2));
            omniMotor.set(oi.getDriverGamepad().getRawAxis(0)*(getForwardDrive()/255)); 
        } else {
            
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}