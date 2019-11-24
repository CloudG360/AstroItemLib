package io.cg360.secondmoon.astroitemlib.tags.context.blocks;

import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;

public class BlockInteractContext extends ExecutionContext {

    private BlockSnapshot targetBlock;
    private Direction targetSide;

    private boolean isCancelled;

    public BlockInteractContext(Player player, BlockSnapshot targetBlock, Direction targetSide, boolean isCancelled) {
        super(player);
        this.isCancelled = isCancelled;

        this.targetBlock = targetBlock;
        this.targetSide = targetSide;
    }

    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public BlockSnapshot getTargetBlock() { return targetBlock; }
    public Direction getTargetSide() { return targetSide; }

    public boolean isCancelled() { return isCancelled; }

}
