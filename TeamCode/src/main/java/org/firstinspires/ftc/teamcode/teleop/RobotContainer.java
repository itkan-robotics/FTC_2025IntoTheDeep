package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.commands.ArmCommand;
import org.firstinspires.ftc.teamcode.utils.MotorConfig;
import org.firstinspires.ftc.teamcode.utils.MotorDirectionConfig;
import org.firstinspires.ftc.teamcode.utils.SimpleLogger;
@TeleOp(name = "TeleOp", group = Constants.Params.GROUP_ANDROID)
public class RobotContainer extends CommandOpMode {
    public static Drive drive;

    public static ArmSubsystem arm;
    public static SimpleLogger log;
    public static TelemetrySubsystem telemetrySubsystem;

    public static Boolean isAllianceBlue = true;


    @Override
    public void initialize() {
        // variables
        GamepadEx base = new GamepadEx(gamepad1);
        GamepadEx operator = new GamepadEx(gamepad2);

        telemetrySubsystem = new TelemetrySubsystem(log,telemetry, FtcDashboard.getInstance());
        drive = new Drive(hardwareMap,"imu",new MotorConfig("fr","fl","br","bl"),new MotorDirectionConfig(false,true,false,true));

        arm = new ArmSubsystem(hardwareMap, "arm", 0.01 , 0, 0.00001, 0.01, 7.739);

        drive.setBrakeMode(true);
        arm.setBrakeMode(true);

        // default commands
        drive.setDefaultCommand(new DriveCommand(drive,base));
        //xButton.whenPressed(arm::change(-100));

//        new GamepadButton(base, GamepadKeys.Button.A).toggleWhenPressed(new HandCommand(hand, Constants.out), new HandCommand(hand, Constants.in));
//        new GamepadButton(base, GamepadKeys.Button.B).toggleWhenPressed(new DroneCommand(drone, Constants.launch), new DroneCommand(drone, Constants.load));
//        new GamepadButton(base, GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new IntakeCommand(intake, 1)).whenPressed(new TransferCommand(transfer, -1)).whenReleased(new IntakeCommand(intake, 0)).whenReleased(new TransferCommand(transfer, 0));
//        new GamepadButton(base, GamepadKeys.Button.LEFT_BUMPER).whenPressed(new IntakeCommand(intake, -1)).whenReleased(new IntakeCommand(intake, 0));
        new GamepadButton(base, GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new ArmCommand(arm, 0));
        new GamepadButton(base, GamepadKeys.Button.LEFT_BUMPER).whenPressed(new ArmCommand(arm, 820));
        // telemetry stuffs

        //telemetrySubsystem.addLogHeadings();

        schedule(new RunCommand(telemetrySubsystem::addTelemetryData));
        //schedule(new RunCommand(telemetrySubsystem::addDashBoardData));

        // update logging stuffs

        //schedule(new RunCommand(telemetrySubsystem::updateDashboardTelemetry));
        //schedule(new RunCommand(telemetrySubsystem::updateLogs));
        schedule(new RunCommand(telemetry::update));

        // do everything before this loop.....

        // pre init
        while(opModeInInit()&&!opModeIsActive()){
            // do pre init stuffs here!!!!!
            telemetry.addLine("'X' to set alliance to BLUE");
            telemetry.addLine("'Y' to set alliance to RED");





            if(base.getButton(GamepadKeys.Button.X) | operator.getButton(GamepadKeys.Button.X)){
                // set it to blue
                telemetry.addLine("Blue Selected!");
                isAllianceBlue = true;
            } else if (base.getButton(GamepadKeys.Button.Y) | operator.getButton(GamepadKeys.Button.Y)) {
                telemetry.addLine("Red Selected!");
                isAllianceBlue = false;
            }
            telemetry.clearAll();
        }
    }
}
