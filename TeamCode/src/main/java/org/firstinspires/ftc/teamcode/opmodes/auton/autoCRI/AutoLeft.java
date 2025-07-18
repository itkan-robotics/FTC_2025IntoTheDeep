package org.firstinspires.ftc.teamcode.opmodes.auton.autoCRI;

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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.base.bot.Const;
import org.firstinspires.ftc.teamcode.base.commands.FollowPathCommand;
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


@Autonomous(name="THe actual auto that'll hopefully work that I had to make cause you guys are all bums",group = ".Auton")
public class AutoLeft extends OpMode {
    static Pose grab = new Pose(3, -20, Math.toRadians(180));
    //get the pose for this plzzzz, should be for left human player
    static Pose scoreF = new Pose(65, -14, Math.toRadians(90));
    static Pose scoreI = new Pose(24, -22, Math.toRadians(90));
    public enum AutoPaths {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(0)),
                scoreI,
                scoreF
        ),
        GRAB_SPECIMEN(
                scoreF,
                scoreI,
                grab
        ),
        SCORE_SPECIMEN(
                grab,
                scoreI,
                scoreF
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
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.1, 0, 0.000004, 0.21, 0.1, 0, 0.000004, 0.21);
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

        Command scorePreload =
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new SlideResetCommand(hSlide, hLimit),
                                new ServoCommand(outtakeClawDistRight, .8),
                                new ServoCommand(outtakeClawDistLeft, .2),
                                new ServoCommand(outtakeClawRot, 1),
                                new ServoCommand(outtakeClawTwist, Const.twist),
                                new SetPIDFSlideArmCommand(slide, 200),
                                new FollowPathCommand(follower, AutoPaths.PRELOAD.line(follower), true)
                        ),
                        new ServoCommand(outtakeClaw, Const.release)
                );


        Command[] grabAndScore = {
                grabAndScore(315),
                grabAndScore(315),
                grabAndScore(315),
        };

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        scorePreload,
                        grabAndScore[0], grabAndScore[1], grabAndScore[2],
                        park()
                ));
    }
    public Command park(){
        return new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),
                new WaitCommand(pause, 100),
                new SetPIDFSlideArmCommand(slide, 0),
                new FollowPathCommand(follower, AutoPaths.GRAB_SPECIMEN.curve(follower), true)
        );
    }

    public Command grabAndScore(int height) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClawTwist, Const.untwist),
                        new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrab),
                        new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrab),
                        new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab),
                        new ServoCommand(outtakeClaw, .6),
                        new SlideResetCommand(slide, vLimit),
                        new FollowPathCommand(follower, AutoPaths.GRAB_SPECIMEN.curve(follower), true)
                ),
                new FollowPathCommand(follower, AutoPaths.GRAB_SPECIMEN.curve(follower), true, .4),
                new WaitCommand(pause, 150),
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 350),
                new ParallelCommandGroup(
                        new ServoCommand(outtakeClawDistRight, 1-Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawDistLeft, Const.distSpecimenGrabFinal),
                        new ServoCommand(outtakeClawRot, Const.rotSpecimenScore),
                        new ServoCommand(outtakeClawTwist, Const.twist),
                        new SetPIDFSlideArmCommand(slide, height),
                        new FollowPathCommand(follower, AutoPaths.SCORE_SPECIMEN.curve(follower), true)
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
