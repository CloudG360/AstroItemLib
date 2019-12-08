package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.ClickType;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

import java.util.HashMap;

public class ClickedContext extends ExecutionContext {

    private ClickInventoryEvent event;
    private ClickType clickType;
    private boolean shiftUsed;

    public ClickedContext(Player player, HashMap<String, String> sharedData, ClickInventoryEvent event, ClickType clickType, boolean shiftUsed) {
        super(player, sharedData);
        this.event = event;
        this.clickType = clickType;
        this.shiftUsed = shiftUsed;
    }

    public ClickInventoryEvent getEvent() { return event; }
    public ClickType getClickType() { return clickType; }
    public boolean isShiftUsed() { return shiftUsed; }
}
