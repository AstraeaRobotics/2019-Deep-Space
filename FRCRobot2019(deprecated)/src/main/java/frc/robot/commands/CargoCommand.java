package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.*;

public class CargoCommand extends Command {
    private Robot robot;
    private OI oi;
    private VictorSPX cargoMotor;
    private VictorSPX intakeMotor;
    private TalonSRX armMotor;

    public CargoCommand(Robot robot, OI oi, Subsystem sub) {
        requires(sub);
        
        this.robot = robot;
        this.oi = oi;

        cargoMotor = new VictorSPX(RobotMap.cargoMainMotor); // Shooting motor
        intakeMotor = new VictorSPX(RobotMap.cargoIntakeMotor); // Intake motor
        armMotor = new TalonSRX(RobotMap.cargoArmMotor); // Arm motor
    }

    /*protected void lowerArm(){ // Must implement code to limit range of motion for intake arm
        armMotor.set(ControlMode.PercentOutput, -1 * Constants.cargoArmMotorSpeed);
    }

    protected void raiseArm(){
        armMotor.set(ControlMode.PercentOutput, 1 * Constants.cargoArmMotorSpeed);
    }

    protected void stopArm(){
        armMotor.set(ControlMode.PercentOutput, 0);
    }

    protected void intakeForward(){
        intakeMotor.set(ControlMode.PercentOutput, 1 * Constants.cargoIntakeMotorSpeed);
    }

    protected void intakeReverse(){
        intakeMotor.set(ControlMode.PercentOutput, -1 * Constants.cargoIntakeMotorSpeed);
    }
    
    protected void intakeStop(){
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }

    protected void cargoForward(){
        cargoMotor.set(ControlMode.PercentOutput, 1 * Constants.cargoMotorSpeed);
    }

    protected void cargoReverse(){
        cargoMotor.set(ControlMode.PercentOutput, -1 * Constants.cargoMotorSpeed);
    }

    protected void cargoStop(){
        cargoMotor.set(ControlMode.PercentOutput, 0);
    }*/

    protected void execute() {
        if(oi.getOperatorGamepad().getRawButtonPressed(2)){
            intakeMotor.set(ControlMode.PercentOutput, 1*Constants.cargoIntakeMotorSpeed);
            cargoMotor.set(ControlMode.PercentOutput, -0.5*Constants.cargoMotorSpeed);
        }else if(oi.getOperatorGamepad().getRawButtonPressed(3)){
            intakeMotor.set(ControlMode.PercentOutput, -1*Constants.cargoIntakeMotorSpeed);
        //}else if(oi.getDriverGamepad().getRawButtonPressed(5)){
            //cargoMotor.set(ControlMode.PercentOutput, -0.5*Constants.cargoIntakeMotorSpeed);
        }else if(oi.getOperatorGamepad().getRawButtonPressed(8)){
            cargoMotor.set(ControlMode.PercentOutput, 1*Constants.cargoMotorSpeed);
        //}else if(oi.getDriverGamepad().getRawButtonPressed(6)){
            //cargoMotor.set(ControlMode.PercentOutput, 0.25*Constants.cargoMotorSpeed);
        }else if(oi.getOperatorGamepad().getRawButtonPressed(7)){
            cargoMotor.set(ControlMode.PercentOutput, -1*Constants.cargoMotorSpeed);
        }else if(oi.getOperatorGamepad().getRawButtonReleased(2)||oi.getOperatorGamepad().getRawButtonReleased(3)||oi.getOperatorGamepad().getRawButtonReleased(5)||oi.getOperatorGamepad().getRawButtonReleased(6)||oi.getOperatorGamepad().getRawButtonReleased(7)||oi.getOperatorGamepad().getRawButtonReleased(8)){
            cargoMotor.set(ControlMode.PercentOutput, 0);
            intakeMotor.set(ControlMode.PercentOutput, 0);
        }
        /*if(oi.getDriverGamepad().getRawButtonPressed(2)) { // Intake full forward
            intakeForward();
        } else if(oi.getDriverGamepad().getRawButtonPressed(3)) { // Intake full reverse
            intakeReverse();
        } else if(oi.getDriverGamepad().getRawButtonPressed(4)) { // Shooter half forward
            shooterForward1();
        } else if(oi.getDriverGamepad().getRawButtonPressed(5)) { // Shooter full forward
            shooterForward2();
        } else if(oi.getDriverGamepad().getRawButtonPressed(6)) { // Shooter full reverse
            shooterReverse();
        }*/
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}