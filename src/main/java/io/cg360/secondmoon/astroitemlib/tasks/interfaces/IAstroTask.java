package io.cg360.secondmoon.astroitemlib.tasks.interfaces;

import com.sun.istack.internal.NotNull;

public interface IAstroTask extends IKeyedDataProvider {

    @NotNull String getName();
    @NotNull String getDescription();
    @NotNull boolean isAsync();
    @NotNull void run();
}
