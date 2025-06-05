package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IExtendSubsystem extends SubsystemBase {
    private DcMotorEx Iextend;

    public IExtendSubsystem(HardwareMap hardwaremap) {
        Iextend = hardwaremap.get(DcMotorEx.class, "Iextend");

        Iextend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        Iextend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        Iextend.setTargetPosition(0);
        Iextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Iextend.setTargetPosition(0);
        Iextend.setPower(1);


    }



    public void set(int iePos) {
        Iextend.setTargetPosition(iePos);
        Iextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Iextend.setPower(1);
    }
    public boolean atPos(){
        return Math.abs(Iextend.getCurrentPosition() - Iextend.getTargetPosition()) < 20;
    }
    public int get(){
        return Iextend.getTargetPosition();
    }

    public void resetIntake(){
        Iextend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Iextend.setTargetPosition(0);
        Iextend.setPower(0.0);
        Iextend.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

}