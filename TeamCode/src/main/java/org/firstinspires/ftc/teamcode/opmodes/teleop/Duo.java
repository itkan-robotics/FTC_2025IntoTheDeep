package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.base.bot.Robot;

@TeleOp(name="Duo", group=".TeleOp")
public class Duo extends CommandOpMode {
    public static Robot tanveerBot;
    @Override
    public void initialize() {
        tanveerBot = new Robot(Robot.Mode.DUO, gamepad1, gamepad2, hardwareMap);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.A,
                tanveerBot.SpecimenGrab(),
                null);

        tanveerBot.Action(tanveerBot.base,
                GamepadKeys.Button.B,
                tanveerBot.SpecimenScore(),
                null);

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.DPAD_DOWN,
                tanveerBot.Reset(),
                null);

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.RIGHT_BUMPER,
                tanveerBot.Intake(true),
                tanveerBot.Intake(null));

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.LEFT_BUMPER,
                tanveerBot.Intake(false),
                tanveerBot.Intake(null));

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.A,
                tanveerBot.SubmersibleIntake(),
                null);

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.B,
                tanveerBot.Transfer(),
                null);

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.Y,
                tanveerBot.HighBasketPos(),
                null);

        tanveerBot.Action(tanveerBot.op,
                GamepadKeys.Button.X,
                tanveerBot.ClawRelease(),
                null);
    }
}
