package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.*;


import java.util.function.DoubleSupplier;

/**
 * A command to drive the robot with joystick input
 * (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class SlideResetCommand extends CommandBase {

    private PIDFSlideSubsystem slide;
    private SingleSlideSubsystem sSlide;
    private PIDFSingleSlideSubsystem pslide;
    private LimitSwitchSubsystem l;

    private boolean finish;

    private boolean didSetPowerToMotor;

    public SlideResetCommand(PIDFSlideSubsystem slide, LimitSwitchSubsystem l) {
        this.slide=slide;
        this.l = l;
        this.slide.usePID(false);
        addRequirements(slide);
    }
    public SlideResetCommand(PIDFSingleSlideSubsystem slide, LimitSwitchSubsystem l) {
        this.pslide=slide;
        this.l = l;
        this.pslide.usePID(false);
        addRequirements(slide);
    }

    public SlideResetCommand(SingleSlideSubsystem sSlide, LimitSwitchSubsystem l) {
        this.sSlide = sSlide;
        this.l = l;
        addRequirements(sSlide);
    }
    @Override
    public void execute() {
        finish = l.get();
        if (finish == false && didSetPowerToMotor == false) {
            if (slide != null) {
                slide.usePID(false);
                slide.set(-1, -1);
            } else if (pslide != null) {
                pslide.usePID(false);
                pslide.set(1, 0);
            } else if (sSlide != null) {
                sSlide.set(-1);
            }
            didSetPowerToMotor = true;
        }
    }
    @Override
    public boolean isFinished(){
        finish = l.get();
        if (finish && pslide != null) {
            pslide.reset();
            pslide.set(0, 0);
        } else if (finish && slide != null) {
            slide.reset();
            slide.set(0, 0);
        } else if (finish && sSlide != null) {
            sSlide.set(0);
        }
        return finish;
    }
    @Override
    public void end(boolean interrupted){
        if (slide != null) {
            slide.set(0, 0);
            slide.reset();
        }
        else if (pslide != null) {
            pslide.set(0, 0);
            pslide.reset();
            pslide.set(0);
            //pslide.usePID(true);
        }
        else if (sSlide != null){
            sSlide.set(0);
        }
    }

}
