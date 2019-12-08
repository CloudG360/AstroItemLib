package fun.mooncraftgames.luna.astroitemlib.tags.context.blocks;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

import java.util.HashMap;

public class BlockInteractContext extends ExecutionContext {

    private BlockSnapshot targetBlock;
    private Direction targetSide;

    private boolean isCancelled;

    public BlockInteractContext(Player player, HashMap<String, String> sharedData, BlockSnapshot targetBlock, Direction targetSide, boolean isCancelled) {
        super(player, sharedData);
        this.isCancelled = isCancelled;

        this.targetBlock = targetBlock;
        this.targetSide = targetSide;
    }

    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public BlockSnapshot getTargetBlock() { return targetBlock; }
    public Direction getTargetSide() { return targetSide; }

    public boolean isCancelled() { return isCancelled; }

}
