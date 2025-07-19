package org.firstinspires.ftc.teamcode.tuning.PID;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.bot.Const;

@Config
@TeleOp(group="PID")
public class EncoderDirection extends OpMode {


    private DcMotorEx motor, motor1;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motor = hardwareMap.get(DcMotorEx.class, Const.rSlide);
        motor1 = hardwareMap.get(DcMotorEx.class, Const.lSlide);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
    @Override
    public void loop(){
        if(gamepad1.left_bumper){
            motor1.setPower(1);
        }
        else if (gamepad1.left_trigger != 0){
            motor1.setPower(-1);
        }
        else {
            motor1.setPower(0);
        }
        if(gamepad1.right_bumper){
            motor.setPower(1);
        }
        else if (gamepad1.right_trigger != 0){
            motor.setPower(-1);
        }
        else {
            motor.setPower(0);
        }
        telemetry.addData("Right Slide Pos", motor.getCurrentPosition());
        telemetry.addData("Left Slide Pos", motor1.getCurrentPosition());
        telemetry.update();
    }



}