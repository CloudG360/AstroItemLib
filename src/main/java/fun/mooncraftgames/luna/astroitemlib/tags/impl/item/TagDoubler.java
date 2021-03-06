package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.tags.*;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockChangeContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;

/**
 * Stops an item from breaking a block when used
 */
public class TagDoubler extends AbstractTag {

    public TagDoubler(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.BLOCK_CHANGE){
            BlockChangeContext changeContext = ((BlockChangeContext) context);
            for(String id:changeContext.getBlockChanges().keySet()){
                BlockChangeContext.BlockChange change = changeContext.getBlockChange(id).get();
                if(change.getBlockChangeType() == BlockChangeContext.BlockChangeType.BREAK){
                    ArrayList<BlockDestroyLootEntry> loot = change.getAdditionalDrops();
                    if(loot.size() != 0) { loot.addAll(new ArrayList<>(loot)); }
                    loot.add(new BlockDestroyLootEntry(false));
                    change.setAdditionalDrops(loot);
                }
            }
        }
        return TagResult.builder().build();
    }
}
