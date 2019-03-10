package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.CargoCommand;
import frc.robot.OI;
import frc.robot.Robot;

public class CargoSubsystem {
    private Robot robot;
    private OI oi;
    private VictorSPX cargoMotor;
    private VictorSPX intakeMotor;
    private TalonSRX armMotor;

    CargoSubsystem(OI oi, Robot robot) {
        this.oi = oi;
        this.robot = robot;
    }

    /*
    @Override
    public void initDefaultCommand() {
        Command command = new CargoCommand(oi, robot);
        command.start();
    }
    */
}