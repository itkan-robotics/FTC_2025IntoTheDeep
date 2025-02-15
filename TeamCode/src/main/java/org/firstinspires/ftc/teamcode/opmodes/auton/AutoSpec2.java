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

import org.firstinspires.ftc.teamcode.Const;
import org.firstinspires.ftc.teamcode.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.commands.ServoCommand;
import org.firstinspires.ftc.teamcode.commands.SetPIDFSlideArmCommand;
import org.firstinspires.ftc.teamcode.commands.SlideResetCommand;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeAutoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitchSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PIDFSingleSlideSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PIDFSlideSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ServoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WaitSubsystem;

import java.util.ArrayList;

@Autonomous
public class AutoSpec2 extends OpMode {
    static Pose grab = new Pose(2, -33, Math.toRadians(0));
    public enum AutoPaths {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(180)),
                new Pose(38, 0, Math.toRadians(180))
        ),

        GO_TO_SAMPLES(
                new Pose(38,  0, Math.toRadians(180)),
                new Pose(15, 0, Math.toRadians(180)),
                new Pose(15, -30, Math.toRadians(0)),
                new Pose(45, -30, Math.toRadians(0)),
                new Pose(45, -47, Math.toRadians(0))
        ),

        PUSH_SAMPLE_1(
                new Pose(45, -47, Math.toRadians(0)),
                new Pose(10, -47, Math.toRadians(0))
        ),

        PUSH_SAMPLE_2(
                new Pose(10, -47, Math.toRadians(0)),
                new Pose(45, -47, Math.toRadians(0)),
                new Pose(45, -58, Math.toRadians(0)),
                new Pose(10, -58, Math.toRadians(0))
        ),

        PUSH_SAMPLE_3(
                new Pose(10, -58, Math.toRadians(0)),
                new Pose(45, -58, Math.toRadians(0)),
                new Pose(45, -63.5, Math.toRadians(0)),
                new Pose(10, -63.5, Math.toRadians(0))
        ),

        GRAB_SPECIMEN_1(
                new Pose(10, -63.5, Math.toRadians(0)),
                new Pose(20, -32, Math.toRadians(0)),
                new Pose(grab.getX(), grab.getY()-5.5, grab.getHeading())
        ),

        SCORE_SPECIMEN_1(
                new Pose(grab.getX(), grab.getY()-5.5, grab.getHeading()),
                new Pose(34, 4, Math.toRadians(0))
        ),

        GRAB_SPECIMEN_2(
                new Pose(34, 4, Math.toRadians(0)),
                new Pose(20, -33, Math.toRadians(0)),
                grab
        ),

        SCORE_SPECIMEN_2(
                grab,
                new Pose(34, 3, Math.toRadians(0))
        ),

        GRAB_SPECIMEN_3(
                new Pose(34, 3, Math.toRadians(0)),
                new Pose(20, -33, Math.toRadians(0)),
                grab
        ),

        SCORE_SPECIMEN_3(
                grab,
                new Pose(34, 2, Math.toRadians(0))
        ),

        GRAB_SPECIMEN_4(
                new Pose(34, 2, Math.toRadians(0)),
                new Pose(20, -33, Math.toRadians(0)),
                grab
        ),

        SCORE_SPECIMEN_4(
                grab,
                new Pose(34, 0, Math.toRadians(0))
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
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.001, 0, 0, 0.05, 0.001, 0, 0, 0.05);
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
        slide.reset();
        int[] slidePos = {
                7750,
                16000,
                10000,
                9000,
                9000
        };
        Command scorePreload =
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new SlideResetCommand(hSlide, hLimit),
                                new ServoCommand(outtakeClawDistRight, .8),
                                new ServoCommand(outtakeClawDistLeft, .2),
                                new ServoCommand(outtakeClawRot, 1),
                                new ServoCommand(outtakeClawTwist, Const.twist),
                                new SetPIDFSlideArmCommand(slide, slidePos[0]),
                                new FollowPathCommand(follower, AutoPaths.PRELOAD.line(follower), true)
                        ),
                        new ServoCommand(outtakeClaw, Const.release)
                );

        Command sampsToHp =
                new SequentialCommandGroup(
                        new FollowPathCommand(follower, AutoPaths.GO_TO_SAMPLES.curve(follower), true),
                        new FollowPathCommand(follower, AutoPaths.PUSH_SAMPLE_1.curve(follower), true),
                        new FollowPathCommand(follower, AutoPaths.PUSH_SAMPLE_2.curve(follower), true),
                        new ParallelCommandGroup(
                                new ServoCommand(intakeClawRot, .58),
                                new SlideResetCommand(hSlide, hLimit),
                                new ServoCommand(outtakeClawTwist, Const.untwist),
                                new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrab),
                                new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrab),
                                new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab),
                                new ServoCommand(outtakeClaw, Const.release),
                                new SlideResetCommand(slide, vLimit),
                                new FollowPathCommand(follower, AutoPaths.PUSH_SAMPLE_3.curve(follower), true)
                        )
                        );

        Command[] grabAndScore = {
                grabAndScore(AutoPaths.GRAB_SPECIMEN_1, AutoPaths.SCORE_SPECIMEN_1, slidePos[1]),
                grabAndScore(AutoPaths.GRAB_SPECIMEN_2, AutoPaths.SCORE_SPECIMEN_2, slidePos[2]),
                grabAndScore(AutoPaths.GRAB_SPECIMEN_3, AutoPaths.SCORE_SPECIMEN_3, slidePos[3]),
                grabAndScore(AutoPaths.GRAB_SPECIMEN_4, AutoPaths.SCORE_SPECIMEN_4, slidePos[4])
        };

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                    scorePreload,
                    sampsToHp,
                    grabAndScore[0], grabAndScore[1], grabAndScore[2], grabAndScore[3]
        ));
    }

    public Command grabAndScore(AutoPaths grabPath, AutoPaths scorePath, int pos) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClawTwist, Const.untwist),
                        new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrab),
                        new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrab),
                        new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab),
                        new ServoCommand(outtakeClaw, .6),
                        new SlideResetCommand(slide, vLimit),
                        new FollowPathCommand(follower, grabPath.curve(follower), true)
                ),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 300),
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawRot, Const.rotSpecimenScore),
                        new ServoCommand(outtakeClawTwist, Const.twist),
                        new SetPIDFSlideArmCommand(slide, pos),
                        new FollowPathCommand(follower, scorePath.curve(follower), true)
                )
        );
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
