package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

import java.util.HashMap;

public class DroppedContext extends ExecutionContext {

    private ClickInventoryEvent.Drop event;

    public DroppedContext(Player player, HashMap<String, String> sharedData, ClickInventoryEvent.Drop event) {
        super(player, sharedData);
        this.event = event;
    }

    public ClickInventoryEvent.Drop getEvent() { return event; }
}
