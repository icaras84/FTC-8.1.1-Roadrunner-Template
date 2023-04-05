package org.firstinspires.ftc.teamcode.util.general.input;

import org.firstinspires.ftc.teamcode.util.general.opmode.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.statemachine.State;

import java.util.Vector;

public class DSMasterController implements Runnable{

    public enum Driver{
        DRIVER1,
        DRIVER2
    }

    private EnhancedOpMode opMode;
    private DSController driver1, driver2;

    private final State.Sequence stateManager;

    public DSMasterController(EnhancedOpMode enhancedOpMode){
        this.opMode = enhancedOpMode;

        this.driver1 = new DSController(this.opMode.gamepad1);
        this.driver2 = new DSController(this.opMode.gamepad2);

        this.stateManager = new State.Sequence();
    }

    public DSController getController(Driver driver){
        if (driver == Driver.DRIVER2) {
            return driver2;
        }
        return driver1;
    }

    public DSController getDriver1(){
        return driver1;
    }

    public DSController getDriver2(){
        return driver2;
    }

    public State.Sequence getStateManager(){
        return stateManager;
    }

    @Override
    public void run() {
        driver1.run();
        driver2.run();

        stateManager.addAll(findConditionals(driver1), findConditionals(driver2));
        stateManager.run();
    }

    private State.ParallelAsyncGroup findConditionals(DSController driver){
        Vector<State> driverConditionals = new Vector<>();
        for (DSController.Analog analog: driver.getAnalogList()) {
            if (analog.rest) driverConditionals.add(analog.getRestState());
            if (analog.maxed) driverConditionals.add(analog.getMaxedState());
            if (analog.changed) driverConditionals.add(analog.getChangedState());
        }

        for (DSController.Digital digital: driver.getDigitalList()) {
            if (digital.changed) driverConditionals.add(digital.getChangedState());
            if (digital.held) driverConditionals.add(digital.getHeldState());
            if (digital.released) driverConditionals.add(digital.getReleasedState());
            if (digital.pressed) driverConditionals.add(digital.getPressedState());
        }

        return new State.ParallelAsyncGroup(driverConditionals.toArray(new State[0]));
    }
}
