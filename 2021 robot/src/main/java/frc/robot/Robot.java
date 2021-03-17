// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.text.BreakIterator;

//import static edu.wpi.first.wpilibj.Timer.delay;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


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
  CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax rightMotor = new CANSparkMax(2, MotorType.kBrushless);

  PWMSparkMax leftshooter = new PWMSparkMax(2);
  PWMSparkMax rightshooter = new PWMSparkMax(3);

// Drive Chain
  DifferentialDrive drivechain = new DifferentialDrive(leftMotor, rightMotor);

// Joystick  
  Joystick joy1 = new Joystick(0);

  //private double startTime;.

// Encoders
  private CANEncoder leftEncoder;
  private CANEncoder rightEncoder;

// Unit conversion
  private final double kDriveTick2Feet = 1.0 / 4096 * 6 * Math.PI / 12;



  @Override
  public void robotInit() {
    leftshooter.setInverted(true); //Sets the left motor to be inverted

    
    leftEncoder = leftMotor.getEncoder();
    rightEncoder = rightMotor.getEncoder();

    //was this supposed to be inversion?
    // leftEncoder.setInverted(true);
    
    // leftMotor.setSensorPhase(false);
    // rightMotor.setSensorPhase(true);

    // are you trying to zero things?
    leftEncoder.setPosition(0.0);
    // leftMotor.setSelectedSensorPosition(0, 0, 10);
    // rightMotor.setSelectedSensorPosition(0, 0, 10);


// Deadband
    drivechain.setDeadband(0.05);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Encoder Position", leftEncoder.getPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Encoder Velocity", leftEncoder.getVelocity() * kDriveTick2Feet);

    SmartDashboard.putNumber("Encoder Position", rightEncoder.getPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Encoder Velocity", rightEncoder.getVelocity() * kDriveTick2Feet);
    

    SmartDashboard.putNumber("Left Drive Encoder Value", leftEncoder.getPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Right Drive Encoder Value", rightEncoder.getPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Encoder Value", encoder.get() * kDriveTick2Feet);
  }

  @Override
  public void autonomousInit() {
    enableMotors(true);
    //startTime = Timer.getFPGATimestamp();

// Reset Encoders to zero
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);

    errorSum = 0;
  }

// PID
  final double kP = 0.5;
  final double kI = 5;
  final double kD = 0.01;
  final double iLimit = 1;

  double setpoint = 0;
  double errorSum = 0;
  double lastTimestamp = 0;
  double lastError = 0;

  @Override
  public void autonomousPeriodic() {
// Get Sensor Position
    double sensorPosition = leftEncoder.get() * kDriveTick2Feet;

// Calculations
    double error = setpoint - sensorPosition;
    double dt = Timer.getFPGATimestamp() - lastTimestamp;

    if (Math.abs(error) < iLimit) {
    errorSum += error * dt;
    }

    double errorRate = (error - lastError) / dt;

    double outputSpeed = kP * error + kI * errorSum + kD * errorRate;

    double leftPosition = leftEncoder.getPosition() * kDriveTick2Feet;
    double rightPosition = rightEncoder.getPosition() * kDriveTick2Feet;
    double distance = (leftPosition + rightPosition) / 2;

    if (distance < 5) {
      drive.tankDrive(0.6, 0.6);
    }

    else {
      drive.tankDrive(0, 0);
    }

// Updated last Variables
    lastTimestamp = Timer.getFPGATimestamp();
    lastError = error;


    /*double DELAY_TIME = 1;
    
    double time = Timer.getFPGATimestamp();

    if (time - startTime < 6) {}

  if ((time - startTime > 6) && (time - startTime < 12)) {}

  if ((time - startTime > 12) && (time - startTime < 18 )) {}

  if ((time - startTime > 18 ) && (time - startTime < 24)) {}

  if (time - startTime > 24)  {
    leftMotor.set(0);
    rightMotor.set(0);
    }*/
  }

  @Override
  public void teleopInit() {
    enableMotors(true);
  }

  @Override
  public void teleopPeriodic() {
// Driving
    double power = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = power * 0.6; //Sets turing for left motor
    double right = turn * 0.3; //Sets turing for right motor

    drivechain.arcadeDrive(left, right);

// Shooting Mechantism
    boolean shooterspeed = joy1.getRawButton(2);

    double flywheel = 0.4;
    double flywheelstop = 0;

    if (shooterspeed) {
      leftshooter.set(flywheel);
      rightshooter.set(flywheelstop);
    }

    else {
      leftshooter.set(flywheelstop);
      rightshooter.set(flywheelstop);
    }
    
  }

  @Override
  public void disabledInit() {
    enableMotors(false);
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  private void enableMotors(boolean on) {
    NeutralMode mode;
    if (on) {
      mode = NeutralMode.Break;
    }
    else {
      mode = NeutralMode.Coast;
    }

     leftMotor.setNeutralMode(mode);
     rightMotor.setNeutralMode(mode);

  }

}
