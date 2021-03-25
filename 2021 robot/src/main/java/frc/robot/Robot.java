// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import static edu.wpi.first.wpilibj.Timer.delay;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.PWMSparkMax;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

// Motor Controllers   
  private CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax rightMotor = new CANSparkMax(2, MotorType.kBrushless);

  private PWMSparkMax leftshooter = new PWMSparkMax(0);
  private PWMSparkMax rightshooter = new PWMSparkMax(1);

  private VictorSPX arm = new VictorSPX(4);


  private VictorSPX intake = new VictorSPX(6);

  private VictorSPX conveyer1 = new VictorSPX(3);
  private VictorSPX conveyer2 = new VictorSPX(5);

// Drivechain
  private DifferentialDrive drivechain = new DifferentialDrive(leftMotor, rightMotor);

//Joystick
  private Joystick joy1 = new Joystick(0);

  //private double startTime;

  @Override
  public void robotInit() {
    
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    //startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void autonomousPeriodic() {
    /*double DELAY_TIME = 1;
    
    double time = Timer.getFPGATimestamp();

    if (time - startTime < 3) {
    leftMotor.set(0);
    rightMotor.set(0.6);
  }

  delay(DELAY_TIME);

  if ((time - startTime >3) && (time - startTime < 6)) {
    leftMotor.set(0.6);
    rightMotor.set(0);
  }

  delay(DELAY_TIME);

  if ((time - startTime > 6) && (time -startTime < 11 )) {
    leftMotor.set(0.6);
    rightMotor.set(0);
  }

  /*if (time - startTime < 13) {
    leftMotor.set(0.7);
    rightMotor.set(0.1);
  }

  if (time - startTime <14) {
    leftMotor.set(0.3);
    rightMotor.set(0.6);    
  }

    else {
    leftMotor.set(0);
    rightMotor.set(0);
    }*/
  }

  @Override
  public void teleopInit() {
    leftshooter.setInverted(true);
    
  }

  @Override
  public void teleopPeriodic() {
    boolean shooterspeed0 = joy1.getRawButton(5); // xbox right bumper
    boolean arm_up = joy1.getRawButton(6); // xbox A
    boolean arm_down = joy1.getRawButton(2); //xbox X
    boolean conveyorup = joy1.getRawButton(4); //UNKNOWN
    boolean conveyordown = joy1.getRawButton(7); //UNKNOWN

// Runs the Conveyor
    if (conveyorup) {
      conveyer1.set(ControlMode.PercentOutput,-.5);
      conveyer2.set(ControlMode.PercentOutput,.5);
    }

    else {
      conveyer1.set(ControlMode.PercentOutput,0);
      conveyer2.set(ControlMode.PercentOutput,0);
    }

     if (conveyordown) {
      conveyer1.set(ControlMode.PercentOutput,.5);
      conveyer2.set(ControlMode.PercentOutput,-.5);
    }

    else {
      conveyer1.set(ControlMode.PercentOutput,0);
      conveyer2.set(ControlMode.PercentOutput,0);

    }

//set intake to left and right trigger   

      intake.set(ControlMode.PercentOutput,.5);

    double flywheel = 0.45; //flywheel speed
    double flywheelstop = 0;

//driving and turn speed cap    
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

//drive train control
    drivechain.tankDrive(left, right);

//when intaking balls set the arm to turn toward the ground
 //   if (joy1.getRawAxis(3) < 0.0){
   //     arm.set(ControlMode.PercentOutput,-.1);

 //   }

//control the arm up and down
    if (arm_up){
      arm.set(ControlMode.PercentOutput,.5);
    }
    else {
      arm.set(ControlMode.PercentOutput,0);
    }

    if (arm_down){
      arm.set(ControlMode.PercentOutput,-.5);
    }
    else {
      arm.set(ControlMode.PercentOutput,0);
    }

//when the shooter is pressed it powers at 0.45, or 45%
    if (shooterspeed0) {
      leftshooter.set(flywheel);
      rightshooter.set(flywheel);
      System.out.println("speed"+flywheel);
    }
   
    else {
      leftshooter.set(flywheelstop);
      rightshooter.set(flywheelstop);
      arm.set(ControlMode.PercentOutput,0);
      intake.set(ControlMode.PercentOutput,0);
      conveyer1.set(ControlMode.PercentOutput,0);
      conveyer1.set(ControlMode.PercentOutput,0);
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
