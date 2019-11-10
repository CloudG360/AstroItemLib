package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class HoldContext extends ExecutionContext {

    private ClickInventoryEvent.Held event;

    public HoldContext(Player player, ClickInventoryEvent.Held event) {
        super(player);
        this.event = event;
    }

    public ClickInventoryEvent.Held getEvent() { return event; }
}
