package org.firstinspires.ftc.teamcode.base.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.utils.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utils.Pose;

public class PinpointSubsystem extends SubsystemBase {
    public GoBildaPinpointDriver pinpoint;
    public PinpointSubsystem(HardwareMap h, String name){
        pinpoint = h.get(GoBildaPinpointDriver.class, name);

        pinpoint.setOffsets(3.8125, 1.875, DistanceUnit.INCH);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        pinpoint.resetPosAndIMU();
    }
    public Pose getPose(){
        pinpoint.update();
        double x = pinpoint.getPosX(DistanceUnit.INCH);
        double y = pinpoint.getPosY(DistanceUnit.INCH);
        double heading =    pinpoint.getHeading(AngleUnit.RADIANS);
        return new Pose(x, y, heading);
    }
    public void setup(double x, double y, double heading){
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.RADIANS, heading));
    }
}