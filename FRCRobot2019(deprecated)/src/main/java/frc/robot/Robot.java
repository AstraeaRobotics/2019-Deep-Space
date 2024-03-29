/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc.robot.subsystems.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

 
enum Mode {
  RAMP, HATCH, CANNON, AUTO_ALIGN;
}

public class Robot extends TimedRobot {
  public static Mode system = Mode.HATCH;
  public static OI m_oi;
  public static DriveSubsystem driveSubsystem;
  public static HatchSubsystem hatchSubsystem;
  public static CannonSubsystem cannonSubsystem;
  public static RampSubsystem rampSubsystem;
  public static I2CDataSubsystem i2cSubsystem;

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    //m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    // chooser.addOption("My Auto", new MyAutoCommand());
    //SmartDashboard.putData("Auto mode", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    driveSubsystem = new DriveSubsystem(m_oi, this);
    driveSubsystem.initDefaultCommand();

    i2cSubsystem = new I2CDataSubsystem(m_oi, this);

    if (system == Mode.HATCH && ActivatedSystems.hatchSub) {
      hatchSubsystem = new HatchSubsystem(m_oi, this);
      hatchSubsystem.initDefaultCommand();
    } else if (system == Mode.CANNON && ActivatedSystems.cannonSub) {
      cannonSubsystem = new CannonSubsystem(m_oi, this);
      cannonSubsystem.initDefaultCommand();
    } else if (system == Mode.RAMP && ActivatedSystems.rampSub) {
      rampSubsystem = new RampSubsystem(m_oi, this);
      rampSubsystem.initDefaultCommand();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
 
}
