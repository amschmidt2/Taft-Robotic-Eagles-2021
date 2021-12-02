// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Timer;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

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
  private Jaguar arm = new Jaguar(2);

  private Joystick joy0 = new Joystick(0);

  boolean arm_up = joy0.getRawButton(1);
  boolean arm_down = joy0.getRawButton(2);

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    Timer timer;
    timer = new Timer();
    System.out.println("start autonomous");
    Sytem.out.println("Reseting timer");
    timer.reset();
    System.out.println("Timer starting");
    timer.start();
    System.out.println("current time is: " + timer.get());
    while(timer.get() < .5){
      System.out.println("timer status");
    }
    System.out.println("done");
    leftMotor.set(MoveRobotSpeed*-1);
    rightMotor.set(MoveRobotSpeed);
    Double timerHelper = timer.get() + MoveRobotTime;
    while(timer.get() < timerHelper){
      System.out.println("Moving Robot");
    }
    leftMotor.set(0);
    rightMotor.set(0);
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    rightMotor.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    double speed = -joy0.getRawAxis(1) * 0.6;
    double turn = -joy0.getRawAxis(0) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    leftMotor.set(left);
    rightMotor.set(right);

//arm controls 
    if (arm_up){
      arm.set(.3);
    }
    
    else if (arm_down){
      arm.set(-.3);
    }
    
    else {
      arm.set(0);
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

}


//Create a if, else if, else statement in order to program the arm for the mini robot.
//You are able to change code between the big robot and the mini robot in order to program right
