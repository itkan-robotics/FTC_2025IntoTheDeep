package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

@Config
public class Const {
    public static final IMU.Parameters IMU_ORIENTATION = new IMU.Parameters(new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
            RevHubOrientationOnRobot.UsbFacingDirection.UP));

    //Names for motors and servos
    public static final String fr = "fr", fl = "fl", br = "br", bl = "bl",
            lSlide= "lSlide", rSlide = "rSlide", hSlide = "horizontalSlide", intake = "intakeMotor", vLimit = "vertLimitSwitch", hLimit = "horizontalLimitSwitch",
            intakeRot = "servo5", intakeDist = "servo0", outtakeClaw = "servo4", outtakeRot = "servo2", outtakeTwist = "servo3", outtakeDistRight = "servo1", outtakeDistLeft = "servo0", gearShifter = "servo6",
            imu = "imu", odo = "odo", rightHang = "servo7", leftHang = "servo8";

    //Intake
    public static double intakeInitTransferPos = .25, intakeFinalTransferPos = 0, intakeDownPos = 0.24, intakeSecondFinalTransferPos = -1,
            block = 0.03, unblock = 0.12, twist = 0.946, untwist = 0.255;

    //Outtake
    public static double outtakeClawDistTempTransfer = -1, outtakeClawDistRightInitTransfer = 1, outtakeClawDistLeftInitTransfer = 0, outtakeClawDistNew = 0.7, outtakeClawDistFinalTransfer = -1, outtakeClawRotInitTransfer = 0.347, outtakeClawRotTransfer = .505,
            grab = 0.497, release = .25,
            distBasketPos = .24, rotBasketPos = .45,
            distSpecimenGrab = 0, rotSpecimenGrab = .74, distSpecimenGrabFinal = 0.6, rotSpecimenScore = .55, distSpecimenScore = .81;

    //Gear Shifter
    public static double maxTorque = .328, minTorque = .552;
    //Slides
    public static double hSlideExtend = 400, vSlideBasket = 2750, vSlideBar = 675;

    //Misc
    public static long pause = 300;

    public static String direction = "forward";
    public static final double tickInInch = 590/24.0, lateralTickInInch = 540/24.0, tickInDeg = 1600/360.0;
    public static final String GROUP_ANDROID = "";
}