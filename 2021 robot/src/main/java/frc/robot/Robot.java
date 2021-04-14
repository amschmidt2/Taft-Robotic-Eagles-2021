// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

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


  // For network communication (limelight/RasPI Camera)

  
  // For network communication (limelight/RasPI Camera)
  NetworkTable limeTable;
  NetworkTableEntry camMode, lightMode;
  
  

  //Autonomous Timer
  Timer autoTimer = new Timer();

  @Override
  public void robotInit() {


    limeTable = NetworkTableInstance.getDefault().getTable("limelight");
    camMode = limeTable.getEntry("camMode");
    lightMode = limeTable.getEntry("ledMode");

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

    lightMode.setNumber(3); // turn on LEDs
    camMode.setNumber(0);   //set limelight to vision mode
  }
  

  @Override
  public void teleopPeriodic() {

   
  // IF we are pressing the trigger (button 1) we test vision
  if (joy1.getRawButton(1)) {
    double dx = limeTable.getEntry("tx").getDouble(0);
    double ta = limeTable.getEntry("ta").getDouble(0);

      lightMode.setNumber(3); // turn on LEDs

    System.out.println("Limeline Target X-Value:" + dx);

    //if no value do nothing
    
    float KpDistance = -0.1f;

    std::shared_ptr<limeTable> table = limeTable::GetTable("limelight");
    float distance_error = table->GetNumber("ty");
    
    if (joystick->GetRawButton(9))
    {
            driving_adjust = KpDistance * distance_error;
    
            left_command += distance_adjust;
            right_command += distance_adjust;
    }

    if(dx == 0)
      {
        System.out.println("No target found on Limelight.");
        leftMotor.set(0.1);
        rightMotor.set(-0.1);

    }else if (dx < -5)     //ADJUST THESE VALUES! (TARGET ERROR ALLOWANCE)
    {
      System.out.println("Turning LEFT");
      leftMotor.set(-0.3);
      rightMotor.set(0.3);
    }else if (dx > 5)   //ADJUST THESE VALUES! (TARGET ERROR ALLOWANCE)
    {
      System.out.println("Turning RIGHT");
      leftMotor.set(0.3);
      rightMotor.set(-0.3); 
    }else
      {
        System.out.println("ON TARGET!" );
        leftMotor.set((20- ta)*0.26);
        rightMotor.set((20- ta)*0.26);
        System.out.println(ta);
    
  } }
  else
  {
    
      lightMode.setNumber(1); // turn OFF LEDs

    double speed = -joy1.getRawAxis(1) * 0.6;
    double turn = -joy1.getRawAxis(0) * 0.3;

    double left = speed - turn;
    double right = speed + turn;

    leftMotor.set(left);
    rightMotor.set(right);

    }
  }
}
