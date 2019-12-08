package fun.mooncraftgames.luna.astroitemlib.tags.context;

import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

/**
 * Provides ExecutionType specific data for Tag Processing.
 */
public abstract class ExecutionContext {

    private Player player;
    private HashMap<String, String> sharedData;

    public ExecutionContext(Player player, HashMap<String, String> sharedData) { this.player = player; }

    public Player getPlayer() { return player; }

    public static class Generic extends ExecutionContext{
        public Generic(Player player, HashMap<String, String> sharedData) { super(player, sharedData); }
    }
}
