package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.DroppedContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class TagUndroppable extends AbstractTag {

    public TagUndroppable(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_DROPPED){
            DroppedContext d = (DroppedContext) context;
            d.getEvent().setCancelled(true);
        }
        return true;
    }
}
