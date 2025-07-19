package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.bot.Const;

@TeleOp(name = "Test")
public class Test extends OpMode {
    public DcMotorEx left, right;
    public int target = 0;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, Const.lSlide);
        right = hardwareMap.get(DcMotorEx.class, Const.rSlide);

        left.setDirection(DcMotorEx.Direction.REVERSE);
        right.setDirection(DcMotorEx.Direction.FORWARD);

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            target = 1000;
        }

        if (gamepad1.x) {
            target = 500;
        }

        if (gamepad1.y) {
            target = 100;
        }

        if(gamepad1.b) {
            target = 0;
        }

        right.setTargetPosition(target);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setPower(1);
        left.setTargetPosition(target);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setPower(1);

        telemetry.addData("Pos", left.getCurrentPosition());
    }
}
