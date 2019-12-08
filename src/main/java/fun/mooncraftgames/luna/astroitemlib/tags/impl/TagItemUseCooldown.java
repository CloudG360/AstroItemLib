package fun.mooncraftgames.luna.astroitemlib.tags.impl;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TagItemUseCooldown extends AbstractTag {
    public TagItemUseCooldown(String id, TagPriority priority, ExecutionTypes type) { super(id, priority, type); }

    @Override
    public TagResult run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context) {
        if(type == ExecutionTypes.ITEM_USED) {
            Optional<LocalDateTime> t = AstroItemLib.getCooldownManager().getItemCooldown(itemStack);
            if (t.isPresent()) {
                LocalDateTime now = LocalDateTime.now();
                if(t.get().isBefore(now)){
                    context.getPlayer().sendTitle(Title.builder().reset().build());
                    context.getPlayer().sendTitle(Title.builder()
                            .actionBar(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "COOLDOWN ", TextStyles.RESET, TextColors.RED, "You must wait", Utils.compareDateTimes(now, t.get())))
                            .build());
                    context.getPlayer().playSound(SoundTypes.ENTITY_WITHER_SPAWN, context.getPlayer().getPosition(), 1d, 0.7d, 0.7d);
                    return TagResult.builder().setShouldCancelTags(true).build();
                } else {
                    AstroItemLib.getCooldownManager().removeItemCooldown(itemStack);
                    int seconds = 30;
                    if(args.length > 0){ try { seconds = Integer.parseInt(args[0]); } catch (Exception ignored) { }}
                    AstroItemLib.getCooldownManager().addItemCooldownSeconds(itemStack, TimeUnit.SECONDS, seconds);
                    return true;
                    //TODO: Check for failures before applying times. Same should be done with applying XPCosts (And then doing a required thing)
                }
            } else {
                int seconds = 30;
                if(args.length > 0){ try { seconds = Integer.parseInt(args[0]); } catch (Exception ignored) { }}
                AstroItemLib.getCooldownManager().addItemCooldownSeconds(itemStack, TimeUnit.SECONDS, seconds);
                return true;
            }
        }
        return TagResult.builder().build();
    }
}
