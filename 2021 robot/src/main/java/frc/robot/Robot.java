// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj.Timer.delay;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.PWMSparkMax;

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

  CANSparkMax leftMotor = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax rightMotor = new CANSparkMax(2, MotorType.kBrushless);

  PWMSparkMax leftshooter = new PWMSparkMax(2);
  PWMSparkMax rightshooter = new PWMSparkMax(3);

  double kP = 1;

  double heading;

  DifferentialDrive drivechain = new DifferentialDrive(leftMotor, rightMotor);

  Gyro gyro = new ADXRS450_Gyro(SPI.Port.kMXP);

  Joystick joy1 = new Joystick(0);

  private double startTime;

  @Override
  public void robotInit() {
    // Places a compass indicator for the gyro heading on the dashboard
    // Explicit down-cast required because Gyro does not extend Sendable
    Shuffleboard.getTab("gyro").add((Sendable) gyro);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
    heading = gyro.getAngle();
  }

  @Override
  public void autonomousPeriodic() {
    double error = heading - gyro.getAngle();

    double DELAY_TIME = 1;
    
    double time = Timer.getFPGATimestamp();

    if (time - startTime < 6) {
    drivechain.tankDrive(-.5 + kP * error, 0 - kP * error);
  }

  delay(DELAY_TIME);

  if ((time - startTime > 6) && (time - startTime < 12)) {
    drivechain.tankDrive(0 + kP * error, .5 - kP * error);
  }

  delay(DELAY_TIME);

  if ((time - startTime > 12) && (time - startTime < 18 )) {
    drivechain.tankDrive(.5 + kP * error, .5 - kP * error);
  }

  delay(DELAY_TIME);

  if ((time - startTime > 18 ) && (time - startTime < 24)) {
    drivechain.tankDrive(-.5 + kP * error, -.5 - kP * error);
  }

  else {
    leftMotor.set(0);
    rightMotor.set(0);
  //or (unknown if it works)
    //drivechain.tankDrive(0 + kP * error, 0 - kP * error);
    }
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    boolean shooterspeed = joy1.getRawButton(2);

    double flywheel = 0.4;
    double flywheelstop = 0;

    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = speed + turn;
    double right = speed - turn;

    drivechain.tankDrive(-left, right);

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
