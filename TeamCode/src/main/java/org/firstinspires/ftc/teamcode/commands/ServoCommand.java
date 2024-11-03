package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.*;


import java.util.function.DoubleSupplier;

/**
 * A command to drive the robot with joystick input
 * (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class ServoCommand extends CommandBase {

    private final ServoSubsystem arm;
    private final double pos;

    public ServoCommand(ServoSubsystem arm, double pos) {
        this.arm=arm;
        this.pos=pos;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        arm.set(pos);
    }
    @Override
    public boolean isFinished(){
        return true;
    }

}
