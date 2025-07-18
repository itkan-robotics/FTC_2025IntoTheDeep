package org.firstinspires.ftc.teamcode.base.bot;

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
            intakeRot = "servo4", intakeDist = "servo0", outtakeClaw = "servo5", outtakeRot = "servo2", outtakeTwist = "servo3", outtakeDistRight = "servo1", outtakeDistLeft = "servo0", gearShifter = "servo6",
            imu = "imu", odo = "odo", rightHang = "servo7", leftHang = "servo8";

    //Intake
    public static double intakeInitTransferPos = .25, intakeFinalTransferPos = 0, intakeDownPos = 0.220, intakeSecondFinalTransferPos = -1,
            block = 0.03, unblock = 0.12, twist = 0.946, untwist = 0.255;

    //Outtake
    public static double outtakeClawDistTempTransfer = -1, outtakeClawDistRightInitTransfer = 1, outtakeClawDistLeftInitTransfer = 0, outtakeClawDistNew = 0.7, outtakeClawDistRightFinalTransfer = -1, outtakeClawDistLeftFinalTransfer = -1, outtakeClawRotInitTransfer = 0.347, outtakeClawRotTransfer = .505,
            release = 0.5, grab = .13,
            distBasketPos = .4, rotBasketPos = 0,
            distSpecimenGrab = 0.01, rotSpecimenGrab = .916, distSpecimenGrabFinal = .785, rotSpecimenScore = 0.6, distSpecimenScore = .786;

    //Gear Shifter
    public static double torque = .42, speed = .3;
    //Slides
    public static double hSlideExtend = 400, vSlideBasket = 2750, vSlideBar = 675;

    //Misc
    public static long pause = 300;

    public static String direction = "forward";
    public static final double tickInInch = 590/24.0, lateralTickInInch = 540/24.0, tickInDeg = 1600/360.0;
    public static final String GROUP_ANDROID = "";
}