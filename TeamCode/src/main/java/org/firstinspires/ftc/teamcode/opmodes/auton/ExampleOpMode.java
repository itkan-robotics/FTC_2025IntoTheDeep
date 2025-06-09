package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.base.subsystems.ColorSensorSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.Follower;
import org.firstinspires.ftc.teamcode.base.subsystems.IExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.PinpointSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ServoSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.utils.PointState;
import org.firstinspires.ftc.teamcode.utils.Pose;
import org.firstinspires.ftc.teamcode.utils.RobotState;

import java.util.ArrayList;
import java.util.List;

@Autonomous
public class ExampleOpMode extends OpMode {

    ExtendSubsystem vSlide;
    IExtendSubsystem hSlide;
    IntakeSubsystem intake;
    ServoSubsystem intakeWrist, blocker, outtakeWrist, grabber;
    ColorSensorSubsystem colorSensor;
    DriveSubsystem drive;
    PinpointSubsystem pinpoint;
    Superstructure superstructure;
    Follower follower;
    List<PointState> poses = new ArrayList<>();
    int currentStage = 0;

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
        drive = new DriveSubsystem(hardwareMap, "fl", "fr", "bl", "br", false, false, false, false);
        pinpoint = new PinpointSubsystem(hardwareMap, "odo");
        pinpoint.setup(0, 0, 90);
        superstructure = new Superstructure(vSlide, hSlide, intake, intakeWrist, blocker, outtakeWrist, grabber, colorSensor);
        follower = new Follower(drive, pinpoint);

        //Add all steps in OpMode here
        poses.add(new PointState(new RobotState(Superstructure.WantedState.HIGH_BASKET_SCORE), new Pose(-20, 15, 225)));
        poses.add(new PointState(new RobotState(Superstructure.WantedState.INTAKE), new Pose(-9, 17, 90)));
    }


    @Override
    public void loop() {
        superstructure.update();
        follower.update();
        if(superstructure.atDesiredState() && follower.atTargetPose()){
            follower.setTargetPose(poses.get(currentStage).pose());
            superstructure.setDesiredState(poses.get(currentStage).state());
            currentStage++;
        }
    }
}