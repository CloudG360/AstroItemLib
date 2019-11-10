package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class PickupContext extends ExecutionContext {

    private ClickInventoryEvent.Pickup event;

    public PickupContext(Player player, ClickInventoryEvent.Pickup event) {
        super(player);
        this.event = event;
    }

    public ClickInventoryEvent.Pickup getEvent() { return event; }
}
