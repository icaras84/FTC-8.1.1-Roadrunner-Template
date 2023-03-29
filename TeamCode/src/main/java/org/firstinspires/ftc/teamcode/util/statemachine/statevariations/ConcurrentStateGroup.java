package org.firstinspires.ftc.teamcode.util.statemachine.statevariations;

import org.firstinspires.ftc.teamcode.util.statemachine.State;

public class ConcurrentStateGroup implements State {

    private State[] states;
    private boolean[] finishMask;

    public ConcurrentStateGroup(State... states){
        this.states = states;
        this.finishMask = new boolean[this.states.length];
    }

    @Override
    public void init() {
        for (State s: states) {
            s.init();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < states.length; i++) {
            if (!finishMask[i]){
                states[i].run();
            }
        }

        for (int i = 0; i < states.length; i++) {
            if (states[i].isFinished()){
                finishMask[i] = true;
            }
        }
    }

    @Override
    public void end() {

    }

    @Override
    public boolean isFinished() {
        boolean output = true;
        for (boolean b: finishMask) {
            if (!b) output = false;
        }
        return output;
    }
}
