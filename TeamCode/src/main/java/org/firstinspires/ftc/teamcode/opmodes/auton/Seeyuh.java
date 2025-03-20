package org.firstinspires.ftc.teamcode.opmodes.auton;

import com.arcrobotics.ftclib.command.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.base.bot.Auto;
import org.firstinspires.ftc.teamcode.base.bot.Robot;

import static org.firstinspires.ftc.teamcode.base.bot.Auto.*;

@Autonomous(name="5+1", group = ".")
public class Seeyuh extends OpMode {

    @Override
    public void init() {

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        scoreSpecPreload(),
                        pushSamps(),

                        grabAndScoreSpec(Spec.GRAB_1,
                                         Spec.SCORE_1),

                        grabAndScoreSpec(Spec.GRAB_2,
                                         Spec.SCORE_2),

                        grabAndScoreSpec(Spec.GRAB_3,
                                         Spec.SCORE_3),

                        grabAndScoreSpec(Spec.GRAB_4,
                                         Spec.SCORE_4)
                )
        );
    }

    @Override
    public void loop() { CommandScheduler.getInstance().run(); }

    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}