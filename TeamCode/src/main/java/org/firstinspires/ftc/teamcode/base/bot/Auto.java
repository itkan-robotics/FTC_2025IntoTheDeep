package org.firstinspires.ftc.teamcode.base.bot;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

import java.util.ArrayList;

public class Auto {
    public static Robot tanveerBot;
    public static Follower f;
    public static double p = 1;

    public enum Spec {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(0)),
                new Pose(33, 6, Math.toRadians(0))
        ),

        GO_TO_SAMPLES(
                new Pose(31,  6, Math.toRadians(0)),
                new Pose(20, 6, Math.toRadians(0)),
                new Pose(20, -30, Math.toRadians(0)),
                new Pose(60, -30, Math.toRadians(180)),
                new Pose(60, -35, Math.toRadians(180))
        ),

        PUSH_1(
                new Pose(60, -35, Math.toRadians(180)),
                new Pose(30, -35, Math.toRadians(180))
        ),

        PUSH_2(
                new Pose(30, -35, Math.toRadians(180)),
                new Pose(60, -35, Math.toRadians(180)),
                new Pose(60, -45, Math.toRadians(180)),
                new Pose(30, -45, Math.toRadians(180))
        ),

        PUSH_3(
                new Pose(30, -45, Math.toRadians(180)),
                new Pose(60, -45, Math.toRadians(180)),
                new Pose(60, -48, Math.toRadians(180)),
                new Pose(30, -48, Math.toRadians(180))
        ),

        GRAB_1(
                new Pose(30, -48, Math.toRadians(180)),
                new Pose(35, -30, Math.toRadians(180)),
                new Pose(20, -30, Math.toRadians(180))
        ),

        SCORE_1(
                new Pose(20, -30, Math.toRadians(180)),
                new Pose(30, 5, Math.toRadians(180)),
                new Pose(45, 5, Math.toRadians(180))
        ),

        GRAB_2(
                new Pose(45, 5, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(20, -30, Math.toRadians(180))
        ),

        SCORE_2(
                new Pose(20, -30, Math.toRadians(180)),
                new Pose(30, 4, Math.toRadians(180)),
                new Pose(45,4, Math.toRadians(180))
        ),

        GRAB_3(
                new Pose(45, 4, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(20, -30, Math.toRadians(180))
        ),

        SCORE_3(
                new Pose(20, -30, Math.toRadians(180)),
                new Pose(30, 3, Math.toRadians(180)),
                new Pose(45, 3, Math.toRadians(180))
        ),

        GRAB_4(
                new Pose(45, 3, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(20, -30, Math.toRadians(180))
        ),

        SCORE_4(
                new Pose(20, -30, Math.toRadians(180)),
                new Pose(30, 1, Math.toRadians(180)),
                new Pose(45, 1, Math.toRadians(180))
        );

        private final Pose[] poses;

        Spec(Pose... poses) {
            this.poses = poses;
        }

        public PathChain line(Follower follower) {
            return follower.pathBuilder()
                    .addPath(new BezierLine(new Point(poses[0]), new Point(poses[1])))
                    .setLinearHeadingInterpolation(poses[0].getHeading(), poses[1].getHeading())
                    .build();
        }

        public PathChain curve(Follower follower) {
            ArrayList<Point> controlPoints = new ArrayList<>();
            for (Pose pose : poses) {
                controlPoints.add(new Point(pose.getX(), pose.getY(), 1));
            }

            BezierCurve bezierCurve = new BezierCurve(controlPoints);

            return follower.pathBuilder()
                    .addPath(bezierCurve)
                    .setLinearHeadingInterpolation(poses[0].getHeading(), poses[poses.length - 1].getHeading())
                    .build();
        }

        public Pose[] getPoses() {
            return poses;
        }
    }
    public enum Samp {
        PRELOAD(
                new Pose(0, 0, Math.toRadians(0)),
                new Pose(31, 6, Math.toRadians(0))
        ),

        GO_TO_SAMPLES(
                new Pose(31,  6, Math.toRadians(0)),
                new Pose(20, 6, Math.toRadians(0)),
                new Pose(20, -30, Math.toRadians(0)),
                new Pose(60, -30, Math.toRadians(180)),
                new Pose(60, -35, Math.toRadians(180))
        ),

        PUSH_SAMPLE_1(
                new Pose(60, -35, Math.toRadians(180)),
                new Pose(30, -35, Math.toRadians(180))
        ),

        PUSH_SAMPLE_2(
                new Pose(30, -35, Math.toRadians(180)),
                new Pose(60, -35, Math.toRadians(180)),
                new Pose(60, -45, Math.toRadians(180)),
                new Pose(30, -45, Math.toRadians(180))
        ),

        PUSH_SAMPLE_3(
                new Pose(30, -45, Math.toRadians(180)),
                new Pose(60, -45, Math.toRadians(180)),
                new Pose(60, -48, Math.toRadians(180)),
                new Pose(30, -48, Math.toRadians(180))
        ),

        GRAB_SPECIMEN_1(
                new Pose(30, -48, Math.toRadians(180)),
                new Pose(35, -30, Math.toRadians(180)),
                new Pose(22, -30, Math.toRadians(180))
        ),

        SCORE_SPECIMEN_1(
                new Pose(23, -30, Math.toRadians(180)),
                new Pose(35, 5, Math.toRadians(180)),
                new Pose(45, 5, Math.toRadians(180))
        ),

        GRAB_SPECIMEN_2(
                new Pose(45, 5, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(20, -30, Math.toRadians(180))
        ),

        SCORE_SPECIMEN_2(
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(34,4, Math.toRadians(180))
        ),

        GRAB_SPECIMEN_3(
                new Pose(36, 3, Math.toRadians(180)),
                new Pose(20, -33, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180))
        ),

        SCORE_SPECIMEN_3(
                new Pose(25, -30, Math.toRadians(180)),
                new Pose(34, 2, Math.toRadians(180))
        ),

        GRAB_SPECIMEN_4(
                new Pose(36, 2, Math.toRadians(180)),
                new Pose(20, -33, Math.toRadians(180)),
                new Pose(25, -30, Math.toRadians(180))
        ),

        SCORE_SPECIMEN_4(
                new Pose(4.75, -33, Math.toRadians(180)),
                new Pose(34, 0, Math.toRadians(180))
        );

        private final Pose[] poses;

        Samp(Pose... poses) {
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

    public Auto(Robot bot) {
        tanveerBot = bot;
        f = bot.follower.getFollower();
    }

    public static Command scoreSpecPreload() {
        return new SequentialCommandGroup(
                tanveerBot.SpecimenScoreReverse(),
                tanveerBot.FollowPath(Spec.PRELOAD.line(f), p),
                tanveerBot.ClawRelease()
        );
    }

    public static Command pushSamps() {
        return new SequentialCommandGroup(
                tanveerBot.FollowPath(Spec.GO_TO_SAMPLES.curve(f), p),
                tanveerBot.SpecimenGrab(),
                tanveerBot.FollowPath(Spec.PUSH_1.curve(f), p),
                tanveerBot.FollowPath(Spec.PUSH_2.curve(f), p),
                tanveerBot.FollowPath(Spec.PUSH_3.curve(f), p)
        );
    }

    public static Command grabAndScoreSpec(Spec grabPath, Spec scorePath) {
        return new SequentialCommandGroup(
                tanveerBot.SpecimenGrab(),
                tanveerBot.FollowPath(grabPath.curve(f), p-.2),
                tanveerBot.SpecimenScore(),
                tanveerBot.FollowPath(scorePath.curve(f), p)
        );
    }
}