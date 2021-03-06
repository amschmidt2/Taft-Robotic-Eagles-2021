// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import static edu.wpi.first.wpilibj.Timer.delay;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.PWMSparkMax;
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

  private CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless); //Sets left motor with CAN ID 1 as a brushless
  private CANSparkMax rightMotor = new CANSparkMax(2, MotorType.kBrushless); //Sets right motor with CAN ID 2 as brushless

  private PWMSparkMax leftshooter = new PWMSparkMax(0); //Sets left motor on shooter as output 0 with PWM
  private PWMSparkMax rightshooter = new PWMSparkMax(1); //sets right motor on shooter as output 1 with PWM

  private PWMSparkMax arm = new PWMSparkMax(2); //Sets PWM output on pin 2 
  private PWMSparkMax intake = new PWMSparkMax(3); //Sets PWM output on pin 3

  private DifferentialDrive drivechain = new DifferentialDrive(leftMotor, rightMotor); //Sets Drivechain to control both left and right motors

  private Joystick joy1 = new Joystick(0); //Sets Joystick input from Joystick 0

  //private double startTime;

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

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
    leftshooter.setInverted(true); //Sets the left motor to be inverted
  }

  @Override
  public void teleopPeriodic() {
    boolean shooterspeed = joy1.getRawButton(5); //xbox Right bumper, PS4/PS5 button 6 (Right Bumper)
    boolean arm_up = joy1.getRawButton(2); //xbox X, PS4/PS5 button 1 (Square)
    boolean arm_down = joy1.getRawButton(0); //xbox A, PS4/PS5 button 2 (X)

//set intake to left and right trigger   
   intake.set(joy1.getRawAxis(3)); //intake in, Xbox Right Trigger (Axis 3), PS4/PS5 Axis 4 (Right Trigger)
   intake.set(-joy1.getRawAxis(2)); //intake out, Xbox Left Trigger (Axis 2), PS4/PS5 Axis 3 (Left Trigger)
   
    double flywheel = 0.45; //flywheel speed
    double flywheelstop = 0;
    
    double speed = -joy1.getRawAxis(1) * 0.6; //Sets speed of drivechain to 60% 
    double turn = -joy1.getRawAxis(0) * 0.3; //Sets speed of turing to 30%

    double left = speed + turn; //Sets turing for left motor
    double right = speed - turn; //Sets turing for right motor

    drivechain.tankDrive(left, right);
//When intaking balls set the arm to turn toward the ground
    if (joy1.getRawAxis(3) < 0.0){ //intake in, Xbox Right Trigger (Axis 3), PS4/PS5 Axis 4 (Right Trigger)
        arm.set(-.1);

    }
//Control the arm up and down
    if (arm_up){
        arm.set (.5);
          }
    if (arm_down){
      arm.set(-.5);
//When the shooter is pressed it powers at 0.45
    }
    if (shooterspeed) {
      leftshooter.set(flywheel);
      rightshooter.set(flywheel);
      System.out.println("speed "+flywheel);
    }

    else {
      leftshooter.set(flywheelstop);
      rightshooter.set(flywheelstop);
      arm.set(0);
      intake.set(0);
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
