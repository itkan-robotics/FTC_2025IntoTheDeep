package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.base.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PinpointSubsystem;
import org.firstinspires.ftc.teamcode.utils.Pose;

@Config
@TeleOp
public class FollowerTuning extends OpMode {
    DriveSubsystem drive;
    PinpointSubsystem pinpoint;
    public static boolean useTranslational = true;
    public static boolean useRotational = false;
    public static double tP = 0, tI = 0, tD = 0, tF = 0, rP = 0, rI = 0, rD = 0, rF = 0;
    PIDFController translationalController, angularController;
    public static double x = 0, y = 0, heading = 0;
    Pose desiredPose;
    FtcDashboard dashboard;
    public static boolean back = false;

    @Override
    public void init(){
        drive = new DriveSubsystem(hardwareMap, "fl", "fr", "bl", "br", false, true, false, true);
        pinpoint = new PinpointSubsystem(hardwareMap, "odo");
        pinpoint.setup(0, 0, 0);
        useTranslational = true;
        useRotational = false;
        tP = 0;
        tI = 0;
        tD = 0;
        tF = 0;
        rP = 0;
        rI = 0;
        rD = 0;
        rF = 0;
        translationalController = new PIDFController(tP, tI, tD, tF);
        angularController = new PIDFController(rP, rI, rD, rF);
        x = 0;
        y = 20;
        heading = 90;
        desiredPose = new Pose(x, y, Math.toRadians(heading));
        dashboard = FtcDashboard.getInstance();
        back = false;

    }
    @Override
    public void loop(){
        update();
    }

    public void update(){
        if(back) desiredPose = new Pose(0, 0, 0);
        else desiredPose = new Pose(x, y, heading);
        translationalController.setP(tP);
        translationalController.setI(tI);
        translationalController.setD(tD);
        translationalController.setF(tF);
        angularController.setP(rP);
        angularController.setI(rI);
        angularController.setD(rD);
        angularController.setF(rF);
        Pose currentPose = pinpoint.getPose();
        double dx = desiredPose.getX()-currentPose.getX();
        double dy = desiredPose.getY()-currentPose.getX();
        double magnitude = Math.hypot(dy, dx);
        double fcAngleRad = Math.atan2(dy, dx);
        double rcAngleRad = AngleUnit.normalizeRadians(fcAngleRad - currentPose.getHeading());
        double translationalPower = translationalController.calculate(magnitude);
        double xComp = translationalPower * Math.cos(rcAngleRad);
        double yComp = translationalPower * Math.sin(rcAngleRad);
        double rotationalPower = angularController.calculate(rcAngleRad);
        if(useTranslational && useRotational) drive.set(xComp, yComp, rotationalPower);
        else if (useRotational) drive.set(0, 0, rotationalPower);
        else if (useTranslational) drive.set(yComp, xComp, 0);

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Position Error", magnitude);
        packet.put("Heading Error", Math.toDegrees(rcAngleRad));
        dashboard.sendTelemetryPacket(packet);

    }
}