package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PIDFSlideSubsystem extends SubsystemBase {
    /**
     * Creates a new ExampleSubsystem.
     */
    private final DcMotorEx right, left;
    private final double p, i, d, f;
    private final double p1, i1, d1, f1;
    private int pos = 0, pos1 = 0;
    private double target = 0;
    boolean use = true;
    private PIDFController controller;
    public PIDFSlideSubsystem(HardwareMap h, String right, String left, Direction rightD, Direction leftD, double p, double i, double d, double f, double p1, double i1, double d1, double f1) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
        this.p1 = p1;
        this.i1 = i1;
        this.d1 = d1;
        this.f1 = f1;
        this.right = h.get(DcMotorEx.class, right);
        this.left = h.get(DcMotorEx.class, left);
        this.right.setDirection(rightD);
        this.left.setDirection(leftD);
        this.right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        controller = new PIDFController(p, i, d, f);
    }

    public void set(double target) {
        this.target = target;
    }
    public void set(double rPow, double lPow){
        right.setPower(rPow); left.setPower(lPow);
    }
    public void change(double amount){this.right.setPower(Math.max(f, amount)); this.left.setPower(Math.max(f1, amount));}
    public void reset(){
        this.right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
    public PIDFController getController(){
        return controller;
    }
    public double getP(){
        return p;
    }
    public double getI(){
        return i;
    }
    public double getD(){
        return d;
    }
    public double getF(){
        return f;
    }
    public void usePID(boolean yes){
        use = yes;
    }
    public int getTick(){return this.left.getCurrentPosition();}

    @Override
    public void periodic() {
            controller.setPIDF(p, i, d, f);
            pos = left.getCurrentPosition();
            double pid = controller.calculate(pos, this.target);

            right.setPower(pid);
            left.setPower(pid);
//        right.setTargetPosition((int)target);
//        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        right.setPower(1);
//        left.setTargetPosition((int)target);
//        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        left.setPower(1);
    }
}