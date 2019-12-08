package fun.mooncraftgames.luna.astroitemlib.tags.impl.devtest;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
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
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_HOLDING){
            context.getPlayer().sendTitle(Title.builder()
                    .actionBar(Text.of(
                            TextColors.DARK_AQUA, TextStyles.BOLD, "Head Yaw: ", TextStyles.RESET, TextColors.AQUA, (float) context.getPlayer().getHeadRotation().getY(),
                            TextColors.DARK_AQUA, TextStyles.BOLD, " Body Yaw: ", TextStyles.RESET, TextColors.AQUA, (float) context.getPlayer().getRotation().getY()
                    )).build());
        }
        return TagResult.builder().build();
    }
}
