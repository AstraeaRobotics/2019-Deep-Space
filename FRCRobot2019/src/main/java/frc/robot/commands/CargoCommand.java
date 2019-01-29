package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.*;

public class CargoCommand extends Command {
    private Robot robot;
    private OI oi;
    private VictorSPX intake;
    private VictorSPX shooter;

    public CargoCommand(Robot robot, OI oi) {
        requires(subsystems.CargoSubsystem);
        
        this.robot = robot;
        this.oi = oi;

        intake = new VictorSPX(RobotMap.cargoVictor1p); // Intake motor
        shooter = new VictorSPX(RobotMap.cargoVictor2p); // Shooting motor
    }

    protected void intakeForward() {
        intake.set(1);
    }

    protected void intakeReverse() {
        intake.set(-1);
    }

    protected void shooterForward1() {
        shooter.set(.5);
    }

    protected void shooterForward2() {
        shooter.set(1);
    }

    protected void shooterReverse() {
        shooter.set(-1);
    }

    protected void execute() {
        if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) { // Intake full forward
            vict1Forward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) { // Intake full reverse
            vict1Backward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) { // Shooter half forward
            shooterForward1();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) { // Shooter full forward
            shooterForward2();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) { // Shooter full reverse
            shooterBackward();
        }
    }
}