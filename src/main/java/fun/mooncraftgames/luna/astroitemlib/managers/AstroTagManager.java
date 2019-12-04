package fun.mooncraftgames.luna.astroitemlib.managers;

import com.flowpowered.math.vector.Vector3i;
import fun.mooncraftgames.luna.astroforgebridge.AstroForgeBridge;
import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.data.AstroItemData;
import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import fun.mooncraftgames.luna.astroitemlib.data.impl.AstroItemDataImpl;
import fun.mooncraftgames.luna.astroitemlib.loot.SupplyLoot;
import fun.mooncraftgames.luna.astroitemlib.tags.*;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockChangeContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockInteractContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.entities.EntityHitContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.entities.EntityInteractContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.*;
import fun.mooncraftgames.luna.astroitemlib.tasks.RunnableManageContinousTags;
import fun.mooncraftgames.luna.astroitemlib.tasks.interfaces.IAstroTask;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import me.ryanhamshire.griefprevention.api.claim.Claim;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.regex.Pattern;

public class AstroTagManager {

    public static final int PROCESSOR_SIZE = 10000;

    private Map<String, AbstractTag> tagMap;
    private List<UUID> tagProcessors;
    private List<String> ignoreBlockDrops;

    public AstroTagManager(){
        this.tagMap = new HashMap<>();
        this.tagProcessors = new ArrayList<>();
        this.ignoreBlockDrops = new ArrayList<>();
    }

    public AstroTagManager registerTag(AbstractTag tag){
        tagMap.put(tag.getId().split(Pattern.quote(":"))[0].toLowerCase(), tag);
        return this;
    }

    public Optional<AbstractTag> getTag(String tag){
        String t = tag.toLowerCase().split(Pattern.quote(":"))[0];
        return tagMap.containsKey(t) ? Optional.of(tagMap.get(t)) : Optional.empty();
    }

    public List<UUID> getTagProcessorIDs(){ return tagProcessors; }
    public void addTagProcessor(UUID uuid){ tagProcessors.add(uuid); }
    public void removeTagProcessor(UUID uuid){ tagProcessors.remove(uuid); }

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
                boolean added = false;
                for(int i = 0; i < tags.size(); i++){
                    if(tagMap.get(shorttag).getPriority().getIntegerPriority() > tagMap.get(tags.get(i)).getPriority().getIntegerPriority()){
                        tags.add(i, t);
                        added = true;
                        break;
                    }
                }
                if(!added) tags.add(t);
            }
        }
        return tags.toArray(new String[0]);
    }

    public static String[] getTagArguments(String raw_tag){
        String processed = raw_tag.replace( "\\:", "{feature.colon}");
        processed = processed.replace( "\\,", "{feature.comma}");

        String[] chunks = processed.split(Pattern.quote(":"));
        if(chunks.length < 2){ return new String[0]; }
        String[] split = chunks[1].split(Pattern.quote(","));
        String[] finalSplit = new String[split.length];

        for(int i = 0; i < split.length; i++){
            String splitStr = split[i];
            String edit = splitStr.replace( "{feature.colon}", ":");
            edit = edit.replace( "{feature.comma}", ",");
            finalSplit[i] = edit;
        }

        return finalSplit;
    }

    // -----------------------------------------------------------------------------

    @Listener(beforeModifications = true)
    public void onPlayerJoin(ClientConnectionEvent.Join event){
        for(UUID tagP : tagProcessors){
            Optional<IAstroTask> t = AstroItemLib.getTaskManager().getTask(tagP);
            if(t.isPresent()){
                IAstroTask task = t.get();
                if(task instanceof RunnableManageContinousTags){
                    RunnableManageContinousTags processor = (RunnableManageContinousTags) task;
                    if(processor.getPlayers().size() < PROCESSOR_SIZE){
                        processor.addPlayer(event.getTargetEntity().getUniqueId());
                        return;
                    }
                }
            }
        }
        AstroItemLib.getLogger().info("Starting new TagProcessor...");
        AstroItemLib.getTaskManager().registerTask(new RunnableManageContinousTags(1, tagProcessors.size()).addPlayer(event.getTargetEntity().getUniqueId()));

    }


    // -----------------------------------------------------------------------------

    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onGameEvent(Event e, @First Player player){
        if(e instanceof ChangeBlockEvent) return;
        // -- COMMON (Stuff which all levels should use)
        Optional<ItemStackSnapshot> i = e.getContext().get(EventContextKeys.USED_ITEM);
        if(!i.isPresent()) return;
        ItemStackSnapshot istack = i.get();
        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();
        String[] otags = orderedTags(tags.toArray(new String[0]));

        // -- ITEM INTERACTS Level 3 (The top of the foodchain)

        if(e instanceof DamageEntityEvent){
            DamageEntityEvent event = (DamageEntityEvent) e;
            HandType handType = HandTypes.OFF_HAND;
            if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;
            UsedContext usedContext = new UsedContext(player, handType, ClickType.LEFT, event.getTargetEntity().getLocation().getPosition());
            EntityInteractContext interactContext = new EntityInteractContext(player, ClickType.LEFT, event.getTargetEntity(), event.isCancelled());
            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    String[] arguments = getTagArguments(tag);
                    boolean result = true;
                    if(t.getType() == ExecutionTypes.ENTITY_HIT) { result = t.run(ExecutionTypes.ENTITY_HIT, tag, arguments, istack, new EntityHitContext(player, event)); }
                    if(t.getType() == ExecutionTypes.ENTITY_INTERACT) { result = t.run(ExecutionTypes.ENTITY_INTERACT, tag, arguments, istack, interactContext); }
                    if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, arguments, istack, usedContext); }
                    if(!result) return;
                }
            }
            if(usedContext.isCancelled()) event.setCancelled(true);
            if(interactContext.isCancelled()) event.setCancelled(true);
            return;
        }

        // -- ITEM INTERACTS Level 2 (Stuff which are interacts but still encompass other events)

        if(e instanceof InteractEntityEvent){
            InteractEntityEvent event = (InteractEntityEvent) e;
            ClickType clickType = e instanceof InteractEntityEvent.Primary ? ClickType.LEFT : ClickType.RIGHT; // Does not anticipate it being neither.

            HandType handType = HandTypes.OFF_HAND;
            if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

            UsedContext usedContext = new UsedContext(player, handType, ClickType.RIGHT, event.getInteractionPoint().orElse(event.getTargetEntity().getLocation().getPosition()));
            EntityInteractContext interactContext = new EntityInteractContext(player, clickType, event.getTargetEntity(), event.isCancelled());

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    String[] arguments = getTagArguments(tag);
                    boolean result = true;
                    if(t.getType() == ExecutionTypes.ENTITY_INTERACT) { result = t.run(ExecutionTypes.ENTITY_INTERACT, tag, arguments, istack, interactContext); }
                    if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, arguments, istack, usedContext); }
                    if(!result) return;
                }
            }

            if(interactContext.isCancelled()) event.setCancelled(true);
            if(usedContext.isCancelled()) event.setCancelled(true);
            return;
        }

        if(e instanceof InteractBlockEvent){
            InteractBlockEvent event = (InteractBlockEvent) e;
            HandType handType = HandTypes.OFF_HAND;
            if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;

            UsedContext usedContext = new UsedContext(player, handType, ClickType.RIGHT, event.getInteractionPoint().orElse(event.getTargetBlock().getPosition().toDouble().add(0.5d, 0.5d, 0.5d)));
            BlockInteractContext interactContext = new BlockInteractContext(player, event.getTargetBlock(), event.getTargetSide(), event.isCancelled());

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    String[] arguments = getTagArguments(tag);
                    boolean result = true;
                    if(t.getType() == ExecutionTypes.BLOCK_INTERACT) { result = t.run(ExecutionTypes.BLOCK_INTERACT, tag, arguments, istack, interactContext); }
                    if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, arguments, istack, usedContext); }
                    if(!result) return;
                }
            }

            if (usedContext.isCancelled()) event.setCancelled(true);
            if (interactContext.isCancelled()) event.setCancelled(true);
        }

        // -- ITEM INTERACTS Level 1 (Covers all interactions which aren't caught by higher layers)
        if(e instanceof InteractItemEvent) {
            InteractItemEvent event = (InteractItemEvent) e;
            HandType type = e instanceof InteractItemEvent.Primary ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND; // Doesn't anticipate it not being either Primary or Secondary. Potential issue.
            UsedContext usedContext = new UsedContext(player, type, ClickType.LEFT, event.getInteractionPoint().orElse(null));
            for (String tag : otags) {
                if (getTag(tag).isPresent()) {
                    AbstractTag t = getTag(tag).get();
                    String[] arguments = getTagArguments(tag);
                    boolean result = true;
                    if (t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, arguments, istack, usedContext); }
                    if (!result) return;
                }
            }
            if (usedContext.isCancelled()) event.setCancelled(true);
            return;
        }

    }

    /**
     * <h3>Item Hold (OnSelect) Events</h3>
     * Handles the single time trigger called when an item is
     * selected in the hotbar.
     *
     * @param event Internal Sponge supplier of event.
     * @param player Internal Sponge supplier of player.
     */
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
                String[] arguments = getTagArguments(tag);
                boolean result = true;
                if(t.getType() == ExecutionTypes.ITEM_HOLD) { result = t.run(ExecutionTypes.ITEM_HOLD, tag, arguments, istack, new HoldContext(player, event)); }
                if(!result) return;
            }
        }
    }


    /**
     * <h3>Item Click Events</h3>
     * Handles tags which are trigger-able by an InventoryClickEvent
     * or other related events. Does not include support for drop (Above
     * in class)
     *
     * @param event Internal Sponge supplier of event.
     * @param player Internal Sponge supplier of player.
     */
    @Listener(beforeModifications = true, order = Order.DEFAULT)
    public void onInventoryClick(ClickInventoryEvent event, @First Player player){
        if(event instanceof ClickInventoryEvent.Open) return;
        if(event instanceof ClickInventoryEvent.Close) return;
        if(event instanceof ClickInventoryEvent.Held) return;

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

            if(clickType == ClickType.UNKNOWN && state == InventoryChangeStates.NOTHING) return;

            String[] otags = orderedTags(tags.toArray(new String[0]));

            for(String tag: otags){
                if(getTag(tag).isPresent()){
                    AbstractTag t = getTag(tag).get();
                    String[] arguments = getTagArguments(tag);
                    boolean result = true;
                    switch (state) {
                        case NOTHING:
                            if (t.getType() == ExecutionTypes.ITEM_CLICKED) { result = t.run(ExecutionTypes.ITEM_CLICKED, tag, arguments, istack, new ClickedContext(player, event, clickType, isShift)); }
                            break;
                        case DROP:
                            if (t.getType() == ExecutionTypes.ITEM_DROPPED) { result = t.run(ExecutionTypes.ITEM_DROPPED, tag, arguments, istack, new DroppedContext(player, (ClickInventoryEvent.Drop) event)); }
                            break;
                        case PICKUP:
                            if (t.getType() == ExecutionTypes.ITEM_PICKUP) { result = t.run(ExecutionTypes.ITEM_PICKUP, tag, arguments, istack, new PickupContext(player, (ClickInventoryEvent.Pickup) event)); }
                            break;
                    }
                    if(!result) return;
                }
            }
        });
    }

    @Listener(beforeModifications = true, order = Order.DEFAULT) public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player) { onBlockEditCommon(event, player); }

    @Listener(beforeModifications = true, order = Order.DEFAULT) public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player) { onBlockEditCommon(event, player); }

    @Listener(beforeModifications = true, order = Order.LATE)
    public void onBlockItemCatch(DropItemEvent.Destruct event, @First BlockSnapshot blockSnapshot){
        if(ignoreBlockDrops.contains(Utils.generateLocationIDV3i(blockSnapshot.getPosition()))){
            event.setCancelled(true);
            ignoreBlockDrops.remove(Utils.generateLocationIDV3i(blockSnapshot.getPosition()));
        }
    }
    private void onBlockEditCommon(ChangeBlockEvent event, Player player) {
        Optional<ItemStackSnapshot> s = event.getContext().get(EventContextKeys.USED_ITEM);
        if(!s.isPresent()) return;
        ItemStackSnapshot istack = s.get();
        Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
        if(!tgs.isPresent()) return;
        List<String> tags = tgs.get();
        if(tags.size() == 1){ if(tgs.get().get(0).equals("internal.ignored")) return; }
        String[] otags = orderedTags(tags.toArray(new String[0]));
        HandType handType = HandTypes.OFF_HAND;
        if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) handType = player.getItemInHand(HandTypes.MAIN_HAND).get().equalTo(istack.createStack()) ? HandTypes.MAIN_HAND : HandTypes.OFF_HAND;
        ItemStack tool = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.builder().itemType(ItemTypes.AIR).quantity(1).build());
        Optional<BlockRayHit<World>> bRay = BlockRay.from(player).distanceLimit(100).skipFilter( lastHit -> {
                    return lastHit.getLocation().getBlock().getType().equals(BlockTypes.AIR) ||
                            lastHit.getLocation().getBlock().getType().equals(BlockTypes.WATER) ||
                            lastHit.getLocation().getBlock().getType().equals(BlockTypes.LAVA);
        }).stopFilter(BlockRay.allFilter()).end();
        Direction direction = Direction.NONE;
        BlockSnapshot blockHit = event.getTransactions().get(0).getOriginal();
        if(bRay.isPresent()){
            BlockRayHit<World> blockRay = bRay.get();
            direction = blockRay.getFaces()[0];
            blockHit = blockRay.getLocation().getBlock().snapshotFor(blockRay.getLocation());
        }
        ClickType type = event instanceof ChangeBlockEvent.Place ? ClickType.RIGHT : ClickType.LEFT;
        BlockChangeContext changecontext = new BlockChangeContext(player, event.getTransactions(), blockHit, direction);
        UsedContext usedContext = new UsedContext(player, handType, type, event.getTransactions().get(0).getOriginal().getPosition().toDouble().add(0.5d, 0.5d, 0.5d));
        for(String tag: otags){
            if(getTag(tag).isPresent()){
                AbstractTag t = getTag(tag).get();
                String[] arguments = getTagArguments(tag);
                boolean result = true;
                if(t.getType() == ExecutionTypes.BLOCK_CHANGE) { result = t.run(ExecutionTypes.BLOCK_CHANGE, tag, arguments, istack, changecontext); }
                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_USED, tag, arguments, istack, usedContext); }
                if(!result) break;
            }
        }
        if(usedContext.isCancelled()) { event.setCancelled(true); return; }
        if(changecontext.areAllChangesCancelled()) { event.setCancelled(true); return; }
        ArrayList<Transaction<BlockSnapshot>> originalBlockChanges = new ArrayList<>(event.getTransactions());
        for(BlockChangeContext.BlockChange blockChange : changecontext.getBlockChanges().values()){
            if(blockChange.isCancelled()){
                if(blockChange.isOriginalTransaction()){
                    Optional<Transaction<BlockSnapshot>> t = originalBlockChanges.stream().filter(change -> change.getOriginal().getPosition() == blockChange.getOriginalBlock().getPosition()).findFirst();
                    if(!t.isPresent()){ AstroItemLib.getLogger().warn("Uh oh? An original block change is missing? Someone messed up somehow. Skipping..."); continue; }
                    t.get().setValid(false);
                } else { continue; }
            }
            if(blockChange.getBlockChangeType() == BlockChangeContext.BlockChangeType.BREAK){
                ItemStack placeholder = tool.copy();
                AstroItemData adat = placeholder.getOrCreate(AstroItemDataImpl.class).orElse(new AstroItemDataImpl());
                placeholder.offer(adat);
                placeholder.offer(AstroKeys.FUNCTION_TAGS, Collections.singletonList("internal.ignored"));
                Vector3i loc = blockChange.getBlock().getPosition();
                if(blockChange.isVanillaDropsCancelled()) ignoreBlockDrops.add(Utils.generateLocationIDV3i(blockChange.getBlock().getPosition()));
                boolean broken = true;
                if(blockChange.isModified()){
                    broken = digBlock(blockChange, placeholder, player);
                    if(!broken) { if(blockChange.isVanillaDropsCancelled()) ignoreBlockDrops.remove(Utils.generateLocationIDV3i(blockChange.getBlock().getPosition())); }
                }
                Task drop = Task.builder().execute(r -> {
                    for(BlockDestroyLootEntry entry: blockChange.getAdditionalDrops()){
                        switch (entry.getType()){
                            case SUPPLYLOOT:
                                for(ItemStack lootentry: entry.getLootTable().orElse(new SupplyLoot().setToDefault()).rollLootPool(-1)){
                                    Utils.dropItem(blockChange.getBlock().getLocation().get(), lootentry.createSnapshot(), 5);
                                }
                                break;
                            case VANILLADROPS:
                                dropBlock(blockChange, player, placeholder, blockChange.getBlock().getPosition());
                                break;
                            case ITEMSTACKSNAPSHOT:
                                Utils.dropItem(
                                        blockChange.getBlock().getLocation().get(),
                                        entry.getItemStack().orElse(ItemStack.builder()
                                                .itemType(ItemTypes.POISONOUS_POTATO)
                                                .quantity(1)
                                                .build().createSnapshot()),
                                        5);
                                break;
                        }
                    }
                }).name("Drop Handler").delayTicks(2).submit(AstroItemLib.get());
            } else { placeBlock(blockChange, player); }
        }
    }

    private boolean placeBlock(BlockChangeContext.BlockChange blockChange, Player player){
        if(AstroItemLib.getGriefPrevention().isPresent()){
            GriefPreventionApi api = AstroItemLib.getGriefPrevention().get();
            Claim claim = api.getClaimManager(player.getLocation().getExtent()).getClaimAt(blockChange.getBlock().getLocation().get());
            Map<String, Boolean> placeperms = claim.getPermissions(player, claim.getContext());
            AstroItemLib.getLogger().info(Utils.dataToMap(placeperms));
        }
        player.getLocation().getExtent().setBlock(blockChange.getBlock().getPosition(), blockChange.getBlock().getState(), BlockChangeFlags.ALL);
        return true;
    }
    private boolean digBlock(BlockChangeContext.BlockChange blockChange, ItemStack t, Player player){
        //TODO: Remove item data temporarily
        if(AstroItemLib.getGriefPrevention().isPresent()){
            GriefPreventionApi api = AstroItemLib.getGriefPrevention().get();
            Claim claim = api.getClaimManager(player.getWorld()).getClaimAt(blockChange.getBlock().getLocation().get());
            Map<String, Boolean> placeperms = claim.getPermissions(player, claim.getContext());
            AstroItemLib.getLogger().info(Arrays.toString(placeperms.keySet().toArray(new String[0])));
        }
        ItemStack tool = t.copy();
        AstroItemData adat = tool.getOrCreate(AstroItemDataImpl.class).orElse(new AstroItemDataImpl());
        tool.offer(adat);
        tool.offer(AstroKeys.FUNCTION_TAGS, Collections.singletonList("internal.ignored"));
        Vector3i loc = blockChange.getBlock().getPosition();
        return AstroForgeBridge.digBlock(player, tool, loc.getX(), loc.getY(), loc.getZ());
    }
    private void dropBlock(BlockChangeContext.BlockChange blockChange, Player player, ItemStack tool, Vector3i loc){ AstroForgeBridge.dropBlock(player, tool, loc.getX(), loc.getY(), loc.getZ()); }
}
