package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ExtendSubsystem extends SubsystemBase {
    private DcMotorEx Lextend, Rextend;


    public ExtendSubsystem(HardwareMap hardwaremap) {
        Lextend = hardwaremap.get(DcMotorEx.class, "Lelevator");
        Rextend = hardwaremap.get(DcMotorEx.class, "Relevator");

        Lextend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Rextend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Lextend.setDirection(DcMotorEx.Direction.REVERSE);
        Rextend.setTargetPosition(0);
        Rextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Rextend.setTargetPosition(0);

        Lextend.setTargetPosition(0);
        Lextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Lextend.setTargetPosition(0);
    }
    public void set(int setPoint){
        Lextend.setTargetPosition(setPoint);
        Rextend.setTargetPosition(setPoint);
        Lextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Rextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Lextend.setPower(1);
        Rextend.setPower(1);
    }
    public void setextend(int extendpos) {
        Lextend.setTargetPosition(extendpos);
        Rextend.setTargetPosition(extendpos);
    }
    public int get(){
        return Lextend.getTargetPosition();
    }
    public boolean atPos(){
        return Math.abs(Lextend.getCurrentPosition() - Lextend.getTargetPosition()) < 50;
    }
    public double getextend(){
        return Lextend.getCurrentPosition();
    }
}
