package fun.mooncraftgames.luna.astroitemlib.tags.context;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Provides ExecutionType specific data for Tag Processing.
 */
public class ExecutionContext {

    private Player player;
    private boolean isAppended;

    public ExecutionContext(Player player, boolean isAppended) { this.player = player; this.isAppended = isAppended; }

    public Player getPlayer() { return player; }
    public boolean isAppended() { return isAppended; }
}
