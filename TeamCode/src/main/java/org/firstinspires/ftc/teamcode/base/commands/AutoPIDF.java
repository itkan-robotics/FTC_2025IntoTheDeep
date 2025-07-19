package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.subsystems.*;

import java.util.function.DoubleSupplier;

/**
 * A command to drive the robot with joystick input
 * (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class AutoPIDF extends CommandBase {

    private PIDFSlideSubsystem PIDFSlide;
    private PIDFSlideSubsystemAdv PIDFSlideAdv;
    private PIDFArmSubsystem arm;
    private PIDFSingleSlideSubsystemAdv PIDFSingleSlideAdv;
    private PIDFSingleSlideSubsystem PIDFSingleSlide;
    private final double change;
    private ElapsedTime t;

    private int i = 0;
    private boolean b;

    public AutoPIDF(PIDFSingleSlideSubsystem PIDFSingleSlide, double change) {
        this.PIDFSingleSlide=PIDFSingleSlide;
        this.change = change;
        addRequirements(PIDFSingleSlide);
    }
    public AutoPIDF(PIDFSingleSlideSubsystemAdv PIDFSingleSlideAdv, double change) {
        this.PIDFSingleSlideAdv=PIDFSingleSlideAdv;
        this.change = change;
        addRequirements(PIDFSingleSlideAdv);
    }
    public AutoPIDF(PIDFSlideSubsystem PIDFSlide, double change) {
        this.PIDFSlide=PIDFSlide;
        this.change = change;
        this.PIDFSlide.usePID(true);
        t = new ElapsedTime();
        t.startTime();
        this.b = b;
        addRequirements(PIDFSlide);
    }
    public AutoPIDF(PIDFSlideSubsystemAdv PIDFSlideAdv, double change) {
        this.PIDFSlideAdv=PIDFSlideAdv;
        this.change = change;
        addRequirements(PIDFSlideAdv);
    }
    public AutoPIDF(PIDFArmSubsystem arm, double change) {
        this.arm=arm;
        this.change = change;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        i++;
        if(PIDFSlide!=null){
            //PIDFSlide.set(change);

            PIDFController controller = PIDFSlide.getController();
            controller.setPIDF(PIDFSlide.getP(), PIDFSlide.getI(), PIDFSlide.getD(), PIDFSlide.getF());
            int pos = PIDFSlide.getTick();
            double pid = controller.calculate(pos, change);
            double power = pid+PIDFSlide.getF();

            PIDFController controller1 = PIDFSlide.getController();
            controller1.setPIDF(PIDFSlide.getP(), PIDFSlide.getI(), PIDFSlide.getD(), PIDFSlide.getF());
            double pid1 = controller.calculate(pos, change);
            double power1 = pid1+PIDFSlide.getF();
            PIDFSlide.set(power, power1);
            if(i>= 100){
                PIDFSlide.set(1, 1);
            }

        }
        else if(PIDFSlideAdv != null){
            PIDFSlideAdv.set(change);
        }
        else if(PIDFSingleSlideAdv != null){
            PIDFSingleSlideAdv.set(change);
        }
        else if(PIDFSingleSlide != null){
            PIDFSingleSlide.set(-1*change);
        }
        else{
            arm.set(change);
        }
    }
    @Override
    public boolean isFinished(){

        if(PIDFSlide!=null){
            return (PIDFSlide.getTick()<change+100&&PIDFSlide.getTick()>change-100) || (i >= 120);
        }
        else if(PIDFSlideAdv != null){
            return PIDFSlideAdv.getTick()<change+3&&PIDFSlideAdv.getTick()>change-3;
        }
        else if(PIDFSingleSlideAdv != null){
            return PIDFSingleSlideAdv.getTick()<change+20&&PIDFSingleSlideAdv.getTick()>change-20;
        }
        else if(PIDFSingleSlide != null){
            return PIDFSingleSlide.getTick()<change+75&&PIDFSingleSlide.getTick()>change-75;
        }
        else{
            return arm.getTick()<change+3&&arm.getTick()>change-3;
        }



    }
    @Override
    public void end(boolean inturrupted){
        if (PIDFSlide != null){
            PIDFSlide.set(PIDFSlide.getF(), PIDFSlide.getF());
        }

    }

}
