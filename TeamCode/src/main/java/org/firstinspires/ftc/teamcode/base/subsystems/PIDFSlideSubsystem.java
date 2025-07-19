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
    private int pos = 0, pos1 = 0;
    private double target = 0;
    boolean use = true;
    PIDFController pidf;
    public PIDFSlideSubsystem(HardwareMap h, String right, String left, Direction rightD, Direction leftD, double p, double i, double d, double f, double p1, double i1, double d1, double f1) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
        this.right = h.get(DcMotorEx.class, right);
        this.left = h.get(DcMotorEx.class, left);
        this.right.setDirection(rightD);
        this.left.setDirection(leftD);
        this.right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        pidf = new PIDFController(p, i, d, f);
    }

    public void set(double target) {
        this.target = target;
    }
    public void set(double rPow, double lPow){
        right.setPower(rPow); left.setPower(lPow);
    }
    public void change(double amount){this.right.setPower(Math.max(f, amount)); this.left.setPower(Math.max(f, amount));}
    public void reset(){
        this.right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        this.left.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
    public PIDFController getController(){
        return pidf;
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
        if (use) {
            pos = left.getCurrentPosition(); //Change from left to whatever motor has a positive encoder when lifted
            double pid = pidf.calculate(pos, this.target);
            right.setPower(pid);
            left.setPower(pid);
        }
    }
}