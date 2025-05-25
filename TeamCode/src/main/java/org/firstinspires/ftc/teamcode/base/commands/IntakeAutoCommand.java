package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.subsystems.IntakeAutoSubsystem;

/**
 * A command to control the intake subsystem at a specified speed.
 */
public class IntakeAutoCommand extends CommandBase {

    private final IntakeAutoSubsystem intake;
    private final double speed;

    public IntakeAutoCommand(IntakeAutoSubsystem intake, double speed) {
        this.intake = intake;
        this.speed = speed;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        intake.set(speed);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
