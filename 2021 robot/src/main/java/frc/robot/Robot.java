// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  private Jaguar leftMotor = new Jaguar(0);
  private Jaguar rightMotor = new Jaguar(1);
  private Joystick joy1 = new Joystick(0);
  DifferentialDrive robotDrive;
  
  // For network communication (limelight/RasPI Camera)
  NetworkTableInstance netInstance;
  NetworkTable table, limeTable;
  NetworkTableEntry xLoc, limeX, limeY, limeTargetArea, camMode, lightMode;
  
  //Autonomous Timer
  Timer autoTimer = new Timer();

  @Override
  public void robotInit() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    camMode = limeTable.getEntry("camMode");
    lightMode = limeTable.getEntry("ledMode");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDouble(0);
    robotDrive = new DifferentialDrive(leftMotor, rightMotor);
    joy1 = new Joystick(0);
  }
    
  @Override
  public void robotPeriodic() {
  
  }
  
  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    rightMotor.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    leftMotor.set(left);
    rightMotor.set(right);

    if (joy1.getRawButton(1)) {
      double dx = limeTable.getEntry("tx").getDouble(-1000);

      System.out.println("Limeline Target X Value:" + dx);

      //if no value do nothing
      if(dx == -1000)
      {
        robotDrive.arcadeDrive(0, 0);
      }else if (dx < 1)     //ADJUST THESE VALUES! (TARGET ERROR ALLOWANCE)
      {
        robotDrive.arcadeDrive(0,-0.3); //ADJUST THESE VALUES(TURN RATE)!
      }else if (dx > 1)   //ADJUST THESE VALUES! (TARGET ERROR ALLOWANCE)
      {
        robotDrive.arcadeDrive(0,0.3); //ADJUST THESE VALUES(TURN RATE)!
      }
      
    } else   //otherwise just drive if button 1 is not pressed
      robotDrive.arcadeDrive(joy1.getY(), -joy1.getZ());
      
      robotDrive.arcadeDrive(0,-0.3); //ADJUST THESE VALUES (TURN RATE)
  robotDrive.arcadeDrive(0,0.3); //ADJUST THESE VALUES (TURN RATE)!
  }

  

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}  // The following is a simple cascasding/linear timer-based state machine
  //    add timer checks for each phase of your auto choreography
  //    adjust the timers as needed.
  public void timedAuto()
  {
    if(autoTimer.get() < 1.0) // for the first second go forward
    {
      robotDrive.arcadeDrive(0.5, 0);
    }else if (autoTimer.get()< 2){  //then spin on axis
      robotDrive.arcadeDrive(0, 0.5);
    }else                           // default is we go idle
    {
      robotDrive.arcadeDrive(0, 0);
    }
  }
}  
