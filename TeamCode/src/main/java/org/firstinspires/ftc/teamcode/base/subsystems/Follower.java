package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.base.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PinpointSubsystem;
import org.firstinspires.ftc.teamcode.utils.Pose;

public class Follower{
    public Pose currentPose = new Pose(0, 0, 90);
    public Pose desiredPose = currentPose;
    public DriveSubsystem drive;
    public PinpointSubsystem pinpoint;
    PIDFController translationalController = new PIDFController(0, 0, 0, 0);
    PIDFController angularController = new PIDFController(0, 0, 0, 0);
    public Follower(DriveSubsystem drive, PinpointSubsystem pinpoint){
        this.drive = drive;
        this.pinpoint = pinpoint;
        translationalController.setSetPoint(0);
        angularController.setSetPoint(0);
    }
    public void setTargetPose(Pose p){
        desiredPose = p;
    }
    public void update(){
        currentPose = pinpoint.getPose();
        double translationalPower = translationalController.calculate(Math.sqrt(Math.pow(desiredPose.getX() - currentPose.getX(), 2) + Math.pow(desiredPose.getY()-currentPose.getY(), 2)));
        double rotationalPower = angularController.calculate(normalize(desiredPose.getHeading()-currentPose.getHeading()));
        double xPow = translationalPower * Math.cos(Math.atan2(desiredPose.getY()-currentPose.getY(), desiredPose.getX() - currentPose.getX()));
        double yPow = translationalPower * Math.sin(Math.atan2(desiredPose.getY()-currentPose.getY(), desiredPose.getX() - currentPose.getX()));
        drive.set(xPow, yPow, rotationalPower);
    }
    public boolean atTargetPose(){
        return Math.abs(normalize(desiredPose.getHeading()-currentPose.getHeading())) <= 1 && Math.sqrt(Math.pow(desiredPose.getX() - currentPose.getX(), 2) + Math.pow(desiredPose.getY()-currentPose.getY(), 2)) <= 1;
    }


    public double normalize(double degrees){
        while(degrees > 180){
            degrees -= 360;
        }
        while (degrees <= -180){
            degrees += 360;
        }
        return degrees;
    }
}