package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.subsystems.ColorSensorSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IntakeSubsystem;

public class SmartIntakeCommand extends CommandBase {

    private final IntakeSubsystem intake;
    private final ColorSensorSubsystem color;
    private final double speed;
    private final String detect;

    public SmartIntakeCommand(IntakeSubsystem intake, ColorSensorSubsystem color, double speed, String detect) {
        this.intake =intake;
        this.color = color;
        this.speed = speed;
        this.detect = detect;
        addRequirements(intake, color);
    }

    @Override
    public void execute() {
        intake.set(speed);
    }

    @Override
    public boolean isFinished(){
        return detect.contains(color.getColor());
    }



}