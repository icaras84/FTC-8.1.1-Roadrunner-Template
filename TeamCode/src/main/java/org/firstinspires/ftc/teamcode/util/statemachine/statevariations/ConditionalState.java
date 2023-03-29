package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import android.os.Build;

import org.firstinspires.ftc.teamcode.util.statemachine.State;

import java.util.function.Supplier;

public class ConditionalState implements State {

    private State t, f;
    private Supplier<Boolean> conditional;

    public ConditionalState(State conditionTrue, State conditionFalse, Supplier<Boolean> condition){
        this.t = conditionTrue;
        this.f = conditionFalse;
        this.conditional = condition;
    }

    @Override
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (conditional.get()){
                t.init();
            } else {
                f.init();
            }
        }
    }

    @Override
    public void run() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (conditional.get()){
                t.run();
            } else {
                f.run();
            }
        }
    }

    @Override
    public void end() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (conditional.get()){
                t.end();
            } else {
                f.end();
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (conditional.get()){
                return t.isFinished();
            } else {
                return f.isFinished();
            }
        }
        return true;
    }
}
