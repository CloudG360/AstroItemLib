package fun.mooncraftgames.luna.astroitemlib.tags.context.blocks;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class BlockBreakContext extends ExecutionContext {

    private ChangeBlockEvent.Break event;

    public BlockBreakContext(Player player, ChangeBlockEvent.Break event) {
        super(player);
        this.event = event;
    }

    public ChangeBlockEvent.Break getEvent() { return event; }
}
