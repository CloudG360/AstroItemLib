package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class PickupContext extends ExecutionContext {

    private ClickInventoryEvent.Pickup event;

    public PickupContext(Player player, boolean isAppended, ClickInventoryEvent.Pickup event) {
        super(player, isAppended);
        this.event = event;
    }

    public ClickInventoryEvent.Pickup getEvent() { return event; }
}
