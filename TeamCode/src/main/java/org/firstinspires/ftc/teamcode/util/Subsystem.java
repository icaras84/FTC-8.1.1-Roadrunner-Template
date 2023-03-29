package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Subsystem {

    void mapHardware(HardwareMap hardwareMap);

    void init();

    default boolean isFree(){
        return true;
    }
}
