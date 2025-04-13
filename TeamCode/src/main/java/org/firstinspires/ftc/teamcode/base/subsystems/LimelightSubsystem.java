package org.firstinspires.ftc.teamcode.base.subsystems;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LimelightSubsystem extends SubsystemBase {
    /**
     * Creates a new ExampleSubsystem.
     */
    private final Limelight3A ll;
    private LLResult result;
    public LimelightSubsystem(HardwareMap h, String name, int pipeline) {
        this.ll = h.get(Limelight3A.class, name);
        ll.pipelineSwitch(pipeline);
        ll.setPollRateHz(250);
        ll.start();
        result = ll.getLatestResult();
    }
    public LLResult getResult() {
        return result;
    }
    public boolean isConnected() {
        return ll.isConnected();
    }
    public boolean isRunning() {
        return ll.isRunning();
    }
    public void stop() {
        ll.stop();
    }
    public void start() {
        ll.start();
    }
    public void pause() {
        ll.pause();
    }
    public void reloadPipeline() {
        ll.reloadPipeline();
    }

    public void setPipeline(int pipeline) {
        ll.pipelineSwitch(pipeline);
    }

    public void setPollRate(int rate) {
        ll.setPollRateHz(rate);
    }

    public void captureSnapshot(String snapname) {
        ll.captureSnapshot(snapname);
    }
    



    @Override
    public void periodic() {
        result = ll.getLatestResult();
    }
}