package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.base.bot.Robot;
import org.firstinspires.ftc.teamcode.base.subsystems.FollowerSubsystem;

import java.util.ArrayList;

@Autonomous(group = ".Tuner")
public class PoseFinder extends OpMode {
    private ArrayList<Pose> poses;
    private boolean previousLeftBumper = false;
    private Robot tanveerBot;

    @Override
    public void init() {
        tanveerBot = new Robot(Robot.Mode.SOLO, gamepad1, null, hardwareMap, telemetry);
        tanveerBot.follower = new FollowerSubsystem(new Follower(hardwareMap), tanveerBot.start, telemetry);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_LEFT,
                tanveerBot.SpecimenGrab(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_RIGHT,
                tanveerBot.SpecimenScoreReverse(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_DOWN,
                tanveerBot.Reset(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.DPAD_UP,
                tanveerBot.SpecimenScore(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.RIGHT_BUMPER,
                tanveerBot.Intake(true),
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
                tanveerBot.ClawRelease(),
                null);
        poses = new ArrayList<>();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();

        if (tanveerBot.follower != null) {
            Follower follower = tanveerBot.follower.getFollower();
            Pose currentPose = follower.getPose();

            if (gamepad1.left_bumper && !previousLeftBumper) {
                poses.add(new Pose(currentPose.getX(), currentPose.getY(), currentPose.getHeading()));
            }
            previousLeftBumper = gamepad1.left_bumper;

            for (Pose p : poses) {
                telemetry.addLine(String.format("Pose: (%.2f, %.2f, %.2f)", p.getX(), p.getY(), Math.toDegrees(p.getHeading())));
            }
        }
        else {
            throw new RuntimeException("Follower is null");
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}