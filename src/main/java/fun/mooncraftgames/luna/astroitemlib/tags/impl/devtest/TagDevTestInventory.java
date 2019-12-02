package fun.mooncraftgames.luna.astroitemlib.tags.impl.devtest;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.ClickedContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

/**
 * For Testing purposes.
 *
 * When an item is used (Right-Click or Left-Click), an item is given to the player.
 */
public class TagDevTestInventory extends AbstractTag {


    public TagDevTestInventory(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(getType() == ExecutionTypes.ITEM_CLICKED){
            ClickedContext c = (ClickedContext) context;
            c.getPlayer().sendMessage(Text.of(TextColors.DARK_AQUA, TextStyles.BOLD, "UI ", TextStyles.RESET, TextColors.AQUA, String.format("Click Type: %s, IsShift: %s", c.getClickType().toString(), c.isShiftUsed())));
            c.getEvent().setCancelled(true);
        }
        return true;
    }
}
