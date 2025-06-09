package org.firstinspires.ftc.teamcode.utils;

public class RobotState {
    Object[] arr;
    public RobotState(Object o, int i){
        arr = new Object[]{o, i};
    }
    public RobotState(Object o){
        arr = new Object[]{o};
    }
    public Object[] get(){
        return arr;
    }
}