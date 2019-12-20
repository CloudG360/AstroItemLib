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
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.Tristate;

import java.time.LocalDateTime;
import java.util.Optional;

public class TagSilentItemUseCooldown extends AbstractTag {
    public TagSilentItemUseCooldown(String id, TagPriority priority, ExecutionTypes type) { super(id, priority, type); }

    @Override
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_USED) {
            Optional<LocalDateTime> t = AstroItemLib.getCooldownManager().getSilentItemCooldown(Utils.generateItemID(context.getPlayer(), itemStack));
            if (t.isPresent()) {
                LocalDateTime ti = t.get();
                LocalDateTime now = LocalDateTime.now();
                if(now.isBefore(ti)){
                    ((UsedContext) context).setCancelled(true);
                    return TagResult.builder().setShouldCancelTags(Tristate.TRUE).setShouldCancelPostTags(Tristate.TRUE).build();
                } else {
                    AstroItemLib.getCooldownManager().removeSilentItemCooldown(Utils.generateItemID(context.getPlayer(), itemStack));
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
                int milliseconds = 1500;
                if(args.length > 0){ try { milliseconds = Integer.parseInt(args[0]); } catch (Exception ignored) { }}
                AstroItemLib.getCooldownManager().addSilentItemCooldownMillis(Utils.generateItemID(context.getPlayer(), itemStack), milliseconds);
            }
            return TagResult.builder().build();
        }
    }
}
