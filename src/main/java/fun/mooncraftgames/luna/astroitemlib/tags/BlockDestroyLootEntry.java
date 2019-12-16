package fun.mooncraftgames.luna.astroitemlib.tags;

import fun.mooncraftgames.luna.astroitemlib.loot.LootPool;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;

public class BlockDestroyLootEntry {
    public enum Type { SUPPLYLOOT, ITEMSTACKSNAPSHOT, VANILLADROPS }

    private Type type;
    private Object data;

    public BlockDestroyLootEntry(LootPool lootTable) { this.type = Type.SUPPLYLOOT;this.data = lootTable; }
    public BlockDestroyLootEntry(ItemStackSnapshot itemStack) { this.type = Type.ITEMSTACKSNAPSHOT; this.data = itemStack; }
    public BlockDestroyLootEntry(boolean isEmpty) {
        // isEmpty is just there to make it less "Oh why aren't drops working" due to an empty constructor. = True is a shortcut for an air stack.
        if(isEmpty) {
            this.type = Type.ITEMSTACKSNAPSHOT;
            this.data = ItemStack.builder().itemType(ItemTypes.AIR).quantity(1).build().createSnapshot();
        } else {
            this.type = Type.VANILLADROPS;
        }
    }

    public Type getType() { return type; }
    public Optional<LootPool> getLootTable() { if(type == Type.SUPPLYLOOT){ return Optional.ofNullable((LootPool) data); } else { return Optional.empty(); } }
    public Optional<ItemStackSnapshot> getItemStack() { if(type == Type.ITEMSTACKSNAPSHOT){ return Optional.ofNullable((ItemStackSnapshot) data); } else { return Optional.empty(); } }
}
