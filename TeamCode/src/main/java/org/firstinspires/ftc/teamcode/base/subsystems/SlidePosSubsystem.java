package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SlidePosSubsystem extends SubsystemBase {
    DcMotorEx rSlide, lSlide;
    public SlidePosSubsystem(HardwareMap h, String right, String left) {
        rSlide = h.get(DcMotorEx.class, right);
        lSlide = h.get(DcMotorEx.class, left);
        rSlide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        lSlide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setPos(int pos) {
        rSlide.setTargetPosition((int)pos);
        rSlide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rSlide.setPower(1);
        lSlide.setTargetPosition((int)pos);
        lSlide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        lSlide.setPower(1);
    }
}
