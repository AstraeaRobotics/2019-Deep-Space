package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.CargoCommand;
import frc.robot.OI;
import frc.robot.Robot;

public class CargoSubsystem {
    private Robot robot;
    private OI oi;
    private VictorSPX cargoVictor1;
    private VictorSPX cargoVictor2;

    CargoSubsystem(OI oi, Robot robot) {
        this.oi = oi;
        this.robot = robot;
    }

    @Override
    public void initDefaultCommand() {
        Command command = new CargoCommand(oi, robot);
        command.start();
    }
}