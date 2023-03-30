package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import org.firstinspires.ftc.teamcode.util.statemachine.State;

public class DefaultState implements State {

    public static final DefaultState inst = new DefaultState();

    private DefaultState(){

    }

    @Override
    public void init() {

    }

    @Override
    public void run() {

    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
