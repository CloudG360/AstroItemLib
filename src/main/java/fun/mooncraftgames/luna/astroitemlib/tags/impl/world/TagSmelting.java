package fun.mooncraftgames.luna.astroitemlib.tags.impl.world;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.BlockDestroyLootEntry;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagPriority;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.blocks.BlockChangeContext;
import fun.mooncraftgames.luna.astroitemlib.utilities.HashMapBuilder;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Stops an item from breaking a block when used
 */
public class TagSmelting extends AbstractTag {

    private static final HashMap<BlockType, BlockDestroyLootEntry[]> SMELT_ORES =
            HashMapBuilder.builder(BlockType.class, BlockDestroyLootEntry[].class)
                    .addField(BlockTypes.IRON_ORE, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.IRON_INGOT).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.GOLD_ORE, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.GOLD_INGOT).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.CLAY, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.BRICK).quantity(3).build().createSnapshot()) })
                    .addField(BlockTypes.STONE, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.STONE).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.COBBLESTONE, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.STONE).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.SAND, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.GLASS).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.NETHERRACK, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.NETHERBRICK).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.LOG, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.COAL_BLOCK).quantity(1).build().createSnapshot()) })
                    .addField(BlockTypes.LOG2, new BlockDestroyLootEntry[]{ new BlockDestroyLootEntry(ItemStack.builder().itemType(ItemTypes.COAL_BLOCK).quantity(1).build().createSnapshot()) })
            .build();

    public TagSmelting(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, String[] args, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(type == ExecutionTypes.BLOCK_CHANGE){
            BlockChangeContext changeContext = ((BlockChangeContext) context);
            for(String id:changeContext.getBlockChanges().keySet()){
                if(changeContext.getBlockChange(id).get().getBlockChangeType() == BlockChangeContext.BlockChangeType.BREAK){
                    if(Arrays.asList(SMELT_ORES.keySet().toArray(new BlockType[0])).contains(changeContext.getBlockChange(id).get().getBlock().getState().getType())){
                        changeContext.getBlockChange(id).get().setVanillaDropsCancelled(true);

                        changeContext.getBlockChange(id).get().setAdditionalDrops(new ArrayList<>(Arrays.asList(SMELT_ORES.get(changeContext.getBlockChange(id).get().getBlock().getState().getType()))));
                    }
                }
            }
        } //TODO: Add a detection for entity hit for smelting food drops.
        return true;
    }
}
