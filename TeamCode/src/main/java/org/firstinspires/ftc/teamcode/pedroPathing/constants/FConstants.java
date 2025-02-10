package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.CustomFilteredPIDFCoefficients;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Const;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.PINPOINT;

        FollowerConstants.leftFrontMotorName = Const.fl;
        FollowerConstants.leftRearMotorName = Const.bl;
        FollowerConstants.rightFrontMotorName = Const.fr;
        FollowerConstants.rightRearMotorName = Const.br;

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        FollowerConstants.mass = 13;

        FollowerConstants.xMovement = 68.33858333455704;
        FollowerConstants.yMovement = -64.56794996448953;

        FollowerConstants.forwardZeroPowerAcceleration = -36.2118;
        FollowerConstants.lateralZeroPowerAcceleration = -71.5747;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(-0.1,0,1e-8,0.05);
        FollowerConstants.useSecondaryTranslationalPID = false;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.1,0,0.01,0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(1.2,0, 1e-13, 0.01);
        FollowerConstants.useSecondaryHeadingPID = false;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(2,0,0.1,0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(-0.006,0,-1e-23,0.6,0.001);
        FollowerConstants.useSecondaryDrivePID = false;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.1,0,0,0.6,0); // Not being used, @see useSecondaryDrivePID

        FollowerConstants.zeroPowerAccelerationMultiplier = 5;
        FollowerConstants.centripetalScaling = 5e-7;

        FollowerConstants.pathEndTimeoutConstraint = 200;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;

        FollowerConstants.nominalVoltage = 13.14;
        FollowerConstants.useVoltageCompensationInAuto = true;
    }
}
