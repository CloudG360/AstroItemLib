package io.cg360.secondmoon.astroitemlib.tags.item.world;

import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockBreakContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

/**
 * Stops an item from breaking a block when used
 */
public class TagStopBreakBlock extends AbstractTag {

    public TagStopBreakBlock(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.BLOCK_BREAK) ((BlockBreakContext) context).getEvent().setCancelled(true);
        return true;
    }
}
