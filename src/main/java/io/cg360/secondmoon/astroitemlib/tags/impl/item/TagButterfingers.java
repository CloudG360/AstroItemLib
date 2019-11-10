package io.cg360.secondmoon.astroitemlib.tags.impl.item;

import io.cg360.secondmoon.astroitemlib.Utils;
import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import io.cg360.secondmoon.astroitemlib.tags.data.item.HoldContext;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Random;

public class TagButterfingers extends AbstractTag {

    public static final float BUTTERFINGERS_CHANCE  = 0.2f;

    public TagButterfingers(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_HOLD){
            HoldContext d = (HoldContext) context;
            Random random = new Random();
            if(random.nextFloat() <= BUTTERFINGERS_CHANCE){
                d.getEvent().getFinalSlot().set(ItemStack.builder().itemType(ItemTypes.AIR).build());
                Utils.dropItem(d.getPlayer(), itemStack);
                d.getPlayer().sendMessage(Text.of(TextColors.YELLOW, "Watch out, ", TextColors.GOLD, TextStyles.BOLD, "BUTTERFINGERS!"));
            }
        }
        return true;
    }
}
