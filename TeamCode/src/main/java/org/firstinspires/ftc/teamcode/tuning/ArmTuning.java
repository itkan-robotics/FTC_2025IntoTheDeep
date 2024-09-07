package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class ArmTuning extends OpMode {
    private PIDController controller;

    public static double p = 0, i=0, d=0;
    public static double f = 0;

    public static double target = 1;

    private final double ticks_in_degree = 1;

    private DcMotorEx arm_motor;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm_motor = hardwareMap.get(DcMotorEx.class, "armRotater");
        arm_motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        arm_motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void loop(){
        controller.setPID(p, i, d);
        int armPos = arm_motor.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target/ticks_in_degree)) * f;
        double power = pid + ff;
        arm_motor.setPower(power);
        telemetry.addData("pos", armPos);
        telemetry.addData("target", target);
        telemetry.update();
        if(-gamepad1.left_stick_y>0){
            target+=.2;
        }
        else if(-gamepad1.left_stick_y<0){
            target-=.2;
        }
    }



}