package org.firstinspires.ftc.teamcode.util.general.profiling;

public interface Profile<T extends Profile> {
    T copy();
    String toString();
}
