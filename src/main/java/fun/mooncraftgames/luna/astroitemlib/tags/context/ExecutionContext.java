package fun.mooncraftgames.luna.astroitemlib.tags.context;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Provides ExecutionType specific data for Tag Processing.
 */
public abstract class ExecutionContext {

    private Player player;

    public ExecutionContext(Player player) { this.player = player; }

    public Player getPlayer() { return player; }

    public static class Generic extends ExecutionContext{
        public Generic(Player player) { super(player); }
    }
}
