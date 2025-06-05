package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.subsystems.ExtendSubsystem;


public class ExtendCommand extends CommandBase {

    private final ExtendSubsystem claw;
    private final int rPos;

    public ExtendCommand(ExtendSubsystem claw, int rPos) {
        this.claw=claw;
        this.rPos = rPos;
        addRequirements(claw);
    }

    @Override
    public void execute() {
        claw.set(rPos);
    }
    @Override
    public boolean isFinished(){
        return claw.atPos();
    }

}

