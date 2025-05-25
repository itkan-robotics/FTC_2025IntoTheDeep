package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.bot.Const;
import org.firstinspires.ftc.teamcode.base.commands.AutoPIDF;
import org.firstinspires.ftc.teamcode.base.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.base.commands.IntakeAutoCommand;
import org.firstinspires.ftc.teamcode.base.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.base.commands.ServoCommand;
import org.firstinspires.ftc.teamcode.base.commands.SetPIDFSlideArmCommand;
import org.firstinspires.ftc.teamcode.base.commands.SlideResetCommand;
import org.firstinspires.ftc.teamcode.base.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.FConstants;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.LConstants;
import org.firstinspires.ftc.teamcode.base.subsystems.IntakeAutoSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.LimitSwitchSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PIDFSingleSlideSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PIDFSlideSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ServoSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.WaitSubsystem;

import java.util.ArrayList;

@Autonomous(name="0+0",group = ".Auton")
public class AutoSamp extends OpMode {
    static Pose score = new Pose(-10, 20, Math.toRadians(225));
    static Pose finalScore = new Pose(-16, 16, Math.toRadians(225));
    static double samp1X = -11.2;
    static double samp2X = -20.25;
    public enum AutoPaths {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(90)),
                score
        ),

        GRAB_SAMPLE_1(
                score,
                new Pose(samp1X, 18, Math.toRadians(270))
        ),

        GRAB_SAMPLE_1_FINAL(
                new Pose(samp1X, 18, Math.toRadians(270)),
                new Pose(samp1X, 30, Math.toRadians(270))
        ),

        SCORE_SAMPLE_1(
                new Pose(samp1X, 30, Math.toRadians(270)),
                score
        ),

        GRAB_SAMPLE_2(
                score,
                new Pose(samp2X, 18, Math.toRadians(270))
        ),
        GRAB_SAMPLE_2_FINAL(
                new Pose(samp2X, 18, Math.toRadians(270)),
                new Pose(samp2X, 30, Math.toRadians(270))
        ),
        SCORE_SAMPLE_2(
                new Pose(samp2X, 30, Math.toRadians(270)),
                score
        ),

        GRAB_SAMPLE_3(
                score,
                new Pose(-14, 27, Math.toRadians(315))
        ),
        GRAB_SAMPLE_3_FINAL(
                new Pose(-14, 27, Math.toRadians(315)),
                new Pose(-21, 33, Math.toRadians(315))
        ),

        SCORE_SAMPLE_3(
                new Pose(-21, 33, Math.toRadians(315)),
                score
        ),

        SCORE(
                score,
                finalScore
        ),
        LEAVE(
                finalScore,
                score
        );

        private final Pose[] poses;

        AutoPaths(Pose... poses) {
            this.poses = poses;
        }

        public PathChain line(Follower follower) {
            PathChain path = follower.pathBuilder()
                    .addPath(new BezierLine(new Point(poses[0]), new Point(poses[1])))
                    .setLinearHeadingInterpolation(poses[0].getHeading(), poses[1].getHeading())
                    .build();
            return path;
        }

        public PathChain curve(Follower follower) {
            ArrayList<Point> controlPoints = new ArrayList<>();
            for (Pose pose : poses) {
                controlPoints.add(new Point(pose.getX(), pose.getY(), 1));
            }

            BezierCurve bezierCurve = new BezierCurve(controlPoints);

            PathChain path = follower.pathBuilder()
                    .addPath(bezierCurve)
                    .setLinearHeadingInterpolation(poses[0].getHeading(), poses[poses.length - 1].getHeading())
                    .build();

            return path;
        }

        public Pose[] getPoses() {
            return poses;
        }
    }

    private Follower follower;

    public static ServoSubsystem outtakeClawRot, outtakeClaw, intakeClawDist, intakeClawRot, outtakeClawTwist, outtakeClawDistRight, outtakeClawDistLeft, shifter;
    public static IntakeAutoSubsystem intake;
    public static LimitSwitchSubsystem vLimit, hLimit;
    public static PIDFSlideSubsystem slide;
    public static PIDFSingleSlideSubsystem hSlide;
    public static WaitSubsystem pause;

    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);

        intake = new IntakeAutoSubsystem(hardwareMap, Const.intake, new ElapsedTime());
        hSlide = new PIDFSingleSlideSubsystem(hardwareMap, Const.hSlide, -0.02, 0, 0, 0.0);
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.2, 0, 0.000004, 0.25, 0.2, 0, 0.000004, 0.25);
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

        follower.setPose(AutoPaths.PRELOAD.getPoses()[0]);
        outtakeClaw.set(Const.grab);
        intakeClawRot.set(.58);

        Command scoreCommand = new ParallelCommandGroup(
                new ServoCommand(outtakeClawRot, .64),
                new ServoCommand(outtakeClawDistRight, 1-0.378),
                new ServoCommand(outtakeClawDistLeft, 0.378),
                new AutoPIDF(slide, 1150)
        );
        Command releaseCommand = new SequentialCommandGroup(
                new WaitCommand(pause, 50),
                new ServoCommand(outtakeClaw, Const.release)
        );
        Command resetSlideCommand = new SlideResetCommand(slide, vLimit);
        Command intakeCommand = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClaw, Const.release),
                        new SequentialCommandGroup(
                                new ServoCommand(intakeClawRot, 0.4),
                                new SetPIDFSlideArmCommand(hSlide, -700),
                                new ServoCommand(intakeClawRot, 0.12)
                        )
                ),
                new IntakeAutoCommand(intake, -1)
        );
        Command transferCommand = new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new ServoCommand(intakeClawRot, .3),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                new ServoCommand(outtakeClawRot, 0.7),
                new ServoCommand(outtakeClawTwist, 0.924),
                new SlideResetCommand(slide, vLimit),
                new SlideResetCommand(hSlide, hLimit),
                new IntakeAutoCommand(intake, 0),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.8),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, 0.36),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, .2),
                new SetPIDFSlideArmCommand(slide, 200)
        );

        Command sampAutoPath = new SequentialCommandGroup(
                new FollowPathCommand(follower, AutoPaths.PRELOAD.curve(follower), true),
                scoreCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE.curve(follower), true),
                releaseCommand,
                new FollowPathCommand(follower, AutoPaths.LEAVE.curve(follower), true),
                resetSlideCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_1.curve(follower), true),
                intakeCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_1_FINAL.curve(follower), true, .5),
                transferCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE_SAMPLE_1.curve(follower), true),
                scoreCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE.curve(follower), true),
                releaseCommand,
                new FollowPathCommand(follower, AutoPaths.LEAVE.curve(follower), true),
                resetSlideCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_2.curve(follower), true),
                intakeCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_2_FINAL.curve(follower), true, .5),
                transferCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE_SAMPLE_2.curve(follower), true),
                scoreCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE.curve(follower), true),
                releaseCommand,
                new FollowPathCommand(follower, AutoPaths.LEAVE.curve(follower), true),
                resetSlideCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_3.curve(follower), true),
                intakeCommand,
                new FollowPathCommand(follower, AutoPaths.GRAB_SAMPLE_3_FINAL.curve(follower), true, .5),
                transferCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE_SAMPLE_3.curve(follower), true),
                scoreCommand,
                new FollowPathCommand(follower, AutoPaths.SCORE.curve(follower), true),
                releaseCommand,
                new FollowPathCommand(follower, AutoPaths.LEAVE.curve(follower), true),
                resetSlideCommand
        );



        CommandScheduler.getInstance().schedule(sampAutoPath);
    }


    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();

        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", follower.getPose().getHeading());
        telemetry.addData("Busy", follower.isBusy());
        telemetry.update();
    }
}
