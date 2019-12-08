package fun.mooncraftgames.luna.astroitemlib.tags.impl.item;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.UsedContext;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TagSilentItemUseCooldown extends AbstractTag {
    public TagSilentItemUseCooldown(String id, TagPriority priority, ExecutionTypes type) { super(id, priority, type); }

    @Override
    @SuppressWarnings("unchecked")
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_USED) {
            Optional<LocalDateTime> t = AstroItemLib.getCooldownManager().getItemCooldown(itemStack);
            if (t.isPresent()) {
                LocalDateTime now = LocalDateTime.now();
                if(t.get().isBefore(now)){
                    ((UsedContext) context).setCancelled(true);
                    return TagResult.builder().setShouldCancelTags(true).setShouldCancelPostTags(true).build();
                } else {
                    AstroItemLib.getCooldownManager().removeItemCooldown(itemStack);
                }
            }
            String milliseconds = "0.5";
            if(args.length > 0){ milliseconds = args[0]; }
            return TagResult.builder().addPostTags(Pair.of(
                    new Post("cooldown_post", TagPriority.COOLDOWN, ExecutionTypes.ITEM_USED), String.format("cooldown_post:%s", milliseconds))
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
                if(!context.getSharedData().containsKey("failed")){ // Will have failed at one point so a presence check is enough.
                    int milliseconds = 2;
                    if(args.length > 0){ try { milliseconds = Integer.parseInt(args[0]); } catch (Exception ignored) { }}
                    AstroItemLib.getCooldownManager().addItemCooldownSeconds(itemStack, TimeUnit.MILLISECONDS, milliseconds);
                }
            }
            return TagResult.builder().build();
        }
    }
}
