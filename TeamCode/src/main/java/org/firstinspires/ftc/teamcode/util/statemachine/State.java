package org.firstinspires.ftc.teamcode.util.statemachine;

import android.os.Build;

import java.util.function.Supplier;

public interface State {

    final class Empty implements State{

        public Empty(){}

        @Override public void init() {}
        @Override public void run() {}
        @Override public void end() {}

        @Override
        public boolean isFinished() {
            return true;
        }
    }
    class Wait implements State{
        private long durationMS;

        private long lastTime = 0L, currTime = 0L, deltaTime = 0L, projectedTime = 0L;
        public Wait(long ms){
            this.durationMS = ms;
        }

        @Override
        public void init() {
            lastTime = System.currentTimeMillis();
            currTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            currTime = System.currentTimeMillis();
            deltaTime = currTime - lastTime;
            projectedTime = currTime + deltaTime;
            lastTime = currTime;
        }

        @Override
        public void end() {

        }

        @Override
        public boolean isFinished() {
            return currTime + (0.02 * deltaTime) >= durationMS;
        }

        public long getDeltaTimeMS(){
            return this.deltaTime;
        }

        public long getProjectedTimeMS(){
            return this.projectedTime;
        }
    }
    class WaitFor<T extends Flag<?>> implements State{
        private T monitoredFlag;
        private T targetFlag;
        private boolean exit;
        public WaitFor(T flag, T target){
            this.monitoredFlag = flag;
            this.targetFlag = target;
        }

        @Override
        public void init() {}

        @Override
        public void run() {
            this.exit = monitoredFlag.getCurrentState().equals(targetFlag);
        }

        @Override
        public void end() {}

        @Override
        public boolean isFinished() {
            return this.exit;
        }
    }
    class If implements State {

        private State t, f;
        private Supplier<Boolean> conditional;

        public If(Supplier<Boolean> condition, State conditionTrue, State conditionFalse){
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
    class While implements State {

        private State[] states;
        private Supplier<Boolean> conditional;
        private StateManager internalStateManager;

        public While(Supplier<Boolean> condition, State... states){
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (conditional.get()){
                    internalStateManager.run();
                    if (internalStateManager.hasNoStates()){
                        reloadStateStack();
                    }
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
                return !conditional.get();
            }
            return true;
        }
    }
    class For implements State {
        private int i, loopLimit;

        private State[] cachedStates;
        private StateManager internalStateManager;

        public For(int loopCount, State... states){
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
            this.internalStateManager.addAll(this.cachedStates);
        }

        @Override
        public void run() {
            if (!isFinished()){
                internalStateManager.run();
                if (internalStateManager.hasNoStates()){
                    reloadStateStack();
                    this.i++;
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

        public int getI(){
            return this.i;
        }
    }
    class AsyncGroup implements State {

        private State[] states;
        private boolean[] finishMask;

        public AsyncGroup(State... states){
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
    class ParallelAsyncGroup implements State {

        private volatile StateManager internalStateManager;
        private volatile State[] states;

        public ParallelAsyncGroup(State... states){
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

    void init();
    void run();
    void end();
    boolean isFinished();
}
