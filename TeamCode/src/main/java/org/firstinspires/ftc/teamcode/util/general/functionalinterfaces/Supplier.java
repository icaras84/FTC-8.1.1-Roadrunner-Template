package org.firstinspires.ftc.teamcode.util.general.functionalinterfaces;

/**
 * Mimics the {@code Supplier} class from the JVM to free it from SDK versions
 * @param <T>
 */
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
