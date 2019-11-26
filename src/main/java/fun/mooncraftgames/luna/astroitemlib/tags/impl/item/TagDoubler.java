package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.BlockDestroyLootEntry;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockChangeContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Stops an item from breaking a block when used
 */
public class TagDoubler extends AbstractTag {

    public TagDoubler(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.BLOCK_CHANGE){
            BlockChangeContext changeContext = ((BlockChangeContext) context);
            for(String id:changeContext.getBlockChanges().keySet()){
                BlockChangeContext.BlockChange change = changeContext.getBlockChange(id).get();
                if(change.getBlockChangeType() == BlockChangeContext.BlockChangeType.BREAK){
                    List<BlockDestroyLootEntry> loot = change.getDrops();
                    if(loot.size() == 0){ loot.add(new BlockDestroyLootEntry(false)); }
                    loot.addAll(new ArrayList<>(loot));
                }
            }
        }
        return true;
    }
}
