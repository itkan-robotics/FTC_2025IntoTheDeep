package org.firstinspires.ftc.teamcode.utils;

public class PointState {
    RobotState r;
    Pose p;
    public PointState(RobotState r, Pose p){
        this.r = r;
        this.p = p;
    }
    public RobotState state(){
        return r;
    }
    public Pose pose(){
        return p;
    }
}
