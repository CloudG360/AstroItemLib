package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.ClickType;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;

public class ClickedContext extends ExecutionContext {

    private ClickInventoryEvent event;
    private ClickType clickType;
    private boolean usedShift;

    public ClickedContext(Player player, ClickInventoryEvent event, ClickType clickType, boolean usedShift) {
        super(player);
        this.event = event;
        this.clickType = clickType;
        this.usedShift = usedShift;
    }

    public ClickInventoryEvent getEvent() { return event; }
    public ClickType getClickType() { return clickType; }
    public boolean isUsedShift() { return usedShift; }
}
