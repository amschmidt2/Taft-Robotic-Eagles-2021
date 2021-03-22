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


  @Override
  public void robotInit() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDouble(0);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(<value>);
    


//SEEKING WITH LIMELIGHT
std::shared_ptr<NetworkTable> table = NetworkTable::GetTable("limelight");
float tv = table->GetNumber("tv");
float tx = table->GetNumber("tx");

float steering_adjust = 0.0f;
if (tv == 0.0f)
{
        // We don't see the target, seek for the target by spinning in place at a safe speed.
        steering_adjust = 0.3f;
}
else
{
        // We do see the target, execute aiming code
        float heading_error = tx;
        steering_adjust = Kp * tx;
}

left_command+=steering_adjust;
right_command-=steering_adjust;
//SEEKIMG WITH LIMELIGHT


//read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double value = tv.getDouble(0.0);

//post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
    SmartDashboard.putNumber("LimelightValue", value);
  }
  
  
  @Override
  public void robotPeriodic() {}

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
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}