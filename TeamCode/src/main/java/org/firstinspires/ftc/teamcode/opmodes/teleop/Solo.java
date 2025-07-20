package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.bot.Robot;

@TeleOp(name="Solo", group=".")
public class Solo extends CommandOpMode {
    public static Robot tanveerBot;

    @Override
    public void initialize() {
        tanveerBot = new Robot(Robot.Mode.SOLO, gamepad1, null, hardwareMap, telemetry);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_LEFT,
                tanveerBot.SpecimenGrab(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_RIGHT,
                tanveerBot.SpecimenScore(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_DOWN,
                tanveerBot.Reset(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.RIGHT_BUMPER,
                tanveerBot.Intake(true),
                tanveerBot.Intake(null));

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.LEFT_BUMPER,
                tanveerBot.Intake(false),
                tanveerBot.Intake(null));

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.A,
                tanveerBot.SubmersibleIntake(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.B,
                tanveerBot.Transfer(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.Y,
                tanveerBot.HighBasketPos(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.X,
                tanveerBot.HighBasketScore(),
                null);
    }

//    @Override
//    public void run() {
//        DcMotor leftSlide = hardwareMap.get(DcMotor.class, "lSlide");
//        DcMotor rightSlide = hardwareMap.get(DcMotor.class, "rSlide");
//        CommandScheduler.getInstance().run();
//
//        while(opModeIsActive() && !isStopRequested())
//        {
//            telemetry.addData("Pos", leftSlide.getCurrentPosition());
//            telemetry.addData("Pos", rightSlide.getCurrentPosition());
//            telemetry.update();
//        }
//    }
}
