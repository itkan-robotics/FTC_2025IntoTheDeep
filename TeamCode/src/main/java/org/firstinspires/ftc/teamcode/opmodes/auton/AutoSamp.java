package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.base.commands.ExtendCommand;
import org.firstinspires.ftc.teamcode.base.commands.FollowPathCommand;
import org.firstinspires.ftc.teamcode.base.commands.IExtendCommand;
import org.firstinspires.ftc.teamcode.base.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.base.commands.ServoCommand;
import org.firstinspires.ftc.teamcode.base.commands.SmartIntakeCommand;
import org.firstinspires.ftc.teamcode.base.subsystems.ColorSensorSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ServoSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.WaitSubsystem;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.FConstants;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.LConstants;

import java.util.ArrayList;


@Autonomous(name="0+5",group = ".Auton")
public class AutoSamp extends OpMode {
    static Pose score = new Pose(4, 25, Math.toRadians(-45));

    public enum AutoPaths {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(0)),
                score
        ),

        GRAB_1(
                score,
                new Pose(14, 9, Math.toRadians(0))
        ),
        SCORE_1(
                new Pose(14, 9, Math.toRadians(0)),
                score
        ),
        GRAB_2(
                score,
                new Pose(14, 18, Math.toRadians(0))
        ),
        SCORE_2(
                new Pose(14, 18, Math.toRadians(0)),
                score
        ),
        GRAB_3(
                score,
                new Pose(14, 16.5, Math.toRadians(20))
        ),
        SCORE_3(
                new Pose(14, 16.5, Math.toRadians(20)),
                score
        ),
        GRAB_4(
                score,
                new Pose(60, 15, Math.toRadians(-90)),
                new Pose(70, -8, Math.toRadians(-90))
        ),
        SCORE_4(
                new Pose(70, -8, Math.toRadians(-90)),
                new Pose(60, 15, Math.toRadians(-45)),
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

    private static Follower follower;
    //FollowerConstants.holdPointTranslationalScaling = 0;
    ExtendSubsystem vSlide;
    IExtendSubsystem hSlide;
    IntakeSubsystem intake;
    ServoSubsystem intakeWrist, blocker, outtakeWrist, grabber;
    ColorSensorSubsystem colorSensor;


    @Override
    public void init() {
        vSlide = new ExtendSubsystem(hardwareMap);
        hSlide = new IExtendSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, "intake");
        intakeWrist = new ServoSubsystem(hardwareMap, "intakeWrist");
        blocker = new ServoSubsystem(hardwareMap, "blocker");
        outtakeWrist = new ServoSubsystem(hardwareMap, "scorewrist");
        grabber = new ServoSubsystem(hardwareMap, "grabber");
        colorSensor = new ColorSensorSubsystem(hardwareMap, "color_sensor");
        follower = new Follower(hardwareMap);
        Constants.setConstants(FConstants.class, LConstants.class);
        follower.setPose(AutoPaths.PRELOAD.getPoses()[0]);
        Command preloadScore = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new FollowPathCommand(follower, AutoPaths.PRELOAD.curve(follower))
                        ),
                        new ExtendCommand(vSlide, 1200),
                        new ServoCommand(outtakeWrist, 0.9)
                ),
                new WaitCommand(300),
                new ServoCommand(grabber, 0.1),
                new WaitCommand(300)
        );

/*
        //Scores
        Command score = new SequentialCommandGroup(
                new ExtendCommand(vSlide, 1200),
                new WaitCommand(pause, 100),
                new FollowPathCommand(follower, AutoPaths.SCORE.curve(follower)),
                new WaitCommand(pause, 100),
                new ServoCommand(outtakeWrist, 0.9),
                new WaitCommand(pause, 300),
                new ServoCommand(grabber, 0.1),
                new WaitCommand(pause, 500)
        );
        //Brings everything down, intakes, and transfers
        Command i = new SequentialCommandGroup(
                new WaitCommand(pause, 1000),
                new ParallelCommandGroup(
                        new ExtendCommand(vSlide, 0),
                        new IExtendCommand(hSlide, -590),
                        new ServoCommand(intakeWrist, 0.6),
                        new ServoCommand(blocker, 0),
                        new ServoCommand(outtakeWrist, 0.15),
                        new ServoCommand(grabber, 0.1),
                        new MotorCommand(intake, -1)
                ),
                new WaitCommand(pause, 300),
                new IExtendCommand(hSlide, 0),
                new ServoCommand(intakeWrist, 0.18),
                new WaitCommand(pause, 600),
                new MotorCommand(intake, -0.5),
                new ServoCommand(blocker, 0.5),
                new WaitCommand(pause, 500),
                new ServoCommand(grabber, 1),
                new WaitCommand(pause, 200),
                new MotorCommand(intake, 0)
        );
*/
        Command drive = new SequentialCommandGroup(
                preloadScore,
                intake(AutoPaths.GRAB_1),
                score(AutoPaths.SCORE_1),
                intake(AutoPaths.GRAB_2),
                score(AutoPaths.SCORE_2),
                intake(AutoPaths.GRAB_3),
                score(AutoPaths.SCORE_3),
                intake(AutoPaths.GRAB_4),
                score(AutoPaths.SCORE_4)
        );


        CommandScheduler.getInstance().schedule(drive);
    }





    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", follower.getPose().getHeading());
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Outtake Wrist", outtakeWrist.get());
        telemetry.addData("Intake Wrist", intakeWrist.get());
        telemetry.addData("Grabber", grabber.get());
        telemetry.addData("Blocker", blocker.get());
        telemetry.addData("vSlide", vSlide.get());
        telemetry.addData("hSlide", hSlide.get());
        telemetry.addData("Color Sensor", colorSensor.getColor());
        telemetry.update();
    }
    public Command score(AutoPaths p){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(follower, p.curve(follower)),
                        new SequentialCommandGroup(
                                new ParallelCommandGroup(
                                        new IExtendCommand(hSlide, 0),
                                        new ServoCommand(intakeWrist, 0),
                                        new IntakeCommand(intake, -0.6)
                                ),
                                new WaitCommand(300),
                                new ServoCommand(blocker, 0.65),
                                new WaitCommand(300),
                                new ParallelCommandGroup(
                                        new ExtendCommand(vSlide, 1200),
                                        new ServoCommand(outtakeWrist, 0.9),
                                        new IntakeCommand(intake, 0),
                                        new ServoCommand(grabber, 1)
                                )
                        )
                ),
                new WaitCommand(500),
                new ServoCommand(grabber, 0.1),
                new WaitCommand(300)
        );
    }
    public Command intake(AutoPaths p){
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(follower, p.curve(follower)),
                        new ServoCommand(outtakeWrist, 0.15),
                        new ServoCommand(blocker, 0),
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new ExtendCommand(vSlide, 0)
                        )
                ),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new ServoCommand(intakeWrist, 0.35),
                        new IExtendCommand(hSlide, -590),
                        new SmartIntakeCommand(intake, colorSensor, -0.95, "Yellow")
                )
        );
    }
}