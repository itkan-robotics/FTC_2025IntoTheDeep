package org.firstinspires.ftc.teamcode.base.bot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.*;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.base.commands.*;
import org.firstinspires.ftc.teamcode.base.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.base.subsystems.*;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.FConstants;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.LConstants;
import org.firstinspires.ftc.teamcode.utils.MotorConfig;
import org.firstinspires.ftc.teamcode.utils.MotorDirectionConfig;
import org.firstinspires.ftc.teamcode.utils.SimpleLogger;

public class Robot {
    public enum Mode {
        SOLO,
        DUO,
        AUTO
    }

    public GamepadEx base;
    public GamepadEx op;
    static SimpleLogger log;
    public Pose start = new Pose(0, 0, Math.toRadians(0));

    public static Drive drive;
    public static ServoSubsystem outtakeClawRot, outtakeClaw, intakeClawDist, intakeClawRot, outtakeClawTwist, outtakeClawDistRight, outtakeClawDistLeft, shifter;
    public static IntakeSubsystem intake;
    public static IntakeAutoSubsystem intakeAuto;
    public static LimitSwitchSubsystem vLimit, hLimit;
    public static PIDFSlideSubsystem slide;
    public static PIDFSingleSlideSubsystem hSlide;
    public FollowerSubsystem follower;
    public static WaitSubsystem pause;
    public Telemetry telemetry;
    public HardwareMap hardwareMap;

    public Robot(Mode m, Gamepad g1, Gamepad g2, HardwareMap hardwareMap, Telemetry telemetry) {
        Constants.setConstants(FConstants.class, LConstants.class);
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;

        log = new SimpleLogger();
        intake = new IntakeSubsystem(hardwareMap, Const.intake);
        intakeAuto = new IntakeAutoSubsystem(hardwareMap, Const.intake, new ElapsedTime());
        drive = new Drive(hardwareMap, Const.imu, new MotorConfig(Const.fr, Const.fl, Const.br, Const.bl),
                new MotorDirectionConfig(false,true,false,true));
        hSlide = new PIDFSingleSlideSubsystem(hardwareMap, Const.hSlide, -0.02, 0, 0.000002, 0.0);
        //slide = new SlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);
//        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE,
//                .06, 0, 5e-3, 0,
//                .06, 0, 5e-3, 0);
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.08, 0, 1e-40, 0.29, 0.08, 0, 1e-40, 0.29); //Set directions properly (first one is right slide)
        pause = new WaitSubsystem();
        outtakeClaw = new ServoSubsystem(hardwareMap, Const.outtakeClaw);
        intakeClawDist = new ServoSubsystem(hardwareMap, Const.intakeDist);
        intakeClawRot = new ServoSubsystem(hardwareMap, Const.intakeRot);
        outtakeClawDistLeft = new ServoSubsystem(hardwareMap, Const.outtakeDistLeft);
        outtakeClawDistRight = new ServoSubsystem(hardwareMap, Const.outtakeDistRight);
        vLimit = new LimitSwitchSubsystem(hardwareMap, Const.vLimit);
        hLimit = new LimitSwitchSubsystem(hardwareMap, Const.hLimit);
        //shifter = new ServoSubsystem(hardwareMap, Const.gearShifter);
        outtakeClawRot = new ServoSubsystem(hardwareMap, Const.outtakeRot);
        outtakeClawTwist = new ServoSubsystem(hardwareMap, Const.outtakeTwist);

        Init(m, g1, g2);
    }

    public void Init(Mode m, Gamepad g1, Gamepad g2) {
        if (m == Mode.SOLO || m == Mode.DUO) {
            if (m == Mode.SOLO) {
                base = new GamepadEx(g1);
            } else if (m == Mode.DUO) {
                base = new GamepadEx(g1);
                op = new GamepadEx(g2);
            }
            drive.setDefaultCommand(new DriveCommand(drive, base));
        } else {
            follower = new FollowerSubsystem(new Follower(hardwareMap), start, telemetry);
        }

        CommandScheduler.getInstance().run();
    }

    public void Action(GamepadEx g, GamepadKeys.Button b, Command Press, Command Release) {
        if(Release == null) {
            new GamepadButton(g, b).whenPressed(Press);
        } else {
            new GamepadButton(g, b).whenPressed(Press).whenReleased(Release);
        }
    }

    public Command SpecimenGrab() {
        return new ParallelCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new SlideResetCommand(slide, vLimit),
                new ServoCommand(intakeClawRot, .3),
                new SlideResetCommand(hSlide, hLimit),
                new ServoCommand(outtakeClawTwist, Const.untwist),
                new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrab),
                new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrab),
                new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab)
        );
    }

    public Command SpecimenScore() {
        return new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, 0),
                new WaitCommand(pause, 50),
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawRot, Const.rotSpecimenScore),
                        new ServoCommand(outtakeClawTwist, Const.twist),
                        new SetPIDFSlideArmCommand(slide, 315)
                )
        );
    }

    public Command SpecimenScoreReverse() {
        return new ParallelCommandGroup(
                new ServoCommand(outtakeClaw, .25),
                new ServoCommand(outtakeClawDistRight, Const.distSpecimenGrabFinal),
                new ServoCommand(outtakeClawDistLeft, 1-Const.distSpecimenGrabFinal),
                new ServoCommand(outtakeClawRot, 1),
                new ServoCommand(outtakeClawTwist, Const.twist),
                new SetPIDFSlideArmCommand(slide, 200)
        );
    }

    public Command Intake(Boolean accept) {
        if(accept == null) return new IntakeCommand(intake, 0);
        if(accept) return new IntakeCommand(intake, -1);
        return new IntakeCommand(intake, 1);
    }

    public Command IntakeAuto(Boolean accept) {
        if(accept == null) return new IntakeCommand(intake, 0);
        if(accept) return new IntakeAutoCommand(intakeAuto, -1);
        return new IntakeAutoCommand(intakeAuto, 1);
    }

    public Command SubmersibleIntake() {
        return new ParallelCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new SequentialCommandGroup(
                    new ServoCommand(intakeClawRot, 0.4),
                    new SetPIDFSlideArmCommand(hSlide, -700),
                    new ServoCommand(intakeClawRot, 0.12)
                )
        );
    }

    public Command Transfer() {
        return new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new ServoCommand(intakeClawRot, .3),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                new ServoCommand(outtakeClawRot, 0.7),
                new ServoCommand(outtakeClawTwist, 0.924),
                new SlideResetCommand(slide, vLimit),
                new SlideResetCommand(hSlide, hLimit),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.7265),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, 0.36),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, .2),
                new SetPIDFSlideArmCommand(slide, 200)
        );
    }

    public Command HighBasketPos() {
        return new ParallelCommandGroup(
                new ServoCommand(outtakeClawRot, .64),
                new ServoCommand(outtakeClawDistRight, 1-0.378),
                new ServoCommand(outtakeClawDistLeft, 0.378),
                new SetPIDFSlideArmCommand(slide, 2750)
        );
    }

    public Command ClawRelease() {
        return new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release)
        );
    }

    public Command HighBasketScore() {
        return new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new WaitCommand(pause, 300),
                new ParallelCommandGroup(
                        new SlideResetCommand(slide, vLimit),
                        new ServoCommand(outtakeClawDistLeft, 1),
                        new ServoCommand(outtakeClawDistRight, 0),
                        new ServoCommand(outtakeClawRot, 0.7),
                        new ServoCommand(outtakeClawTwist, 0.924)
                )
        );
    }

    public Command Reset() {
        return new ParallelCommandGroup(
                new SlideResetCommand(slide, vLimit),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                new ServoCommand(outtakeClawRot, 0.7),
                new ServoCommand(outtakeClawTwist, 0.924)
        );
    }

    public FollowPathCommand FollowPath(PathChain path, double power) {
        return new FollowPathCommand(
                follower.getFollower(),
                path,
                true,
                power
        );
    }
}