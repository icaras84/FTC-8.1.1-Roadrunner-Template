package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import org.firstinspires.ftc.teamcode.util.statemachine.State;
import org.firstinspires.ftc.teamcode.util.statemachine.StateManager;

public class LoopedState implements State {
    protected int i, loopLimit;

    private State[] cachedStates;
    private StateManager internalStateManager;

    public LoopedState(int loopCount, State... states){
        this.cachedStates = states;
        this.internalStateManager = new StateManager();

        this.loopLimit = loopCount;
    }

    @Override
    public void init() {
        this.i = 0;
        reloadStateStack();
    }

    private void reloadStateStack(){
        this.internalStateManager.addAll(cachedStates);
    }

    @Override
    public void run() {
        if (!isFinished()){
            internalStateManager.run();
            if (internalStateManager.hasNoStates()){
                reloadStateStack();
                i++;
            }
        }
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        return this.i >= this.loopLimit;
    }
}
