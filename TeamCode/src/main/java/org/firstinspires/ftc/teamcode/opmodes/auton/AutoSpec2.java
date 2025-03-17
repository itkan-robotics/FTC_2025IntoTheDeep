package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.base.bot.Robot;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.base.bot.Auto.*;

@Autonomous(name="5+1", group = ".Auton")
public class AutoSpec2 extends OpMode {
    Robot bot = new Robot(Robot.Mode.AUTO, null, null, hardwareMap);

    @Override
    public void init() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        scoreSpecPreload(),
                        pushSamps(),

                        grabAndScoreSpec(Spec.GRAB_SPECIMEN_1,
                                         Spec.SCORE_SPECIMEN_1),

                        grabAndScoreSpec(Spec.GRAB_SPECIMEN_2,
                                         Spec.SCORE_SPECIMEN_2),

                        grabAndScoreSpec(Spec.GRAB_SPECIMEN_3,
                                         Spec.SCORE_SPECIMEN_3),

                        grabAndScoreSpec(Spec.GRAB_SPECIMEN_4,
                                         Spec.SCORE_SPECIMEN_4)
                )
        );
    }

    @Override
    public void loop() { CommandScheduler.getInstance().run(); }

    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}