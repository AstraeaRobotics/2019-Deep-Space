package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.*;

public class CargoCommand extends Command {
    private Robot robot;
    private OI oi;
    private VictorSPX intake;
    private VictorSPX shooter;

    public CargoCommand(Robot robot, OI oi, Subsystem sub) {
        requires(sub);
        
        this.robot = robot;
        this.oi = oi;

        intake = new VictorSPX(RobotMap.cargoVictor1p); // Intake motor
        shooter = new VictorSPX(RobotMap.cargoVictor2p); // Shooting motor
    }

    protected void intakeForward() {
        intake.set(ControlMode.PercentOutput, 1);
    }

    protected void intakeReverse() {
        intake.set(ControlMode.PercentOutput, -1);
    }

    protected void shooterForward1() {
        shooter.set(ControlMode.PercentOutput,.5);
    }

    protected void shooterForward2() {
        shooter.set(ControlMode.PercentOutput,1);
    }

    protected void shooterReverse() {
        shooter.set(ControlMode.PercentOutput, -1);
    }

    protected void execute() {
        if(oi.getDriverGamepad().getRawButtonPressed(2)) { // Intake full forward
            intakeForward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(3)) { // Intake full reverse
            intakeReverse();
        } else if(oi.getDriverGamepad().getRawButtonPressed(4)) { // Shooter half forward
            shooterForward1();
        } else if(oi.getDriverGamepad().getRawButtonPressed(5)) { // Shooter full forward
            shooterForward2();
        } else if(oi.getDriverGamepad().getRawButtonPressed(6)) { // Shooter full reverse
            shooterReverse();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}