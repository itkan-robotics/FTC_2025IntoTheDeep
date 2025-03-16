package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.base.bot.Robot;

@TeleOp(name="Solo", group=".TeleOp")
public class Solo extends CommandOpMode {

    @Override
    public void initialize() {
        Robot tanveerBot = new Robot(Robot.Mode.SOLO, gamepad1, null, hardwareMap);

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
                tanveerBot.ClawRelease(),
                null);
    }
}
