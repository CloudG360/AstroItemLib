package io.cg360.secondmoon.astroitemlib.tags.context.item;

import io.cg360.secondmoon.astroitemlib.tags.ClickType;
import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;

public class UsedContext extends ExecutionContext {

    private HandType handType;
    private ClickType clickType;

    private boolean cancelled;

    /**
     *
     * @param player The player using the item.
     * @param handType The hand the item is held in.
     * @param clickType was the use action a punch or a place?
     */
    public UsedContext(Player player, HandType handType, ClickType clickType) {
        super(player);

        this.handType = handType;
        this.clickType = clickType;
        this.cancelled = false;
    }

    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    /** If true, all events connected with the context are cancelled (Like a block change)*/
    public boolean isCancelled() { return cancelled; }
    public HandType getHandType() { return handType; }
    public ClickType getClickType() { return clickType; }
}
