package io.cg360.secondmoon.astroitemlib.tags.context.item;

import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class DroppedContext extends ExecutionContext {

    private ClickInventoryEvent.Drop event;

    public DroppedContext(Player player, ClickInventoryEvent.Drop event) {
        super(player);
        this.event = event;
    }

    public ClickInventoryEvent.Drop getEvent() { return event; }
}
