package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
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
