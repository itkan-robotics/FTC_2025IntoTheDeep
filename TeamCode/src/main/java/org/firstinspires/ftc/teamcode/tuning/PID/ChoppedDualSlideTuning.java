package org.firstinspires.ftc.teamcode.tuning.PID;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.base.bot.Const;

@Config
@TeleOp(group="PID")
public class ChoppedDualSlideTuning extends OpMode {

    public static double p = 0.0, i = 0.0, d = 0.0, f = 0.0;
    static double target = 2750;

    private PIDController controller;
    private DcMotorEx       motor1, motor2;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor1 = hardwareMap.get(DcMotorEx.class, Const.lSlide);
        motor2 = hardwareMap.get(DcMotorEx.class, Const.rSlide);

        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        controller = new PIDController(p, i, d);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);

        int pos1 = motor1.getCurrentPosition();
        int pos2 = motor2.getCurrentPosition();
        int avg  = (pos1 + pos2) / 2;

        double pid = controller.calculate(avg, target);

        double direction = Math.signum(target - avg);
        double ff        = f * direction;

        double power = pid + ff;
        power = Math.max(-1.0, Math.min(1.0, power));

        motor1.setPower(power);
        motor2.setPower(power);

        telemetry.addData("pos1",    pos1);
        telemetry.addData("pos2",    pos2);
        telemetry.addData("avg",     avg);
        telemetry.addData("target",  target);
        telemetry.addData("P×error", pid);
        telemetry.addData("FF",      ff);
        telemetry.addData("power",   power);
        telemetry.update();
    }
}
