package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.subsystems.IExtendSubsystem;



public class IExtendCommand extends CommandBase {

    private final IExtendSubsystem claw;
    private final int rPos;

    public IExtendCommand(IExtendSubsystem claw, int rPos) {
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

