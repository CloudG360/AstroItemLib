package fun.mooncraftgames.luna.astroitemlib.tags.context.blocks;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class BlockPlaceContext extends ExecutionContext {

    private ChangeBlockEvent.Place event;

    public BlockPlaceContext(Player player, ChangeBlockEvent.Place event) {
        super(player);
        this.event = event;
    }

    public ChangeBlockEvent.Place getEvent() { return event; }
}
