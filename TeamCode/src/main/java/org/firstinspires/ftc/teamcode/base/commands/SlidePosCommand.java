package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.subsystems.SlidePosSubsystem;

public class SlidePosCommand extends CommandBase {
    public SlidePosSubsystem slide;
    public int pos;

    public SlidePosCommand(SlidePosSubsystem slide, int pos) {
        this.slide = slide;
        this.pos = pos;
        addRequirements(slide);
    }

    @Override
    public void execute() {
        slide.setPos(pos);
    }
}
