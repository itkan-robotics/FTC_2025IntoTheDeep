package org.firstinspires.ftc.teamcode.base.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.FConstants;
import org.firstinspires.ftc.teamcode.tuning.Pedro.constants.LConstants;

public class FollowerSubsystem extends SubsystemBase {
    private final Follower f;
    private final Telemetry telemetry;


    public FollowerSubsystem(Follower f, Pose start, Telemetry telemetry) {
        Constants.setConstants(FConstants.class, LConstants.class);
        this.f = f;
        this.telemetry = telemetry;
        setStartPose(start);
        telemTs(telemetry);
    }


    public void setStartPose(Pose start) {
        f.setPose(start);
    }

    public Follower getFollower() {
        return this.f;
    }

    public void telemTs(Telemetry t) {
        f.update();
        Pose p = f.getPose();

        t.addData("X", "%.2f", p.getX());
        t.addData("Y", "%.2f", p.getY());
        t.addData("Heading (deg)", "%.2f", Math.toDegrees(p.getHeading()));
        t.addData("Busy", f.isBusy() ? "Yes 😎" : "No 😭");

        t.update();
    }

    @Override
    public void periodic() {
        telemTs(telemetry);
    }
}
