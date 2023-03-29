package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import org.firstinspires.ftc.teamcode.util.statemachine.State;
import org.firstinspires.ftc.teamcode.util.statemachine.StateManager;

public class ParallelStateGroup implements State {

    private volatile StateManager internalStateManager;
    private volatile State[] states;

    public ParallelStateGroup(State... states){
        this.states = states;
        this.internalStateManager = new StateManager();
        this.internalStateManager.addAll(states);
    }

    @Override
    public void init() {

    }

    @Override
    public void run() {
        new Thread(internalStateManager);
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
