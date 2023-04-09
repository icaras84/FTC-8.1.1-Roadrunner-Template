package org.firstinspires.ftc.teamcode.util.general.servos;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.util.general.misc.GeneralConstants;
import org.firstinspires.ftc.teamcode.util.statemachine.State;

import java.util.LinkedList;

public class ServoEx implements Servo {

    public static class ServoCommand implements State{

        private double deltaTimeMS;
        private double lastCommandedPos, targetPos, delta, rate;

        public ServoCommand(double radiansPerSecond, double target){
            this.rate = radiansPerSecond * GeneralConstants.MS2SEC;
            this.targetPos = target;
        }

        @Override
        public void init() {

        }

        @Override
        public void run() {
            lastCommandedPos += rate * deltaTimeMS;
        }

        @Override
        public void end() {
            lastCommandedPos = targetPos;
        }

        @Override
        public boolean isFinished() {
            this.delta = targetPos - lastCommandedPos;
            return delta < rate * deltaTimeMS;
        }

        public void setLastCommandedPosition(double lastCommandedPosition){
            this.lastCommandedPos = lastCommandedPosition;
            this.delta = targetPos - lastCommandedPos;
            this.rate = Math.copySign(Math.abs(rate), delta);
        }

        public void setDeltaTimeMS(double dt){
            this.deltaTimeMS = dt;
        }

        public double getPos(){
            return lastCommandedPos;
        }
    }

    private double range;
    private Servo internalServo;
    private LinkedList<ServoCommand> commandQueue;
    private ServoCommand currentCommand;
    private boolean initialized;

    public ServoEx(@NonNull HardwareMap hardwareMap, String name, double radianRange){
        this.internalServo = hardwareMap.servo.get(name);
        this.commandQueue = new LinkedList<>();
        this.initialized = false;
        this.range = radianRange;
    }

    public void update(double deltaTimeMs){
        if (!initialized){
            currentCommand = commandQueue.peek();
            currentCommand.init();
            initialized = true;
        }

        currentCommand.setDeltaTimeMS(deltaTimeMs);
        currentCommand.setLastCommandedPosition(getPosition());
        currentCommand.run();

        if (currentCommand.isFinished()){
            currentCommand.end();
            commandQueue.removeFirst();
            initialized = false;
        }

        setPosition(clamp(0, 1, currentCommand.getPos() / range));
    }

    private double clamp(double min, double max, double val){
        return Math.min(max, Math.max(min, val));
    }

    public void queueCommand(ServoCommand command){
        commandQueue.add(command);
    }

    @Override
    public ServoController getController() {
        return internalServo.getController();
    }

    @Override
    public int getPortNumber() {
        return internalServo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        internalServo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return internalServo.getDirection();
    }

    @Override
    public void setPosition(double position) {
        internalServo.setPosition(position);
    }

    @Override
    public double getPosition() {
        return internalServo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        internalServo.scaleRange(min, max);
    }

    @Override
    public Manufacturer getManufacturer() {
        return internalServo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return internalServo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return internalServo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return internalServo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        internalServo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        internalServo.close();
    }
}
