package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;

public class HoldingContext extends ExecutionContext {

    private HandType handType;

    public HoldingContext(Player player, HandType handType) {
        super(player);
        this.handType = handType;
    }

    public HandType getHandType() { return handType; }
}
