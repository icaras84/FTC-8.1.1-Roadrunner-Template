package org.firstinspires.ftc.teamcode.util.general.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.util.general.opmode.ParameterizedRunnable;
import org.firstinspires.ftc.teamcode.util.statemachine.State;
import org.firstinspires.ftc.teamcode.util.statemachine.statevariations.DefaultState;

public class DSController implements Runnable{

    public static class Analog implements ParameterizedRunnable<Float> {
        public enum FLAG{
            ON_CHANGED,
            ON_MAXED,
            ON_REST
        }
        private float value, lastValue;
        public boolean changed, maxed, rest;

        private State changedState, maxedState, restState;

        public Analog(){
            value = 0;
            lastValue = 0;
            changed = false;

            changedState = DefaultState.inst;
            maxedState = DefaultState.inst;
            restState = DefaultState.inst;
        }

        public void update(float currentValue){
            lastValue = value;
            value = currentValue;

            changed = value - lastValue != 0;
            maxed = Math.abs(value) >= 1;
            rest = value == 0;
        }

        public float getValue(){
            return value;
        }

        public float getLastValue(){
            return lastValue;
        }

        @Override
        public void run(Float inputParameter) {
            update(inputParameter);
        }

        public void bindCondition(FLAG flag, State state){
            switch (flag){
                case ON_REST:
                    this.restState = state;
                    break;
                case ON_MAXED:
                    this.maxedState = state;
                    break;
                case ON_CHANGED:
                    this.changedState = state;
                    break;
            }
        }

        public State getChangedState(){
            return changedState;
        }

        public State getMaxedState(){
            return maxedState;
        }

        public State getRestState(){
            return restState;
        }
    }

    public static class Digital implements ParameterizedRunnable<Boolean>{
        public enum FLAG{
            ON_CHANGED,
            ON_HELD,
            ON_RELEASE,
            ON_PRESS
        }

        private boolean state, lastState;
        public boolean changed, held, released, pressed;

        private State pressedState, releasedState, heldState, changedState;

        public Digital(){}

        public void update(boolean currentState){
            lastState = state;
            state = currentState;

            changed = lastState != state;
            held = state && lastState;
            released = !state && lastState;
            pressed = state && !lastState;

            pressedState = DefaultState.inst;
            releasedState = DefaultState.inst;
            heldState = DefaultState.inst;
            changedState = DefaultState.inst;
        }

        public boolean getState(){
            return state;
        }

        public boolean getLastState(){
            return lastState;
        }

        @Override
        public void run(Boolean inputParameter) {
            update(inputParameter);
        }

        public void bindConditional(FLAG flag, State conditional){
            switch (flag){
                case ON_RELEASE:
                    this.releasedState = conditional;
                    break;
                case ON_HELD:
                    this.heldState = conditional;
                    break;
                case ON_CHANGED:
                    this.changedState = conditional;
                    break;
                case ON_PRESS:
                    this.pressedState = conditional;
                    break;
            }
        }

        public State getChangedState(){
            return changedState;
        }
        public State getReleasedState(){
            return releasedState;
        }

        public State getPressedState(){
            return pressedState;
        }
        public State getHeldState(){
            return heldState;
        }
    }

    private Gamepad controller;

    public Analog
            rightStickX, rightStickY, rightTrigger,
            leftStickX, leftStickY, leftTrigger;

    private Analog[] analogList;

    public Digital
            rightStickButton, leftStickButton,
            rightBumper, leftBumper,
            buttonA, buttonB, buttonX, buttonY,
            dPadUp, dPadDown, dPadLeft, dPadRight,
            start, back, guide;

    private Digital[] digitalList;

    public DSController(Gamepad controller){
        this.controller = controller;

        setupAnalog();
        setupDigital();
    }

    private void setupAnalog(){
        //setup right analog stick and trigger
        rightStickX = new Analog();
        rightStickY = new Analog();
        rightTrigger = new Analog();

        //setup left analog stick and trigger
        leftStickX = new Analog();
        leftStickY = new Analog();
        leftTrigger = new Analog();

        analogList = new Analog[]{
                rightStickX,
                rightStickY,
                rightTrigger,
                leftStickX,
                leftStickY,
                leftTrigger
        };
    }

    private void updateAnalog(){
        //update right analog stick and trigger
        rightStickX.update(controller.right_stick_x);
        rightStickY.update(controller.right_stick_y);
        rightTrigger.update(controller.right_trigger);

        //update left analog stick and trigger
        leftStickX.update(controller.left_stick_x);
        leftStickY.update(controller.left_stick_y);
        leftTrigger.update(controller.left_trigger);
    }

    private void setupDigital(){
        //setup the buttons that are under the analog sticks
        rightStickButton = new Digital();
        leftStickButton = new Digital();

        //setup left and right bumpers
        rightBumper = new Digital();
        leftBumper = new Digital();

        //setup A, B, X, Y buttons on the controller
        buttonA = new Digital();
        buttonB = new Digital();
        buttonX = new Digital();
        buttonY = new Digital();

        //setup D-pad inputs
        dPadUp = new Digital();
        dPadDown = new Digital();
        dPadLeft = new Digital();
        dPadRight = new Digital();

        //setup centralized buttons (start, guide, back)
        start = new Digital();
        guide = new Digital();
        back = new Digital();

        digitalList = new Digital[]{
                rightStickButton,
                leftStickButton,
                rightBumper,
                leftBumper,
                buttonA,
                buttonB,
                buttonX,
                buttonY,
                dPadUp,
                dPadDown,
                dPadLeft,
                dPadRight,
                start,
                guide,
                back
        };
    }

    private void updateDigital(){
        //update the buttons that are under the analog sticks
        rightStickButton.update(controller.right_stick_button);
        leftStickButton.update(controller.left_stick_button);

        //update left and right bumpers
        rightBumper.update(controller.right_bumper);
        leftBumper.update(controller.left_bumper);

        //update A, B, X, Y buttons on the controller
        buttonA.update(controller.a);
        buttonB.update(controller.b);
        buttonX.update(controller.x);
        buttonY.update(controller.y);

        //update D-pad inputs
        dPadUp.update(controller.dpad_up);
        dPadDown.update(controller.dpad_down);
        dPadLeft.update(controller.dpad_left);
        dPadRight.update(controller.dpad_right);

        //update centralized buttons (start, guide, back)
        start.update(controller.start);
        guide.update(controller.guide);
        back.update(controller.back);
    }

    public Analog[] getAnalogList(){
        return analogList;
    }

    public Digital[] getDigitalList(){
        return digitalList;
    }

    @Override
    public void run() {
        updateAnalog();
        updateDigital();
    }
}
