package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.ClickType;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;

public class UsedContext extends ExecutionContext {

    private HandType handType;
    private ClickType clickType;

    /**
     *
     * @param player
     * @param handType
     * @param clickType
     */
    public UsedContext(Player player, HandType handType, ClickType clickType) {
        super(player);
        this.handType = handType;
        this.clickType = clickType;
    }

    public HandType getHandType() { return handType; }
    public ClickType getClickType() { return clickType; }
}
