// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Conveyer;

public class moveBall extends CommandBase {
  Conveyer conveyer; 
  /** Creates a new moveBall. */
  public moveBall(Conveyer s) {
      conveyer = s;
      addRequirements(conveyer);
    
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println(Constants.CONVEYER1);
    System.out.println(Constants.CONVEYER2);


  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    conveyer.moveBall(Constants.CONVEYER2);
    conveyer.moveBall(Constants.CONVEYER1);


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    conveyer.stop();

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
