package io.cg360.secondmoon.astroitemlib.managers;

import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import io.cg360.secondmoon.astroitemlib.data.AstroKeys;
import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.data.UsedContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockBreakContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockInteractContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockPlaceContext;
import io.cg360.secondmoon.astroitemlib.tags.data.entities.EntityHitContext;
import io.cg360.secondmoon.astroitemlib.tags.data.entities.EntityInteractContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.*;
import java.util.regex.Pattern;

public class AstroTagManager {

    private Map<String, AbstractTag> tagMap;

    private boolean overrideLeftClick;
    private boolean overrideRightClick;

    public AstroTagManager(){
        this.tagMap = new HashMap<>();
        overrideLeftClick = false;
        overrideRightClick = false;
    }

    public AstroTagManager registerTag(AbstractTag tag){
        tagMap.put(tag.getId(), tag);
        return this;
    }

    public Optional<AbstractTag> getTag(String tag){
        String t = tag.toLowerCase().split(Pattern.quote(":"))[0];
        return tagMap.containsKey(t) ? Optional.of(tagMap.get(t)) : Optional.empty();
    }

    /**
     * Takes in tags from an item and gives them a process order
     * based on priority. The order of tags does affect priority as
     * well where if two tags are equal priority, the one which comes
     * first gets to execute first out of the two.
     *
     * @param tagsIn - Unfiltered/Unmodified tags from an item
     * @return Ordered list of tags.
     */
    public String[] orderedTags(String[] tagsIn){
        List<String> tags = new ArrayList<>();
        for(String t:tagsIn){
            String shorttag = t.toLowerCase().split(Pattern.quote(":"))[0];
            if(!tagMap.containsKey(shorttag)){
                AstroItemLib.getLogger().warn(String.format("<@Data> An item uses an unregistered/unrecognised tag: %s | Is a plugin missing?", shorttag));
                continue;
            }
            if(tags.size() == 0){
                tags.add(t);
            } else {
                for(int i = 0; i < tags.size(); i++){
                    if(tagMap.get(shorttag).getPriority().getIntegerPriority() > tagMap.get(tags.get(i)).getPriority().getIntegerPriority()){
                        tags.add(i, t);
                        break;
                    }
                }
            }
        }
        return tags.toArray(new String[0]);
    }

    @Listener(beforeModifications = true, order = Order.BEFORE_POST)
    public void onUseLeftEvent(InteractItemEvent.Primary event, @First Player player){
        if(overrideLeftClick) {
            overrideLeftClick = false;
            return;
        }
        Optional<List<String>> tgs = event.getItemStack().get(AstroKeys.FUNCTION_TAGS);
        if(tgs.isPresent()){
            List<String> tags = tgs.get();
            HandType type = event.getHandType();
            ItemStackSnapshot istack = event.getItemStack();

            String[] otags = orderedTags(tags.toArray(new String[0]));

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    boolean result = true;
                    if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, type, UsedContext.ClickType.RIGHT)); }
                    if(!result) return;
                }
            }
            /*Task a_task = Task.builder()
                    .execute(runnable -> {
                        for(String tag: otags){
                            boolean result;
                            Task sync = Task.builder().execute(s_task -> {
                                getTag(tag).ifPresent(prs ->{
                                    prs.run(tag, istack, context);
                                });}
                            ).name("TagSync#"+tag).submit(AstroItemLib.get());
                        }
                    }).async().name("TagProcessor - InteractItem.Primary").submit(AstroItemLib.get());

             */

        }
    }

    @Listener(beforeModifications = true, order = Order.BEFORE_POST)
    public void onUseRightEvent(InteractItemEvent.Secondary event, @First Player player){
        if(overrideRightClick) {
            overrideRightClick = false;
            return;
        }
        Optional<List<String>> tgs = event.getItemStack().get(AstroKeys.FUNCTION_TAGS);
        if(tgs.isPresent()){
            List<String> tags = tgs.get();
            HandType type = event.getHandType();
            ItemStackSnapshot istack = event.getItemStack();

            String[] otags = orderedTags(tags.toArray(new String[0]));

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    boolean result = true;
                    if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, type, UsedContext.ClickType.RIGHT)); }

                    if(!result) return;
                }
            }

        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onEntityHit(DamageEntityEvent event, @First Player player){
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        overrideLeftClick = true;

        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;


        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.HIT) { result = t.run(ExecutionTypes.HIT, tag, istack, new EntityHitContext(player, event)); }
                if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, handType, UsedContext.ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onEntityInteract(InteractEntityEvent event, @First Player player){
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        overrideRightClick = true;

        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.INTERACT_ENTITY) { result = t.run(ExecutionTypes.INTERACT_ENTITY, tag, istack, new EntityInteractContext(player, event)); }
                if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, handType, UsedContext.ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onBlockInteract(InteractBlockEvent event, @First Player player){
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        overrideRightClick = true;

        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.INTERACT_BLOCK) { result = t.run(ExecutionTypes.INTERACT_BLOCK, tag, istack, new BlockInteractContext(player, event)); }
                if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, handType, UsedContext.ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void blockPlaceEvent(ChangeBlockEvent.Place event, @First Player player){
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        overrideRightClick = true;

        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.PLACE_BLOCK) { result = t.run(ExecutionTypes.PLACE_BLOCK, tag, istack, new BlockPlaceContext(player, event)); }
                if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, handType, UsedContext.ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void blockBreakEvent(ChangeBlockEvent.Break event, @First Player player){
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        overrideLeftClick = true;

        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.BREAK_BLOCK) { result = t.run(ExecutionTypes.BREAK_BLOCK, tag, istack, new BlockBreakContext(player, event)); }
                if(t.getType() == ExecutionTypes.USED) { result = t.run(ExecutionTypes.USED, tag, istack, new UsedContext(player, handType, UsedContext.ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

}
