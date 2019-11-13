package io.cg360.secondmoon.astroitemlib.managers;

import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import io.cg360.secondmoon.astroitemlib.data.AstroKeys;
import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ClickType;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.InventoryChangeStates;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockBreakContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockInteractContext;
import io.cg360.secondmoon.astroitemlib.tags.data.blocks.BlockPlaceContext;
import io.cg360.secondmoon.astroitemlib.tags.data.entities.EntityHitContext;
import io.cg360.secondmoon.astroitemlib.tags.data.entities.EntityInteractContext;
import io.cg360.secondmoon.astroitemlib.tags.data.item.*;
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
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
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
        tagMap.put(tag.getId().split(Pattern.quote(":"))[0].toLowerCase(), tag);
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

                    if (i == tags.size()-1){
                        tags.add(t);
                    }
                }
            }
        }
        return tags.toArray(new String[0]);
    }

    @Listener(beforeModifications = true, order = Order.BEFORE_POST)
    public void onUseLeft(InteractItemEvent.Primary event, @First Player player){
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
                    if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, type, ClickType.RIGHT)); }
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
    public void onUseRight(InteractItemEvent.Secondary event, @First Player player){
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
                    if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, type, ClickType.RIGHT)); }
                    if(!result) return;
                }
            }

        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onInventorySingleHold(ClickInventoryEvent.Held event, @First Player player){
        Optional<ItemStack> s = event.getFinalSlot().peek();
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get().createSnapshot();

        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();

        String[] otags = orderedTags(tags.toArray(new String[0]));

        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                boolean result = true;
                if(t.getType() == ExecutionTypes.ITEM_HOLD) { result = t.run(ExecutionTypes.ITEM_HOLD, tag, istack, new HoldContext(player, event)); }
                if(!result) return;
            }
        }
    }

    // Inventory Click Events

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onInventoryClick(ClickInventoryEvent event, @First Player player){
        if(event instanceof ClickInventoryEvent.Open) return;
        if(event instanceof ClickInventoryEvent.Close) return;

        event.getTransactions().forEach(transaction -> {
            ItemStackSnapshot istack = transaction.getOriginal();
            Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
            if(!tgs.isPresent()) return;
            List<String> tags = tgs.get();

            //TODO: Determine ClickType & If shift.
            ClickType clickType = ClickType.UNKNOWN;
            InventoryChangeStates state = InventoryChangeStates.NOTHING;
            boolean isShift = false;

            if(event instanceof ClickInventoryEvent.Primary) clickType = ClickType.LEFT;
            if(event instanceof ClickInventoryEvent.Secondary) clickType = ClickType.RIGHT;
            if(event instanceof ClickInventoryEvent.Middle) clickType = ClickType.MIDDLE;
            if(event instanceof ClickInventoryEvent.Shift) isShift = true;
            if(event instanceof ClickInventoryEvent.Transfer){ clickType = ClickType.QUICK_SWITCH; }
            if(event instanceof ClickInventoryEvent.NumberPress){ clickType = ClickType.QUICK_SWITCH; }
            if(event instanceof ClickInventoryEvent.SwapHand){ clickType = ClickType.QUICK_SWITCH; }
            if(event instanceof ClickInventoryEvent.Drag){ clickType = ClickType.LEFT; isShift = true; }
            if(event instanceof ClickInventoryEvent.Creative) { clickType = ClickType.CREATIVE; }

            if(event instanceof ClickInventoryEvent.Drop){ state = InventoryChangeStates.DROP; }
            if(event instanceof ClickInventoryEvent.Pickup){ state = InventoryChangeStates.PICKUP; }
            if(event instanceof ClickInventoryEvent.Held){ state = InventoryChangeStates.HOLD; }

            if(clickType == ClickType.UNKNOWN && state == InventoryChangeStates.NOTHING) return;

            String[] otags = orderedTags(tags.toArray(new String[0]));

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    boolean result = true;
                    switch (state) {
                        case NOTHING:
                            if (t.getType() == ExecutionTypes.ITEM_CLICKED) { result = t.run(ExecutionTypes.ITEM_CLICKED, tag, istack, new ClickedContext(player, event, clickType, isShift)); }
                            break;
                        case DROP:
                            if (t.getType() == ExecutionTypes.ITEM_DROPPED) { result = t.run(ExecutionTypes.ITEM_DROPPED, tag, istack, new DroppedContext(player, (ClickInventoryEvent.Drop) event)); }
                            break;
                        case PICKUP:
                            if (t.getType() == ExecutionTypes.ITEM_PICKUP) { result = t.run(ExecutionTypes.ITEM_PICKUP, tag, istack, new PickupContext(player, (ClickInventoryEvent.Pickup) event)); }
                            break;
                        case HOLD:
                            if (t.getType() == ExecutionTypes.ITEM_HOLD) { result = t.run(ExecutionTypes.ITEM_HOLD, tag, istack, new HoldContext(player, (ClickInventoryEvent.Held) event)); }
                            break;
                    }
                    if(!result) return;
                }
            }
        });
    }


    // -----


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
                if(t.getType() == ExecutionTypes.ENTITY_HIT) { result = t.run(ExecutionTypes.ENTITY_HIT, tag, istack, new EntityHitContext(player, event)); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, handType, ClickType.RIGHT)); }
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
                if(t.getType() == ExecutionTypes.ENTITY_INTERACT) { result = t.run(ExecutionTypes.ENTITY_INTERACT, tag, istack, new EntityInteractContext(player, event)); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, handType, ClickType.RIGHT)); }
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
                if(t.getType() == ExecutionTypes.BLOCK_INTERACT) { result = t.run(ExecutionTypes.BLOCK_INTERACT, tag, istack, new BlockInteractContext(player, event)); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, handType, ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player){
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
                if(t.getType() == ExecutionTypes.BLOCK_PLACE) { result = t.run(ExecutionTypes.BLOCK_PLACE, tag, istack, new BlockPlaceContext(player, event)); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, handType, ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player){
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
                if(t.getType() == ExecutionTypes.BLOCK_BREAK) { result = t.run(ExecutionTypes.BLOCK_BREAK, tag, istack, new BlockBreakContext(player, event)); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, istack, new UsedContext(player, handType, ClickType.RIGHT)); }
                if(!result) return;
            }
        }
    }

}
