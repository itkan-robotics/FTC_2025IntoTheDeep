package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveSubsystem extends SubsystemBase {
    public DcMotorEx fl, fr, bl, br;
    public DriveSubsystem(HardwareMap h, String fln, String frn, String bln, String brn, boolean fld, boolean frd, boolean bld, boolean brd){
        fl = h.get(DcMotorEx.class, fln);
        fr = h.get(DcMotorEx.class, frn);
        bl = h.get(DcMotorEx.class, bln);
        br = h.get(DcMotorEx.class, brn);

        if(fld) this.fl.setDirection(DcMotorSimple.Direction.REVERSE);
        if(frd) this.fr.setDirection(DcMotorSimple.Direction.REVERSE);
        if(bld) this.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        if(brd) this.br.setDirection(DcMotorSimple.Direction.REVERSE);

        this.fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void set(double x, double y, double rx){
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);
    }
}