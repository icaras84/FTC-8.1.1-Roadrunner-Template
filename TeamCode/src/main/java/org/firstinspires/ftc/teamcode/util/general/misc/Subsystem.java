package org.firstinspires.ftc.teamcode.util.general.misc;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Subsystem {

    void mapHardware(HardwareMap hardwareMap);

    void init();

    default boolean isFree(){
        return true;
    }
}
