package org.firstinspires.ftc.teamcode.tuning.PID;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.base.bot.Const;

@Config
@TeleOp(group="PID")
public class DualSlideTuning extends OpMode {

    public static double p = 0, i = 0, d = 0.0;
    public static double f = 0;
    public static double target = 500;

    private PIDFController controller;
    private int pos1, pos2;
    private DcMotorEx motor1, motor2;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor1 = hardwareMap.get(DcMotorEx.class, Const.lSlide);
        motor2 = hardwareMap.get(DcMotorEx.class, Const.rSlide);

        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        controller = new PIDFController(p, i, d, f);
    }

    @Override
    public void loop() {
        controller = new PIDFController(p, i, d, f);
        double pid = controller.calculate(motor1.getCurrentPosition(), target);




        motor1.setPower(pid);
        motor2.setPower(pid);

        telemetry.addData("pos1", pos1);
        telemetry.addData("pos2", pos2);
        telemetry.addData("target", target);
        telemetry.addData("power", pid);
        telemetry.update();
    }
}