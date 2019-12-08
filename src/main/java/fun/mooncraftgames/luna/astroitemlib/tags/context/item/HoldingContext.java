package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class HoldingContext extends ExecutionContext {

    private HandType handType;

    public HoldingContext(Player player, HashMap<String, String> sharedData, HandType handType) {
        super(player, sharedData);
        this.handType = handType;
    }

    public HandType getHandType() { return handType; }
}
