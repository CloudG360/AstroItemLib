package fun.mooncraftgames.luna.astroitemlib.tasks;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tasks.interfaces.IAstroTask;
import fun.mooncraftgames.luna.astroitemlib.utilities.HashMapBuilder;

import java.util.HashMap;

public class CooldownGCTask implements IAstroTask {
    @Override public String getName() { return "Cooldown Garbage Collector"; }
    @Override public String getDescription() { return "Clears away cooldowns which have passed but haven't been checked."; }
    @Override public boolean isAsync() { return false; } // Edits a list, probs shouldn't be async
    @Override public int getRepeatRate() { return 24000; } // Every 20 minutes
    @Override public int getDelay() { return 12000; } // Start after 10 minutes.

    private int cycles;

    @Override
    public void run() {
        cycles++;
        for(String stack: AstroItemLib.getCooldownManager().getCooldowns().keySet()){

        }
    }

    @Override
    public HashMap<String, String> toDataStringMap() {
        return HashMapBuilder.builder(String.class, String.class)
                .addField("Cycles", String.valueOf(cycles))
                .addField("Restart needed", (cycles > 48 ? "True" : "False"))
                .build();
    }
}
