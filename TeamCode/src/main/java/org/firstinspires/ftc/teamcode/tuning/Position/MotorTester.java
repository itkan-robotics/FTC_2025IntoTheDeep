package org.firstinspires.ftc.teamcode.tuning.Position;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.base.bot.Const;
import org.firstinspires.ftc.teamcode.base.commands.SlideArmCommand;
import org.firstinspires.ftc.teamcode.base.subsystems.PIDFSlideSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.SlideSubsystem;

@TeleOp(name="MotorTester", group=".")
public class MotorTester extends CommandOpMode {
    public SlideSubsystem slide;
    @Override
    public void initialize() {
        slide = new SlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD);
        slide.setDefaultCommand(new SlideArmCommand(slide, new GamepadEx(gamepad1)));

    }
}
