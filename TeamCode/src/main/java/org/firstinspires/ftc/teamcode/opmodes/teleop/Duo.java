package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.*;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.commands.*;
import org.firstinspires.ftc.teamcode.utils.*;

@TeleOp
public class Duo extends CommandOpMode {
    public static GamepadEx base;
    SimpleLogger log;

    public static Drive drive;
    public static ServoSubsystem outtakeClawRot, outtakeClaw, intakeClawDist, intakeClawRot, outtakeClawTwist, outtakeClawDistRight, outtakeClawDistLeft, shifter;
    public static IntakeSubsystem intake;
    public static LimitSwitchSubsystem vLimit, hLimit;
    public static PIDFSlideSubsystem slide;
    public static PIDFSingleSlideSubsystem hSlide;
    public static WaitSubsystem pause;

    @Override
    public void initialize() {
        base = new GamepadEx(gamepad1);

        log = new SimpleLogger();
        intake = new IntakeSubsystem(hardwareMap, Const.intake);
        drive = new Drive(hardwareMap, Const.imu, new MotorConfig(Const.fr, Const.bl, Const.br, Const.fl),new MotorDirectionConfig(false,true,false,true));
        hSlide = new PIDFSingleSlideSubsystem(hardwareMap, Const.hSlide, -0.02, 0, 0, 0.0);
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.001, 0,  0, 0.2, 0.001, 0, 0, 0.2);
        pause = new WaitSubsystem();
        outtakeClaw = new ServoSubsystem(hardwareMap, Const.outtakeClaw);
        intakeClawDist = new ServoSubsystem(hardwareMap, Const.intakeDist);
        intakeClawRot = new ServoSubsystem(hardwareMap, Const.intakeRot);
        outtakeClawDistLeft = new ServoSubsystem(hardwareMap, Const.outtakeDistLeft);
        outtakeClawDistRight = new ServoSubsystem(hardwareMap, Const.outtakeDistRight);
        vLimit = new LimitSwitchSubsystem(hardwareMap, Const.vLimit);
        hLimit = new LimitSwitchSubsystem(hardwareMap, Const.hLimit);
        shifter = new ServoSubsystem(hardwareMap, Const.gearShifter);
        outtakeClawRot = new ServoSubsystem(hardwareMap, Const.outtakeRot);
        outtakeClawTwist = new ServoSubsystem(hardwareMap, Const.outtakeTwist);

        //Default Commands
        drive.setDefaultCommand(new DriveCommand(drive,base));

        //Intake
        new GamepadButton(base, GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new IntakeCommand(intake, -0.5)).whenReleased(new IntakeCommand(intake, 0));
        new GamepadButton(base, GamepadKeys.Button.LEFT_BUMPER).whenPressed(new IntakeCommand(intake, 1)).whenReleased(new IntakeCommand(intake, 0));

        //HorizontalSlide Extend
        new GamepadButton(base, GamepadKeys.Button.A).whenPressed(new SequentialCommandGroup(
                new ServoCommand(intakeClawRot, 0.5),
                new SetPIDFSlideArmCommand(hSlide, -700),
                new ServoCommand(intakeClawRot, .22)
        ));

        //Transfer
        new GamepadButton(base, GamepadKeys.Button.X).whenPressed(new SequentialCommandGroup(
                new ServoCommand(intakeClawRot, 0.35),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, 1),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.7),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawTwist, 0.924),
                new SlideResetCommand(slide, vLimit),
                //new WaitCommand(pause, 300),

                //new WaitCommand(pause, 300),
                new SlideResetCommand(hSlide, hLimit),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.79),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, 0.46),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, 0.4429),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, 0.05),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, 0.30 ),
                new ServoCommand(intakeClawRot, 0.377),
                new SetPIDFSlideArmCommand(slide, 5000)

        ));

        //Basket Score
        new GamepadButton(base, GamepadKeys.Button.Y).whenPressed(
                new SequentialCommandGroup(
                        new ServoCommand(outtakeClawRot, 0.5),
                        new ServoCommand(outtakeClawDistRight, 1-0.378),
                        new ServoCommand(outtakeClawDistLeft, 0.378),
                        new SetPIDFSlideArmCommand(slide, 40000)
                ));

        //Release in Basket
        new GamepadButton(base, GamepadKeys.Button.B).whenPressed(new ServoCommand(outtakeClaw, Const.release));

        //Specimen Grab Pos

        new GamepadButton(base, GamepadKeys.Button.DPAD_LEFT).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClawTwist, Const.untwist),
                new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrab),
                new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrab),
                new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab),
                new ServoCommand(outtakeClaw, Const.release)
        ));

        //Grab
        new GamepadButton(base, GamepadKeys.Button.DPAD_RIGHT).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 300),
              //  new SetPIDFSlideArmCommand(slide, 5000),
                new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrabFinal),
                new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrabFinal),
                new ServoCommand(outtakeClawRot, Const.rotSpecimenScore),
                new ServoCommand(outtakeClawTwist, Const.twist)
        ));

        //VSlide Down
        new GamepadButton(base, GamepadKeys.Button.DPAD_DOWN).whenPressed(new SlideResetCommand(slide, vLimit));


    }
}
