package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.UsedContext;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Tristate;

import java.time.LocalDateTime;
import java.util.Optional;

public class TagItemUseCooldown extends AbstractTag {
    public TagItemUseCooldown(String id, TagPriority priority, ExecutionTypes type) { super(id, priority, type); }


    @Override
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_USED) {
            Optional<LocalDateTime> t = AstroItemLib.getCooldownManager().getItemCooldown(Utils.generateItemID(context.getPlayer(), itemStack));
            if (t.isPresent()) {
                LocalDateTime ti = t.get();
                LocalDateTime now = LocalDateTime.now();
                if(now.isBefore(ti)){
                    context.getPlayer().sendTitle(Title.builder().reset().build());
                    context.getPlayer().sendTitle(Title.builder()
                            .actionBar(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "You must wait ", Utils.compareDateTimes(now, ti), " before you can use this."))
                            .build());
                    context.getPlayer().playSound(SoundTypes.ENTITY_WITHER_SPAWN, context.getPlayer().getPosition(), 1d, 0.7d, 0.7d);
                    ((UsedContext) context).setCancelled(true);
                    return TagResult.builder().setShouldCancelTags(Tristate.TRUE).setShouldCancelPostTags(Tristate.TRUE).build();
                } else {
                    AstroItemLib.getCooldownManager().removeItemCooldown(Utils.generateItemID(context.getPlayer(), itemStack));
                }
            }
            String milliseconds = "1500";
            if(args.length > 0){ milliseconds = args[0]; }
            return TagResult.builder().addPostTags(Pair.of(
                    new Post("s_cooldown_post", TagPriority.COOLDOWN, ExecutionTypes.ITEM_USED), String.format("s_cooldown_post:%s", milliseconds))
            ).build();
        }
        return TagResult.builder().build();
    }

    public static class Post extends AbstractTag {

        public Post(String id, TagPriority priority, ExecutionTypes type) {
            super(id, priority, type);
        }

        @Override
        public TagResult run(ExecutionTypes type, String fullTag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
            if(type == ExecutionTypes.ITEM_USED && isAppended){
                if(context.getSharedData().containsKey("failure")) {
                    int milliseconds = 1500;
                    if (args.length > 0) { try { milliseconds = Integer.parseInt(args[0]); } catch (Exception ignored) { } }
                    AstroItemLib.getCooldownManager().addItemCooldownMillis(Utils.generateItemID(context.getPlayer(), itemStack), milliseconds);
                }
            }
            return TagResult.builder().build();
        }
    }
}
