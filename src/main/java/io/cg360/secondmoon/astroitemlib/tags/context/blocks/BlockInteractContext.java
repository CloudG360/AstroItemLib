package io.cg360.secondmoon.astroitemlib.tags.context.blocks;

import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;

public class BlockInteractContext extends ExecutionContext {

    private InteractBlockEvent event;

    public BlockInteractContext(Player player, InteractBlockEvent event) {
        super(player);
        this.event = event;
    }

    public InteractBlockEvent getEvent() { return event; }
}
