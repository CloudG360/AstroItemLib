package io.cg360.secondmoon.astroitemlib.tasks.interfaces;

import com.sun.istack.internal.NotNull;

import java.util.UUID;

public interface IAstroTask extends IKeyedDataProvider {

    @NotNull String getName();
    @NotNull default String getDescription() { return "No description provided. Yee Haw."; }

    boolean isAsync();

    default int getRepeatRate(){ return 0; };
    default int getDelay(){ return 0; };

    void run();

    default void onRegister(UUID uuid) { }
    default void onRecycle(UUID uuid) { }
    default void onUnregister(UUID uuid) { }
}
