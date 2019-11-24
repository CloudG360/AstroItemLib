package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.HoldContext;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Random;
import java.util.regex.Pattern;

public class TagButterfingers extends AbstractTag {

    public static final float BUTTERFINGERS_CHANCE  = 0.2f;
    public static final int DEFAULT_TIME = 120;

    public TagButterfingers(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_HOLD){
            AstroItemLib.getLogger().info("BUTTERFINGERS");
            HoldContext d = (HoldContext) context;
            Random random = new Random();
            if(random.nextFloat() <= BUTTERFINGERS_CHANCE){
                int time = DEFAULT_TIME;

                try {
                    String[] params = tag.split(Pattern.quote(":"));
                    if(params.length > 1){
                        time = Integer.parseInt(params[1]);
                    }
                } catch (Exception ignored) {}

                d.getEvent().getFinalSlot().set(ItemStack.builder().itemType(ItemTypes.AIR).build());
                Utils.dropItem(d.getPlayer().getLocation(), itemStack, time);
                d.getPlayer().sendMessage(Text.of(TextColors.YELLOW, "Watch out, ", TextColors.GOLD, TextStyles.BOLD, "BUTTERFINGERS!"));
            }
        }
        return true;
    }
}
