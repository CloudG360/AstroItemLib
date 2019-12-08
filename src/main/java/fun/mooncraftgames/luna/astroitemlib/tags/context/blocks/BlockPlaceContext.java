package fun.mooncraftgames.luna.astroitemlib.tags.context.blocks;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;

import java.util.HashMap;

public class BlockPlaceContext extends ExecutionContext {

    private ChangeBlockEvent.Place event;

    public BlockPlaceContext(Player player, HashMap<String, String> sharedData, ChangeBlockEvent.Place event) {
        super(player, sharedData);
        this.event = event;
    }

    public ChangeBlockEvent.Place getEvent() { return event; }
}
