// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import static edu.wpi.first.wpilibj.Timer.delay;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.PWMVictorSPX;

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

  // winch motors, pulls the base of the robot up and down
  private PWMVictorSPX leftWinch = new PWMVictorSPX(4);
  private PWMVictorSPX rightWinch = new PWMVictorSPX(3);

  private PWMVictorSPX elevator = new PWMVictorSPX(2);

  private VictorSPX arm = new VictorSPX(4);


  private VictorSPX intake = new VictorSPX(6);

  private VictorSPX conveyer1 = new VictorSPX(3);
  private VictorSPX conveyer2 = new VictorSPX(5);

// Drivechain
  private DifferentialDrive drivechain = new DifferentialDrive(leftMotor, rightMotor);

//Joystick
  private Joystick joy0 = new Joystick(0);
  private Joystick joy1 = new Joystick(1);

  //private double startTime;

  @Override
  public void robotInit() {
    
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    Double shooterSpeed = .4;
    Double conveyerTime = 10.0;
    Double spinUp = 5.0;  //Shooter spin up time
    Double moveRobotTime = 10.0;
    Double moveRobotSpeed = 1.0;
    //startTime = Timer.getFPGATimestamp();
    Timer timer;
    timer = new Timer();
    System.out.println("Start autonomous");
    System.out.println("Reseting Timer");
    timer.reset();
    System.out.println("Timer Starting");
    timer.start();
    System.out.println("current time is: " + timer.get());
    while(timer.get() < .5){
      System.out.println("Timer status");
    }
    System.out.println("Done");
    leftMotor.set(moveRobotSpeed*-1);
    rightMotor.set(moveRobotSpeed);
    Double timerHelper = timer.get() + moveRobotTime;
    while(timer.get() < timerHelper){
      System.out.println("Moving robot");
    }
    //turn wheels off
    leftMotor.set(0);
    rightMotor.set(0);

    //starting shooter
    leftshooter.set(shooterSpeed*-1);
    rightshooter.set(shooterSpeed);
    timer.reset();
    timer.start();
    while(timer.get() < spinUp){
      System.out.println("Waiting for spin up");
    }
    //Conveyer starts going up
    conveyer1.set(ControlMode.PercentOutput, -.5);
    conveyer2.set(ControlMode.PercentOutput, .5);
    timer.reset();
    timer.start();
    while(timer.get() < conveyerTime){
      System.out.println("Waiting for ammo");
    }
    conveyer1.set(ControlMode.PercentOutput, 0);
    conveyer2.set(ControlMode.PercentOutput, 0);
    leftshooter.set(0);
    rightshooter.set(0);
    //Move robot robot foward for more points
    //put move robot code here
    
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
    // joystick controller #0


    boolean conveyorup = joy0.getRawButton(4); //UNKNOWN
    boolean conveyordown = joy0.getRawButton(1); //UNKNOWN 
    boolean winchUp = joy0.getRawButton(7); // TTF
    boolean winchDown = joy0.getRawButton(8); // T3L
    boolean shooterOut = joy0.getRawButton(6); // right trigger
    

    // joystick controller #1
    boolean arm_up = joy1.getRawButton(4); // xbox A
    boolean arm_down = joy1.getRawButton(1); //xbox Y
    boolean elevator_Up = joy1.getRawButton(3); // Xbox X
    boolean elevator_Down = joy1.getRawButton(2); // Xbox B
    boolean intake_in = joy1.getRawButton(5); // left bummper 
    boolean intake_out = joy1.getRawButton(6); // right bummper 
    

    // Xbox A, B, Y, X, tiny two frames (TTF.), tiny three line(T3L), Dpad, Right Bummper, Left Bummper, Right Trigger, Left Trigger, Top Joystick, Bottom Joystick
    // This was the shooter speed these are not things to look at buttons wise. 
    //boolean shooterspeed0 = joy0.getRawButton(1); // xbox A
    //boolean shooterspeed1 = joy0.getRawButton(2); // xbox B
    //boolean shooterspeed2 = joy0.getRawButton(3); // xbox X
    //boolean shooterspeed3 = joy0.getRawButton(4); // xbox Y

    //Here are the different buttons that could be used for Joystick controller 0:
// boolean nothing = joy0.getRawButton(); // Xbox Y
// boolean nothing = joy0.getRawButton(); // Xbox B
// boolean nothing = joy0.getRawButton(); // Xbox A
// boolean nothing = joy0.getRawButton(); // Xbox x
// boolean nothing = joy0.getRawButton(); // Right Bummper 
// boolean nothing = joy0.getRawButton(); // Left Bummper 
// boolean nothing = joy0.getRawButton(); // Right Trigger 
// boolean nothing = joy0.getRawButton(); // Left Trigger 

    // Here are the different buttons that could be used for Joystick controller 1:
// boolean nothing = joy1.getRawButton(); // TTF
// boolean nothing = joy1.getRawButton(); // T3L
// boolean nothing = joy1.getRawButton(); // Right Trigger 
// boolean nothing = joy1.getRawButton(); // Left Trigger 

    // For joy1 be careful of the Y(4) button it is a bit sticky
    //boolean elevator_Up = joy1.getPOV(0); Want to use the D.pad to move the elevator up and down
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
// elevator moving up and down for the joysticks
     if (elevator_Up){
      elevator.set(.3);
     }

     else  if (elevator_Down){
       elevator.set(-.1);
     }
     else {
      elevator.set(0);

     }
     //making the winch turn :)
     if (winchUp) {
       leftWinch.set(.4);
       rightWinch.set(-.4);
     }     

     else if (winchDown) {
       rightWinch.set(.4);
       leftWinch.set(-.4);
     }

     else {
       rightWinch.set(0);
       leftWinch.set(0);
     }
// Shooter button
      if (shooterOut) {
        leftshooter.set(.4);
        rightshooter.set(.4);
      }

      else {
        leftshooter.set(0);
        rightshooter.set(0);
      }
     // boolean would be Up and Down, PWMVictor would be left and right (if, else if, else)
     // this is arcade drive.
// //driving and turn speed cap    
double speed = joy0.getRawAxis(1);
// double turn = joy0.getRawAxis(0);


double turn_raw = joy0.getRawAxis(0);
double turn = Math.pow(turn_raw, 2.0);
if (turn_raw < 0){
  turn = -turn;
}

// //drive train control
   drivechain.arcadeDrive(speed, turn);
        System.out.println("arcade drive speed: "+speed+", turn: "+turn);


    // this is tank drive
   // double left = Math.pow(-joy0.getRawAxis(2), 2.0);
    //double right = Math.pow(-joy0.getRawAxis(1), 2.0);
    //drivechain.tankDrive(left, right);
    //System.out.println("tank drive left: "+left+", right:"+right);
     
    //double left = Math.pow(-joy0.getRawAxis(2), 2.0);
    //double right = Math.pow(-joy0.getRawAxis(1), 2.0);
    //drivechain.tankDrive(left, right);
    //System.out.println("tank drive left: "+left+", right:"+right);
//when intaking balls set the arm to turn toward the ground
    if (joy0.getRawAxis(3) < 0.0){
       arm.set(ControlMode.PercentOutput,-.1);

  }

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
  }
//when the shooter is pressed it powers at 0.50, or 50%; 0.55, or 55%; 0.60, or 60%; 0.65, or 65%
   // double flywheel0 = 0.45; //flywheel speed 45% Between E5 and E6 45%
   // double flywheel1 = 0.48; //flywheel speed 48% Between E6 and E7 48%
   // double flywheel2 = 0.50; //flywheel speed 50% Between E2 and E3 50%
   // double flywheel3 = 0.52; //flywheel speed 52% E9 52%

   // double flywheelstop = 0; //flywheel speed

  //  if (shooterspeed0) {
  //    leftshooter.set(-flywheel0);
  //    rightshooter.set(flywheel0);
  //    System.out.println("Speed: " + flywheel0);
  //  }

   // else if (shooterspeed1) {
  //    leftshooter.set(-flywheel1);
  //    rightshooter.set(flywheel1);
  //    System.out.println("Speed: " + flywheel1);
   // }

  //  else if (shooterspeed2) {
  //    leftshooter.set(-flywheel2);
  //    rightshooter.set(flywheel2);
  //    System.out.println("Speed: " + flywheel2);
   // }

   // else if (shooterspeed3) {
  //    leftshooter.set(-flywheel3);
  //    rightshooter.set(flywheel3);
  //    System.out.println("Speed: " + flywheel3);
   // }
   
   // else {
  //    leftshooter.set(flywheelstop);
  //    rightshooter.set(flywheelstop);
  //  }
 // }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
