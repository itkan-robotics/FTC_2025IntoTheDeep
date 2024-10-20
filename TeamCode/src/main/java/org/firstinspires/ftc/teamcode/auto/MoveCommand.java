package org.firstinspires.ftc.teamcode.auto;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Drive;

public class MoveCommand extends SequentialCommandGroup {
    double x,y,theta,tolerance,power;
    public MoveCommand(Drive drive){
        addCommands(
                new DriveCommand(drive,15,0,0,0.5,1.5),
                new DriveCommand(drive,15,-15,0,0.5,1.5)
                );
        addRequirements(drive);
    }
}
