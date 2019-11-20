package io.cg360.secondmoon.astroitemlib.tags.impl.devtest;

import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

/**
 * For Testing purposes.
 *
 * When an item is used (Right-Click or Left-Click), an item is given to the player.
 */
public class TagDevCookie extends AbstractTag {


    public TagDevCookie(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(getType() == ExecutionTypes.ITEM_USED){
            context.getPlayer().getInventory().offer(ItemStack.builder().itemType(ItemTypes.COOKIE).build());
        }
        return true;
    }
}
