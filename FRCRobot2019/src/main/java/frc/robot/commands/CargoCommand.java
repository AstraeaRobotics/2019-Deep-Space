package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.*;

public class CargoCommand extends Command {
    private Robot robot;
    private OI oi;
    private VictorSPX vict1;
    private VictorSPX vict2;

    public CargoCommand(Robot robot, OI oi) {
        requires(subsystems.CargoSubsystem);
        
        this.robot = robot;
        this.oi = oi;

        vict1 = new VictorSPX(RobotMap.cargoVictor1p);
        vict2 = new VictorSPX(RobotMap.cargoVictor2p);
    }

    protected void vict1Forward() {
        vict1.set(1);
    }

    protected void vict1Backward() {
        vict1.set(-1);
    }

    protected void vict2Forward1() {
        vict2.set(.5);
    }

    protected void vict2Forward2() {
        vict2.set(1);
    }

    protected void vict2Backward() {
        vict2.set(-1);
    }

    protected void execute() {
        if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) {
            vict1Forward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) {
            vict1Backward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) {
            vict2Forward1();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) {
            vict2Forward2();
        } else if(oi.getDriverGamepad().getRawButtonPressed(/*BUTTON #*/)) {
            vict2Backward();
        }
    }
}