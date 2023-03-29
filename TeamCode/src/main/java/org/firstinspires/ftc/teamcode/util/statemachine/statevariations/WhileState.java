package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import android.os.Build;

import org.firstinspires.ftc.teamcode.util.statemachine.State;
import org.firstinspires.ftc.teamcode.util.statemachine.StateManager;

import java.util.function.Supplier;

public class WhileState implements State {

    private State[] states;
    private Supplier<Boolean> conditional;
    private StateManager internalStateManager;

    public WhileState(Supplier<Boolean> condition, State... states){
        this.conditional = condition;
        this.states = states;
        this.internalStateManager = new StateManager();
    }

    @Override
    public void init() {
        reloadStateStack();
    }

    @Override
    public void run() {
        if (!isFinished()){
            internalStateManager.run();
            if (internalStateManager.hasNoStates()){
                reloadStateStack();
            }
        }
    }

    private void reloadStateStack(){
        this.internalStateManager.addAll(states.clone());
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return conditional.get();
        }
        return true;
    }
}
