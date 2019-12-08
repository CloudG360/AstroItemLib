package fun.mooncraftgames.luna.astroitemlib.tags.impl.devtest;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
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
    public boolean run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(getType() == ExecutionTypes.ITEM_USED){
            context.getPlayer().getInventory().offer(ItemStack.builder().itemType(ItemTypes.COOKIE).build());
        }
        return true;
    }
}
