package org.firstinspires.ftc.teamcode.auto;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.ScoreCommand;
import org.firstinspires.ftc.teamcode.commands.TelemetryCommand;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.ScoringSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;

@Autonomous
public class Close extends Robot {
    boolean isRedAlliance = true;
    public static Drive drive;
    public static ScoringSubsystem score;
    @Override
    public void initialize() {
        score = new ScoringSubsystem(hardwareMap);

        drive = new Drive(hardwareMap);// this is where we schedule commands for auto
//        schedule(
//                new ParallelCommandGroup(
//                        new SequentialCommandGroup( // auto
//                                new ParallelCommandGroup(
//                                        new ScoreCommand(score,ScoringSubsystem.SprocketStates.STOW),
//                                        new DriveCommand(drive,15,-18,0,0.5,1)
//                                        ),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,12,12,0,0.5,1),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,4,9,-45,0.5,1),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,12,19,0,0.5,1),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,4,9,-45,0.5,1),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,10.5,23,26,0.5,1),
//                                new DriveCommand(drive,1),
//                                new DriveCommand(drive,4,9,-45,0.5,1),
//                                new DriveCommand(drive,1)
//                                ),
//                        new RunCommand(() -> { // telemetry
//                            telemetry.addData("Sprocket Pos",score.sprocket.getCurrentPosition());
//                            telemetry.addData("X: "+Close.drive.getX()+", Y: "+Close.drive.getY()+", Heading", Close.drive.getHeading());
//                            telemetry.update();
//                        })
//                )
//        );
        schedule(
                new ParallelCommandGroup(
                new SequentialCommandGroup(
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5),
                new DriveCommand(drive,0,0,-90,0.5,0.5),
                new DriveCommand(drive,0,0,90,0.5,0.5)

            ),
                        new RunCommand(() -> { // telemetry
                            telemetry.addData("Sprocket Pos",score.sprocket.getCurrentPosition());
                            telemetry.addData("X: "+Close.drive.getX()+", Y: "+Close.drive.getY()+", Heading", Close.drive.getHeading());
                            telemetry.update();
                        })

                ));


//        schedule(
//                new MoveCommand(drive)
//        );
    }

    @Override
    public void preInit() {
        telemetry.addData("X: "+Close.drive.getX()+", Y: "+Close.drive.getY()+", Heading: "+Close.drive.getHeading(), "");
        telemetry.addData("RedAlliance?",isRedAlliance);
        telemetry.addData("A for red alliance, X for blue alliance","");
        if(gamepad1.a|| gamepad2.a){
            isRedAlliance = true;
        }else if(gamepad1.x||gamepad2.x){
            isRedAlliance = false;
        }
        telemetry.update();
    }
}
