package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.base.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PIDFSlideSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.SingleSlideSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.SlideSubsystem;

public class SetSlideArmCommand extends CommandBase {

    private SlideSubsystem slide;
    private ArmSubsystem arm;
    private SingleSlideSubsystem sSlide;
    private PIDFSlideSubsystem pSlide;

    private final double speed;

    public SetSlideArmCommand(SlideSubsystem slide, double speed) {
        this.slide=slide;
        this.speed = speed;
        addRequirements(slide);
    }
    public SetSlideArmCommand(ArmSubsystem arm, double speed) {
        this.arm=arm;
        this.speed = speed;
        addRequirements(arm);
    }

    public SetSlideArmCommand(SingleSlideSubsystem sSlide, double speed) {
        this.sSlide = sSlide;
        this.speed = speed;
        addRequirements(sSlide);
    }
    public SetSlideArmCommand(PIDFSlideSubsystem pSlide, double speed){
        this.pSlide = pSlide;
        this.speed = speed;
        pSlide.usePID(false);
    }

    @Override
    public void execute() {
        if (slide != null) {
            slide.set(speed);
        }
        else if (sSlide != null){
            sSlide.set(speed);
        }
        else if (pSlide != null){
            pSlide.set(speed, speed);
        }
        else {
            arm.set(speed);
        }

    }
    @Override
    public boolean isFinished(){
        return true;
    }

}
