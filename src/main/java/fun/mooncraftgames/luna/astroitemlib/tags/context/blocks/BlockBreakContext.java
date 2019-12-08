package fun.mooncraftgames.luna.astroitemlib.tags.context.blocks;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;

import java.util.HashMap;

public class BlockBreakContext extends ExecutionContext {

    private ChangeBlockEvent.Break event;

    public BlockBreakContext(Player player, HashMap<String, String> sharedData, ChangeBlockEvent.Break event) {
        super(player, sharedData);
        this.event = event;
    }

    public ChangeBlockEvent.Break getEvent() { return event; }
}
