package org.firstinspires.ftc.teamcode.base.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.subsystems.*;

import java.util.function.DoubleSupplier;

/**
 * A command to drive the robot with joystick input
 * (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes.
 */
public class SetPIDFSlideArmCommand extends CommandBase {

    private PIDFSlideSubsystem PIDFSlide;
    private PIDFSlideSubsystemAdv PIDFSlideAdv;
    private PIDFArmSubsystem arm;
    private PIDFSingleSlideSubsystemAdv PIDFSingleSlideAdv;
    private PIDFSingleSlideSubsystem PIDFSingleSlide;
    private final double change;
    private ElapsedTime t;

    private boolean b;

    public SetPIDFSlideArmCommand(PIDFSingleSlideSubsystem PIDFSingleSlide, double change) {
        this.PIDFSingleSlide=PIDFSingleSlide;
        this.change = change;
        addRequirements(PIDFSingleSlide);
    }
    public SetPIDFSlideArmCommand(PIDFSingleSlideSubsystemAdv PIDFSingleSlideAdv, double change) {
        this.PIDFSingleSlideAdv=PIDFSingleSlideAdv;
        this.change = change;
        addRequirements(PIDFSingleSlideAdv);
    }
    public SetPIDFSlideArmCommand(PIDFSlideSubsystem PIDFSlide, double change) {
        this.PIDFSlide=PIDFSlide;
        this.change = change;
        this.PIDFSlide.usePID(true);
        t = new ElapsedTime();
        t.startTime();
        this.b = b;
        addRequirements(PIDFSlide);
    }
    public SetPIDFSlideArmCommand(PIDFSlideSubsystemAdv PIDFSlideAdv, double change) {
        this.PIDFSlideAdv=PIDFSlideAdv;
        this.change = change;
        addRequirements(PIDFSlideAdv);
    }
    public SetPIDFSlideArmCommand(PIDFArmSubsystem arm, double change) {
        this.arm=arm;
        this.change = change;
        addRequirements(arm);
    }

    @Override
    public void execute() {
        if(PIDFSlide!=null){
            //PIDFSlide.set(change);

//            PIDController controller = PIDFSlide.getController();
//            controller.setPID(PIDFSlide.getP(), PIDFSlide.getI(), PIDFSlide.getD());
//            int pos = PIDFSlide.getTick();
//            double pid = controller.calculate(pos, change);
//            double power = pid+PIDFSlide.getF();
//
//            PIDController controller1 = PIDFSlide.getController();
//            controller1.setPID(PIDFSlide.getP(), PIDFSlide.getI(), PIDFSlide.getD());
//            double pid1 = controller.calculate(pos, change);
//            double power1 = pid1+PIDFSlide.getF();
//            PIDFSlide.set(power, power1);
            PIDFSlide.set(change);

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
            return (PIDFSlide.getTick()<change+100&&PIDFSlide.getTick()>change-100);
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
