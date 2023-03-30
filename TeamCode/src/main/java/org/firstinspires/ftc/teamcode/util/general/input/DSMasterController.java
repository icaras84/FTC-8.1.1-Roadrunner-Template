package org.firstinspires.ftc.teamcode.util.general.input;

import org.firstinspires.ftc.teamcode.util.general.opmode.EnhancedOpMode;
import org.firstinspires.ftc.teamcode.util.statemachine.State;
import org.firstinspires.ftc.teamcode.util.statemachine.StateManager;
import org.firstinspires.ftc.teamcode.util.statemachine.statevariations.ParallelStateGroup;

import java.util.Vector;

public class DSMasterController implements Runnable{

    public enum Driver{
        DRIVER1,
        DRIVER2
    }

    private EnhancedOpMode opMode;
    private DSController driver1, driver2;

    private final StateManager conditionalManager;

    public DSMasterController(EnhancedOpMode enhancedOpMode){
        this.opMode = enhancedOpMode;

        this.driver1 = new DSController(this.opMode.gamepad1);
        this.driver2 = new DSController(this.opMode.gamepad2);

        this.conditionalManager = new StateManager();
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

    public StateManager getStateManager(){
        return conditionalManager;
    }

    @Override
    public void run() {
        driver1.run();
        driver2.run();

        conditionalManager.addAll(findConditionals(driver1), findConditionals(driver2));
        conditionalManager.run();
    }

    private ParallelStateGroup findConditionals(DSController driver){
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

        return new ParallelStateGroup(driverConditionals.toArray(new State[0]));
    }
}
