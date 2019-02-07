package frc.robot.commands;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;

public class Alignment extends Command{
    private Robot robot;
    private OI oi;

    Ultrasonic leftSensor = new Ultrasonic(RobotMap.alignmentLSensorIN,RobotMap.alignmentLSensorOUT);
    Ultrasonic middleSensor = new Ultrasonic(RobotMap.alignmentMSensorIN, RobotMap.alignmentMSensorOUT);
    Ultrasonic rightSensor = new Ultrasonic(RobotMap.alignmentRSensorIN, RobotMap.alignmentRSensorOUT);

    protected double alignmentSpeedScale = .5;
    protected double maxRangeDeviation = 3;
    protected boolean isFinished = false;

    public Alignment(OI oi, Subsystem sub, Robot robot) {
        requires(sub);
        this.oi = oi;
        this.robot=robot;
    }

    protected boolean checkSensors(){
        double sensorAverage = (leftSensor.getRangeInches()+middleSensor.getRangeInches()+rightSensor.getRangeInches())/3;
        boolean isAligned = true;
        if (Math.abs(leftSensor.getRangeInches()-sensorAverage) > maxRangeDeviation){
            isAligned = false;
        }; if (Math.abs(middleSensor.getRangeInches()-sensorAverage) > maxRangeDeviation){
            isAligned = false;
        }; if (Math.abs(rightSensor.getRangeInches()-sensorAverage) > maxRangeDeviation){
            isAligned = false;
        }
        return isAligned;
    }

    protected void initialize(){
    }

    protected void execute() {
        double rangeDifference = Math.abs(leftSensor.getRangeInches() - rightSensor.getRangeInches());
        double alignmentTurnSpeed = rangeDifference * alignmentSpeedScale;
        if (leftSensor.getRangeInches()>rightSensor.getRangeInches()){
            oi.getRobotDrive().arcadeDrive(0, 1 * Constants.turnSpeed * alignmentTurnSpeed);
        } else if (leftSensor.getRangeInches()>rightSensor.getRangeInches()){
            oi.getRobotDrive().arcadeDrive(0, -1 * Constants.turnSpeed * alignmentTurnSpeed);
        }
        if (checkSensors()){
            isFinished = true;
        }
    }

    protected void end() {
    }

    protected void interrupted(){

    }

    protected boolean isFinished(){
        return isFinished;
    }


}