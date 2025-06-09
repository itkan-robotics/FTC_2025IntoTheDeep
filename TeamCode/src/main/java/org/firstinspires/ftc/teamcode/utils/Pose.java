package org.firstinspires.ftc.teamcode.utils;

public class Pose {
    double x, y, heading;
    public Pose(double x, double y, double heading){
        this.x = x;
        this.heading = heading;
        this.y = y;
        while(this.heading > 360){
            this.heading -= 360;
        }
        while(this.heading < 0){
            this.heading += 360;
        }
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getHeading(){
        return heading;
    }
}