package org.firstinspires.ftc.teamcode.util.statemachine;

public interface State {

    final class DEFAULT implements State{

        public DEFAULT(){}

        @Override public void init() {}
        @Override public void run() {}
        @Override public void end() {}

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
