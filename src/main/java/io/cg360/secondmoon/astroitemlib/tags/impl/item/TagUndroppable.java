package io.cg360.secondmoon.astroitemlib.tags.impl.item;

import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import io.cg360.secondmoon.astroitemlib.tags.data.item.DroppedContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class TagUndroppable extends AbstractTag {

    public TagUndroppable(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_DROPPED){
            DroppedContext d = (DroppedContext) context;
            d.getEvent().setCancelled(true);
        }
        return true;
    }
}
