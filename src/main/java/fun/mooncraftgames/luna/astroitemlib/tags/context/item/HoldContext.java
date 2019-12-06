package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class HoldContext extends ExecutionContext {

    private ClickInventoryEvent.Held event;

    public HoldContext(Player player, boolean isAppended, ClickInventoryEvent.Held event) {
        super(player, isAppended);
        this.event = event;
    }

    public ClickInventoryEvent.Held getEvent() { return event; }
}
