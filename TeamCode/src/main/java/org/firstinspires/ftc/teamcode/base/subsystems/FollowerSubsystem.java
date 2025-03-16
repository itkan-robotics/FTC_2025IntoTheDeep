package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.FConstants;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.LConstants;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class FollowerSubsystem extends SubsystemBase {
    private final Follower follower;
    private final Pose start;

    public FollowerSubsystem(HardwareMap h, Follower follower, Pose start)  {
        Constants.setConstants(FConstants.class, LConstants.class);
        this.follower = follower;
        this.start = start;
    }

    public Follower getFollower() {
        return this.follower;
    }

    public void setStartPose(Pose pose) {
        follower.setPose(pose);
    }

    public Pose getPose() {
        return follower.getPose();
    }

    @Override
    public void periodic() {
//        follower.update();
//
//        telemetry.addData("X", follower.getPose().getX());
//        telemetry.addData("Y", follower.getPose().getY());
//        telemetry.addData("Heading", follower.getPose().getHeading());
//        telemetry.addData("Busy", follower.isBusy());
//        telemetry.update();
    }
}
