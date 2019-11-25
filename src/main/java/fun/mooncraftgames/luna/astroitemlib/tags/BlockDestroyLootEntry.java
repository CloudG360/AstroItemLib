package fun.mooncraftgames.luna.astroitemlib.tags;

import fun.mooncraftgames.luna.astroitemlib.loot.SupplyLoot;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;

public class BlockDestroyLootEntry {
    public enum Type { SUPPLYLOOT, ITEMSTACKSNAPSHOT, VANILLADROPS }

    private Type type;
    private Object data;

    public BlockDestroyLootEntry(SupplyLoot lootTable) { this.type = Type.SUPPLYLOOT;this.data = lootTable; }
    public BlockDestroyLootEntry(ItemStackSnapshot itemStack) { this.type = Type.ITEMSTACKSNAPSHOT;this.data = itemStack; }
    public BlockDestroyLootEntry() { this.type = Type.VANILLADROPS; }

    public Type getType() { return type; }
    public Optional<SupplyLoot> getLootTable() { if(type == Type.SUPPLYLOOT){ return Optional.ofNullable((SupplyLoot) data); } else { return Optional.empty(); } }
    public Optional<ItemStackSnapshot> getItemStack() { if(type == Type.ITEMSTACKSNAPSHOT){ return Optional.ofNullable((ItemStackSnapshot) data); } else { return Optional.empty(); } }
}
