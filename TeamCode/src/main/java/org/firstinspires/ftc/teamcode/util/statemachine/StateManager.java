package org.firstinspires.ftc.teamcode.util.statemachine;

import org.firstinspires.ftc.teamcode.util.general.misc.GenConstants;

import java.util.Stack;

public class StateManager implements Runnable{

    private Stack<State> stateStack;
    private long startTime;

    private boolean sInit = false;
    private State runningState = null;

    public StateManager(){
        stateStack = new Stack<>();
    }

    public StateManager(StateManager stateManager){
        this.stateStack = (Stack<State>) stateManager.stateStack.clone();
    }

    public StateManager add(State state){
        stateStack.add(0, state);
        return this;
    }

    public StateManager addAll(State... states){
        for (State s: states) {
            add(s);
        }
        return this;
    }

    public void run(){
        if (!stateStack.empty()) { //check if the stack is filled
            if (!sInit) { //test if the current state has initialized (default is false)
                startTime = System.currentTimeMillis();

                runningState = stateStack.peek();
                runningState.init();
                sInit = true; //flag true after running initialization to prevent another init call
            }

            runningState.run();

            if (runningState.isFinished()) { //check if current state is finished
                runningState.end(); //call end() of current state
                stateStack.pop(); //dispose of the state
                sInit = false; //flag that the next state needs to initialize
            }
        } else {
            startTime = System.currentTimeMillis();
        }
    }

    public boolean hasNoStates(){
        return stateStack.empty();
    }

    public long getCurrentStateElapsedTimeMS(){
        return System.currentTimeMillis() - startTime;
    }

    public long getCurrentStateElapsedTimeSEC(){
        return (long) (getCurrentStateElapsedTimeMS() * GenConstants.MS2SEC);
    }

    public long getStartTimeMS(){
        return startTime;
    }

    public long getStartTimeSEC(){
        return (long) (startTime * GenConstants.MS2SEC);
    }
}
