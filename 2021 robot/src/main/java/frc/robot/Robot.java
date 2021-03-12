// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import static edu.wpi.first.wpilibj.Timer.delay;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
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
  private 


  @Override
  public void robotInit() {
    leftshooter.setInverted(true); //Sets the left motor to be inverted

    /*
    leftEncoder = leftMotor.getEncoder();
    rightEncoder = rightMotor.getEncoder();
    
    leftEncoder = leftMotor.getEncoder(EncoderType.kQuadrature, 4096);
    rightEncoder = rightMotor.getEncoder(EncoderType.kQuadrature, 4096);
*/
// Encoder Set up
    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    leftMaster.setSensorPhase(false);
    rightMaster.setSensorPhase(true);

    leftMaster.setSelectedSensorPosition(0, 0, 10);
    rightMaster.setSelectedSensorPosition(0, 0, 10);

// Set Encoder Boundary Limits: to stop motors
    


// Deadband
    drivechain.setDeadband(0.05);
  }

  @Override
  public void robotPeriodic() {
    /*SmartDashboard.putNumber("Encoder Position", leftEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Velocity", leftEncoder.getVelocity());

    SmartDashboard.putNumber("Encoder Position", rightEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Velocity", rightEncoder.getVelocity());
    */

    SmartDashboard.putNumber("Left Drive Encoder Value", leftMaster.getSelectedSensorPosition() * kDriveTick2Feet);
    SmartDashboard.putNumber("Right Drive Encoder Value", RightMaster.getSelectedSensorPosition() * kDriveTick2Feet);
  }

  @Override
  public void autonomousInit() {
    //startTime = Timer.getFPGATimestamp();

// Reset Encoders to zero
    leftMaster.setSelectedSensorPosition(0, 0, 10);
    rightMaster.setSelectedSensorPosition(0, 0, 10);
  }

  @Override
  public void autonomousPeriodic() {
    double leftPosition = leftMaster.getSelectedSensorPosition() * kDriveTick2Feet;
    double rightPosition = rightMaster.getSelectedSensorPosition() * kDriveTick2Feet;
    double distance = (leftPosition + rightPosition) / 2;

    if (distance < 10) {
      drive.tankDrive(0.6, 0.6);
    }

    else {
      drive.tankDrive(0, 0);
    }

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
  public void teleopInit() {}

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
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
