package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.*;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.commands.*;
import org.firstinspires.ftc.teamcode.utils.*;

@TeleOp
public class Duo extends CommandOpMode {
    GamepadEx base;
    public static GamepadEx op;
    SimpleLogger log;

    public static Drive drive;
    public static ServoSubsystem outtakeClawRot, outtakeClaw, intakeClawDist, intakeClawRot, outtakeClawTwist, outtakeClawDistRight, outtakeClawDistLeft, shifter;
    public static IntakeSubsystem intake;
    public static LimitSwitchSubsystem vLimit, hLimit;
    public static PIDFSlideSubsystem slide;
    public static PIDFSingleSlideSubsystem hSlide;
    public static WaitSubsystem pause;

    @Override
    public void initialize() {
        base = new GamepadEx(gamepad1);
        op = new GamepadEx(gamepad2);
        log = new SimpleLogger();
        intake = new IntakeSubsystem(hardwareMap, Const.intake);
        drive = new Drive(hardwareMap, Const.imu,new MotorConfig(Const.fr, Const.fl, Const.br, Const.bl),new MotorDirectionConfig(false,true,false,true));
        hSlide = new PIDFSingleSlideSubsystem(hardwareMap, Const.hSlide, -0.1, 0, 0, 0.0);
        slide = new PIDFSlideSubsystem(hardwareMap, Const.rSlide, Const.lSlide, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, 0.02, 0,  0.000003, 0, 0.02, 0, 0.000003, 0);
        pause = new WaitSubsystem();
        outtakeClaw = new ServoSubsystem(hardwareMap, Const.outtakeClaw);
        intakeClawDist = new ServoSubsystem(hardwareMap, Const.intakeDist);
        intakeClawRot = new ServoSubsystem(hardwareMap, Const.intakeRot);
        outtakeClawDistLeft = new ServoSubsystem(hardwareMap, Const.outtakeDistLeft);
        outtakeClawDistRight = new ServoSubsystem(hardwareMap, Const.outtakeDistRight);
        vLimit = new LimitSwitchSubsystem(hardwareMap, Const.vLimit);
        hLimit = new LimitSwitchSubsystem(hardwareMap, Const.hLimit);
        shifter = new ServoSubsystem(hardwareMap, Const.gearShifter);
        outtakeClawRot = new ServoSubsystem(hardwareMap, Const.outtakeRot);
        outtakeClawTwist = new ServoSubsystem(hardwareMap, Const.outtakeTwist);

        //Default Commands
        drive.setDefaultCommand(new DriveCommand(drive,base));

        //Intake
        new GamepadButton(op, GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new IntakeCommand(intake, -1)).whenReleased(new IntakeCommand(intake, 0));
        new GamepadButton(op, GamepadKeys.Button.LEFT_BUMPER).whenPressed(new IntakeCommand(intake, 1)).whenReleased(new IntakeCommand(intake, 0));

        //Transfer
        new GamepadButton(op, GamepadKeys.Button.A).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, 0.377),
                //new WaitCommand(pause, 300),

                new ServoCommand(intakeClawRot, 0.646),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistLeft, 0.963),
                new ServoCommand(outtakeClawDistRight, 0.037),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.51),
                //new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawTwist, 0.924),
                //new WaitCommand(pause, 300),
                //new ServoCommand(intakeClawRot, 0.4),
                //new WaitCommand(pause, 300),
                new SlideResetCommand(hSlide, hLimit),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, 0.526),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistLeft, 1),
                new ServoCommand(outtakeClawDistRight, 0),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, 0.4429),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, 0.4),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, 0.30 ),
                new SetPIDFSlideArmCommand(slide, 15000)
        ));

        //Old transfer
        new GamepadButton(op, GamepadKeys.Button.B).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.release),

                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, Const.intakeInitTransferPos),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistLeft, Const.outtakeClawDistLeftInitTransfer),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistRight, Const.outtakeClawDistRightInitTransfer),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawRot, Const.outtakeClawRotTransfer),
                new WaitCommand(pause, 300),
                new ServoCommand(intakeClawRot, Const.intakeFinalTransferPos),
                new WaitCommand(pause, 300),
                new SlideResetCommand(slide, vLimit),
                new SetPIDFSlideArmCommand(hSlide, 400),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClaw, Const.grab)




        ));


        //Basket Score
        new GamepadButton(op, GamepadKeys.Button.Y).whenPressed(
                new SequentialCommandGroup(
                        new ServoCommand(outtakeClawRot, Const.rotBasketPos),
                        new ServoCommand(outtakeClawDistRight, 0.5),
                        new SetPIDFSlideArmCommand(slide, 2750)
                ));

        //Release in Basket
        new GamepadButton(op, GamepadKeys.Button.X).whenPressed(new ServoCommand(outtakeClaw, Const.release));

        //Specimen Grab Pos
        /*
        new GamepadButton(base, GamepadKeys.Button.A).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClawDistRight, Const.distSpecimenGrab),
                new ServoCommand(outtakeClawRot, Const.rotSpecimenGrab),
                new ServoCommand(outtakeClaw, Const.release)
        ));
*/
        //Grab
        new GamepadButton(base, GamepadKeys.Button.X).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.grab),
                new WaitCommand(pause, 300),
                new ServoCommand(outtakeClawDistRight, Const.distSpecimenGrabFinal)
        ));

        //Release
        new GamepadButton(op, GamepadKeys.Button.DPAD_UP).whenPressed(new ServoCommand(outtakeClaw, Const.release));

        //VSlide Down
        new GamepadButton(op, GamepadKeys.Button.DPAD_DOWN).whenPressed(new SlideResetCommand(slide, vLimit));

        //Specimen Score
        new GamepadButton(base, GamepadKeys.Button.Y).whenPressed(new SequentialCommandGroup(
                new ServoCommand(outtakeClaw, Const.grab),
                new ServoCommand(outtakeClawDistRight, Const.distSpecimenScore),
                new ServoCommand(outtakeClawRot, Const.rotSpecimenScore),
                new SetPIDFSlideArmCommand(slide, 575)
        ));

//        new GamepadButton(base, GamepadKeys.Button.B).whenPressed(new SequentialCommandGroup(
//                new ServoCommand(outtakeClawDistRight, Const.distBasketPos-0.1),
//                new SlideResetCommand(slide, vLimit),
//                new ServoCommand(outtakeClaw, Const.release)
//        ));

        new GamepadButton(base, GamepadKeys.Button.DPAD_LEFT).whenPressed(new SetPIDFSlideArmCommand(slide, 2400));


    }
}
