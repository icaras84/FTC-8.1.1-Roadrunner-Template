package org.firstinspires.ftc.teamcode.util.statemachine;

public class Flag<T extends Enum> {

    public enum Basic{
        ON,
        OFF
    }

    private T currentState;

    public Flag(){
        this(null);
    }

    public Flag(T initialState){
        this.currentState = initialState;
    }

    public T getCurrentState(){
        return this.currentState;
    }

    public Flag setCurrentState(T nState){
        this.currentState = nState;
        return this;
    }

    @Override
    public boolean equals(Object other){
        if (other instanceof Flag){
            Flag oFlag = (Flag) other;
            return oFlag.getCurrentState().equals(this.currentState);
        } else {
            return false;
        }
    }
}
