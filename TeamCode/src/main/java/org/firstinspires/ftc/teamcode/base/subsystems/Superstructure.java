package org.firstinspires.ftc.teamcode.base.subsystems;


import org.firstinspires.ftc.teamcode.base.subsystems.ColorSensorSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IExtendSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.base.subsystems.ServoSubsystem;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public class Superstructure {
    public enum WantedState {
        STOW,
        HIGH_BASKET_POS,
        LOW_BASKET_POS,
        HIGH_BASKET_SCORE,
        LOW_BASKET_SCORE,
        INTAKE
    }
    public enum CurrentState {
        STOW,
        HIGH_BASKET_POS,
        LOW_BASKET_POS,
        HIGH_BASKET_SCORE,
        LOW_BASKET_SCORE,
        INTAKE,
        TRANSFER
    }
    public RobotState currentState = new RobotState(CurrentState.STOW);
    public RobotState previousState = currentState;
    public RobotState desiredState = currentState;
    public boolean transferred = true;
    public ExtendSubsystem vSlide;
    public IExtendSubsystem hSlide;
    public IntakeSubsystem intake;
    public ServoSubsystem intakeWrist, blocker, outtakeWrist, grabber;
    public ColorSensorSubsystem colorSensor;
    public Superstructure(ExtendSubsystem vSlide, IExtendSubsystem hSlide, IntakeSubsystem intake, ServoSubsystem intakeWrist, ServoSubsystem blocker, ServoSubsystem outtakeWrist, ServoSubsystem grabber, ColorSensorSubsystem colorSensor){
        this.vSlide = vSlide;
        this.hSlide = hSlide;
        this.intake = intake;
        this.intakeWrist = intakeWrist;
        this.blocker = blocker;
        this.outtakeWrist = outtakeWrist;
        this.grabber = grabber;
        this.colorSensor = colorSensor;
    }

    public CurrentState getCurrentState(){
        return (CurrentState)currentState.get()[0];
    }
    public boolean atDesiredState(){
        return ((WantedState)desiredState.get()[0]).ordinal() == ((CurrentState)currentState.get()[0]).ordinal();
    }

    public void setDesiredState(RobotState desiredState){
        this.desiredState = desiredState;
    }

    public void update(){
        switchStateManager();
        switchState();
    }

    public void switchStateManager(){
        previousState = currentState;
        WantedState desiredStateEnum = (WantedState)desiredState.get()[0];
        switch(desiredStateEnum){
            case STOW:
                currentState = new RobotState(CurrentState.STOW);
                break;
            case HIGH_BASKET_SCORE:
                if(atHighBasketPos()){
                    currentState = new RobotState(CurrentState.HIGH_BASKET_SCORE);
                    break;
                }
            case HIGH_BASKET_POS:
                if(transferred){
                    currentState = new RobotState(CurrentState.HIGH_BASKET_POS);
                }
                else {
                    currentState = new RobotState(CurrentState.TRANSFER);
                }
                break;
            case LOW_BASKET_SCORE:
                if(atLowBasketPos()){
                    currentState = new RobotState(CurrentState.LOW_BASKET_SCORE);
                    break;
                }
            case LOW_BASKET_POS:
                if(transferred){
                    currentState = new RobotState(CurrentState.LOW_BASKET_POS);
                }
                else {
                    currentState = new RobotState(CurrentState.TRANSFER);
                }
                break;
            case INTAKE:
                currentState = new RobotState(CurrentState.INTAKE, (Integer)desiredState.get()[1]);
                break;
        }
    }

    public void switchState(){
        if((CurrentState)previousState.get()[0] == (CurrentState)currentState.get()[0]) return;
        CurrentState currentStateEnum = (CurrentState)currentState.get()[0];
        pause(300);
        switch(currentStateEnum){
            case STOW:
                stow();
                break;
            case HIGH_BASKET_SCORE:
                highBasketScore();
                break;
            case HIGH_BASKET_POS:
                highBasketPos();
                break;
            case LOW_BASKET_SCORE:
                lowBasketScore();
                break;
            case LOW_BASKET_POS:
                lowBasketPos();
                break;
            case INTAKE:
                intake((Integer)currentState.get()[1]);
                break;
            case TRANSFER:
                transfer();
                break;
        }
    }

    public void stow(){
        vSlide.set(0);
        hSlide.set(0);
        outtakeWrist.set(0.15);
        intakeWrist.set(0);
    }
    public void highBasketScore(){
        grabber.set(0.1);
        transferred = false;
    }
    public void highBasketPos(){
        vSlide.set(1200);
        outtakeWrist.set(0.9);
    }
    public void lowBasketScore(){
        grabber.set(0.1);
        transferred = false;
    }
    //Idk the correct low basket slide height, assumed 750 - TODO
    public void lowBasketPos(){
        vSlide.set(750);
        outtakeWrist.set(0.9);
    }
    public void intake(int distance){
        blocker.set(0);
        vSlide.set(0);
        outtakeWrist.set(0.15);
        hSlide.set(distance);
        intakeWrist.set(0.35);
        intake.set(-0.95);
    }
    public void transfer(){
        hSlide.set(0);
        intakeWrist.set(0);
        intake.set(-0.6);
        pause(300);
        blocker.set(0.65);
        transferred = true;
    }

    public boolean atHighBasketPos(){
        return vSlide.get()>1175;
    }
    //Idk low basket pos, assumed 750 - TODO
    public boolean atLowBasketPos(){
        return vSlide.get()>725 && vSlide.get()<775;
    }
    public void pause(long duration){
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < duration) {}
    }
}