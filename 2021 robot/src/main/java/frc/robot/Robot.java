// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj.Timer.delay;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import com.analog.adis16470.frc.ADIS16470_IMU;
  

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final double kAngleSetpoint = 0.0;
	private static final double kP = 0.005; // propotional turning constant
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  private static final double kVoltsPerDegreePerSecond = 0.0128;

  private static final int kLeftMotorPort = 0;
	private static final int kRightMotorPort = 1;
  private static final int kJoystickPort = 0;
  
  private DifferentialDrive m_myRobot
			= new DifferentialDrive(new Spark(kLeftMotorPort),
			new Spark(kRightMotorPort));
	private AnalogGyro m_gyro = new AnalogGyro(kGyroPort);
	private Joystick m_joystick = new Joystick(kJoystickPort);

 

  private double startTime;
  public static final ADIS16470_IMU imu = new ADIS16470_IMU();

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
    rightMotor.setInverted(true);
  }

  @Override
  public void autonomousPeriodic() {
    double DELAY_TIME = 1;
    
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

  if ((time - startTime > 6) && (time -startTime < 11 )) {
    leftMotor.set(0.6);
    rightMotor.set(0.1);
  }

  /*if (time - startTime < 13) {
    leftMotor.set(0.7);
    rightMotor.set(0.1);
  }

  if (time - startTime <14) {
    leftMotor.set(0.3);
    rightMotor.set(0.6);    
  }*/

    else {
    leftMotor.set(0);
    rightMotor.set(0);
    }
  }

  @Override
  public void teleopInit() {
    rightMotor.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {

    double turningValue = (kAngleSetpoint - imu.getAngle()) * kP;
		// Invert the direction of the turn if we are going backwards
		turningValue = Math.copySign(turningValue, m_joystick.getY());
		m_myRobot.arcadeDrive(m_joystick.getY(), turningValue);
    
    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    leftMotor.set(left);
    rightMotor.set(right);

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
