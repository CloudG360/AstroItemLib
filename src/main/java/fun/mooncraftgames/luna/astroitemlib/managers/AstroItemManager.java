package fun.mooncraftgames.luna.astroitemlib.managers;

import fun.mooncraftgames.luna.astroitemlib.exceptions.InvalidItemException;
import fun.mooncraftgames.luna.astroitemlib.exceptions.OverwriteDeniedException;
import fun.mooncraftgames.luna.astroitemlib.items.ItemTemplate;
import fun.mooncraftgames.luna.astroitemlib.loot.SupplyLoot;

import java.util.HashMap;
import java.util.Optional;

public class AstroItemManager {

    private HashMap<String, SupplyLoot> lootpools;
    private HashMap<String, ItemTemplate> itemTemplates;


    public AstroItemManager(){
        this.lootpools = new HashMap<>();
        this.itemTemplates = new HashMap<>();
        SupplyLoot loot = new SupplyLoot().setToDefault();
        this.lootpools.put(loot.getId().toLowerCase(), loot);
        ItemTemplate item = new ItemTemplate().setDefault();
        this.itemTemplates.put(item.getUniqueID().toLowerCase(), item);
    }

    // --------------------

    public void registerLootPool(String id, SupplyLoot loot) throws OverwriteDeniedException {
        if(lootpools.containsKey(id.toLowerCase())){
            throw new OverwriteDeniedException(String.format("A loot table was registered with a duplicate id: %s", id.toLowerCase()));
        }
        lootpools.put(id.toLowerCase(), loot);
    }
    public void unregisterLootPool(String id){ lootpools.remove(id.toLowerCase()); }

    // --------------------

    public void registerCustomItem(String id, ItemTemplate item) throws OverwriteDeniedException {
        item.verifyIntegrity();
        if(item.getId().equals("DEFAULT")){
            throw new InvalidItemException(InvalidItemException.ExceptionType.ITEM_ID, id, "No uid field was detected in json definition.");
        }
        if(itemTemplates.containsKey(id.toLowerCase())){
            throw new OverwriteDeniedException(String.format("A custom item was registered with a duplicate id: %s", id.toLowerCase()));
        }
        itemTemplates.put(id.toLowerCase(), item);
    }
    public void unregisterCustomItem(String id){ lootpools.remove(id.toLowerCase()); }

    // --------------------

    public void clearPoolDatabase(){
        lootpools.clear();
        SupplyLoot loot = new SupplyLoot().setToDefault();
        this.lootpools.put(loot.getId().toLowerCase(), loot);
    }

    public void clearItemDatabase(){
        itemTemplates.clear();
        ItemTemplate item = new ItemTemplate().setDefault();
        this.itemTemplates.put(item.getUniqueID().toLowerCase(), item);
    }

    // --------------------

    public Optional<SupplyLoot> getPool (String id){ return lootpools.containsKey(id.toLowerCase())? Optional.of(lootpools.get(id.toLowerCase())) : Optional.empty(); }
    public Optional<ItemTemplate> getItem (String id){ return itemTemplates.containsKey(id.toLowerCase())? Optional.of(itemTemplates.get(id.toLowerCase())) : Optional.empty(); }
    public String[] getPools() {
        return lootpools.keySet().toArray(new String[0]);
    }
    public String[] getItems() {
        return itemTemplates.keySet().toArray(new String[0]);
    }
}
