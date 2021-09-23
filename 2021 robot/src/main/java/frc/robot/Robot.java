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
  private Joystick joy2 = new Joystick(1);

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
    
    double time = Timer.getFPGATimestamp();*/
  }

  @Override
  public void teleopInit() {
    leftshooter.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    boolean shooterspeed0 = joy2.getRawButton(1); // xbox A
    boolean shooterspeed1 = joy2.getRawButton(2); // xbox B
    boolean shooterspeed2 = joy2.getRawButton(3); // xbox X
    boolean shooterspeed3 = joy2.getRawButton(4); // xbox Y

    boolean arm_up = joy1.getRawButton(3); // xbox A
    boolean arm_down = joy1.getRawButton(2); //xbox X
    boolean conveyorup = joy1.getRawButton(4); //UNKNOWN
    boolean conveyordown = joy1.getRawButton(1); //UNKNOWN
    boolean intake_in = joy1.getRawButton(5);
    boolean intake_out = joy1.getRawButton(8);


// Runs the Conveyor
    if (conveyorup) {
      conveyer1.set(ControlMode.PercentOutput, -.5);
      conveyer2.set(ControlMode.PercentOutput, .5);
    }

     else if (conveyordown) {
      conveyer1.set(ControlMode.PercentOutput, .5);
      conveyer2.set(ControlMode.PercentOutput, -.5);
    }

    else {
      conveyer1.set(ControlMode.PercentOutput, 0);
      conveyer2.set(ControlMode.PercentOutput, 0);
    }

//set intake to left and right trigger   
     if (intake_in){
      intake.set(ControlMode.PercentOutput, .5);
      arm.set(ControlMode.PercentOutput, -.5);
     }  

     else if (intake_out){
      intake.set(ControlMode.PercentOutput, -.5);
      arm.set(ControlMode.PercentOutput, .5);
     }

     else {
      intake.set(ControlMode.PercentOutput, 0);
      arm.set(ControlMode.PercentOutput, 0);
     }

//driving and turn speed cap    
    double left = -joy1.getRawAxis(1) * -joy1.getRawAxis(1);
    double right = -joy1.getRawAxis(0) * -joy1.getRawAxis(0);

    // double left = speed + turn;
    // double right = speed - turn;

//drive train control
    drivechain.tankDrive(left, right);
    System.out.println("tank drive left: "+left+", right: "+right);


//when intaking balls set the arm to turn toward the ground
 //   if (joy1.getRawAxis(3) < 0.0){
   //     arm.set(ControlMode.PercentOutput,-.1);

 //   }

//control the arm up and down
    if (arm_up){
      arm.set(ControlMode.PercentOutput, .5);
    }

    else if (arm_down){
      arm.set(ControlMode.PercentOutput, -.5);
    }
    
    else {
      arm.set(ControlMode.PercentOutput, 0);
    }

//when the shooter is pressed it powers at 0.50, or 50%; 0.55, or 55%; 0.60, or 60%; 0.65, or 65%
    double flywheel0 = 0.45; //flywheel speed 45% Between E5 and E6 45%
    double flywheel1 = 0.48; //flywheel speed 48% Between E6 and E7 48%
    double flywheel2 = 0.50; //flywheel speed 50% Between E2 and E3 50%
    double flywheel3 = 0.52; //flywheel speed 52% E9 52%

    double flywheelstop = 0; //flywheel speed

    if (shooterspeed0) {
      leftshooter.set(flywheel0);
      rightshooter.set(flywheel0);
      System.out.println("Speed: " + flywheel0);
    }

    else if (shooterspeed1) {
      leftshooter.set(flywheel1);
      rightshooter.set(flywheel1);
      System.out.println("Speed: " + flywheel1);
    }

    else if (shooterspeed2) {
      leftshooter.set(flywheel2);
      rightshooter.set(flywheel2);
      System.out.println("Speed: " + flywheel2);
    }

    else if (shooterspeed3) {
      leftshooter.set(flywheel3);
      rightshooter.set(flywheel3);
      System.out.println("Speed: " + flywheel3);
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
