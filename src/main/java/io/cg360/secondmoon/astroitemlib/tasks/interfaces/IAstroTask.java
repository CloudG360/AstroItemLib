package io.cg360.secondmoon.astroitemlib.tasks.interfaces;

import com.sun.istack.internal.NotNull;

public interface IAstroTask extends IKeyedDataProvider {

    @NotNull String getName();
    @NotNull String getDescription();
    @NotNull boolean isAsync();
    @NotNull default int getRepeatRate(){ return 0; };
    @NotNull default int getDelay(){ return 0; };
    @NotNull void run();
}
