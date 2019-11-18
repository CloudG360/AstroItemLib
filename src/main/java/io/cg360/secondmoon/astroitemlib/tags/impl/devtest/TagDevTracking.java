package io.cg360.secondmoon.astroitemlib.tags.impl.devtest;

import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;

/**
 * For Testing purposes.
 *
 * When an item is used (Right-Click or Left-Click), an item is given to the player.
 */
public class TagDevTracking extends AbstractTag {


    public TagDevTracking(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_HOLDING){
            context.getPlayer().sendTitle(Title.builder()
                    .actionBar(Text.of(
                            TextColors.DARK_AQUA, TextStyles.BOLD, "Head Yaw: ", TextStyles.RESET, TextColors.AQUA, context.getPlayer().getHeadRotation().getY(),
                            TextColors.DARK_AQUA, TextStyles.BOLD, " Body Yaw: ", TextStyles.RESET, TextColors.AQUA, context.getPlayer().getRotation().getY()
                    )).build());
        }
        return true;
    }
}
