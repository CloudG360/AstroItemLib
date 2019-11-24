package fun.mooncraftgames.luna.astroitemlib.tasks.interfaces;

import java.util.UUID;

public interface IAstroTask extends IKeyedDataProvider {

    String getName();

    default String getDescription() { return "No description provided. Yee Haw."; }

    boolean isAsync();

    default int getRepeatRate(){ return 0; };
    default int getDelay(){ return 0; };

    void run();

    default void onRegister(UUID uuid) { }
    default void onRecycle(UUID uuid) { }
    default void onUnregister(UUID uuid) { }
}
