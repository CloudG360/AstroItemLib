package fun.mooncraftgames.luna.astroitemlib.tags.impl.world;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockChangeContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class TagUnplaceable  extends AbstractTag {


    public TagUnplaceable(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.BLOCK_CHANGE){
            BlockChangeContext changeContext = ((BlockChangeContext) context);
            for(String id:changeContext.getBlockChanges().keySet()){
                if(changeContext.getBlockChange(id).get().getBlockChangeType() == BlockChangeContext.BlockChangeType.PLACE){
                    changeContext.getBlockChange(id).get().setCancelled(true);
                }
            }
        }
        return true;
    }
}
