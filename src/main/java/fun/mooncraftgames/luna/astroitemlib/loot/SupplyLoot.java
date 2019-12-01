package fun.mooncraftgames.luna.astroitemlib.loot;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.exceptions.MalformedLootPoolException;
import fun.mooncraftgames.luna.astroitemlib.items.ItemTemplate;
import fun.mooncraftgames.luna.astroitemlib.items.SerializableItemEnchantment;
import fun.mooncraftgames.luna.astroitemlib.items.SerializableItemKeys;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.*;

/**
 * (C) Copyright 2019 - Will Scully (CloudGamer360), All rights reserved
 *
 * By using the following application, you are accepting all responsibility
 * for any damage or legal issues which arise over it. Additionally, you are
 * agreeing not to use this application or it's components anywhere other
 * than the Mooncraft Minecraft Server unless you have written permission from
 * the copyright holder.
 *
 *
 * Failure to follow the license will result in a termination of license.
 */
public class SupplyLoot {

    private String id;
    private String title;
    private SupplyRoll[] lootpool;

    private Integer minrolls;
    private Integer maxrolls;

    private transient ItemStack[] lastRoll;

    public ItemStack[] rollLootPool(int result_size){
        verifyIntergirty();

        int rolls = new Random().nextInt((maxrolls - minrolls) + 1) + minrolls;

        int resultSize = result_size;
        if(result_size < 0) resultSize = rolls;

        List<SupplyRoll> currentpool = new ArrayList<>();
        List<SupplyRoll> requiredRolls = new ArrayList<>();

        List<SupplyRoll> inventoryPrs = new ArrayList<>();

        // Populate


        for(SupplyRoll roll : lootpool){
            roll.verifyIntegrity();
            if(roll.getForcePresent()) requiredRolls.add(roll);
            for(int i = 0; i < roll.getWeight(); i++){
                currentpool.add(roll);
            }

        }

        if(requiredRolls.size() > maxrolls) {

            AstroItemLib.getLogger().warn("Loot Pool @ "+id+" | Pool has more than the maximum rolls for required item stacks. Purging any extra items from the pool.");
            Iterator<SupplyRoll> r = requiredRolls.iterator();
            for(int i = 0; i < maxrolls; i++) r.next();
            while (r.hasNext()) {
                SupplyRoll roll = r.next();
                requiredRolls.remove(roll);
            }

            inventoryPrs.addAll(requiredRolls);

        } else {

            for(SupplyRoll roll: requiredRolls){ addRolls(roll, currentpool, inventoryPrs); }
            if(inventoryPrs.size() < rolls) {
                Random random = new Random();
                for (int i = requiredRolls.size(); i < rolls; i++) {
                    SupplyRoll roll = currentpool.get(random.nextInt(currentpool.size()));
                    addRolls(roll, currentpool, inventoryPrs);
                }
            }

        }
        int size = 0;
        List<ItemStack> items = new ArrayList<>();
        for(SupplyRoll r: inventoryPrs){
            if(size < resultSize) {
                ItemStack itemStack = r.getItemStack();
                items.add(itemStack);
                size++;
            }
        }
        if(size < resultSize){
            for(int i = size; i < resultSize; i++){
                items.add(new ItemTemplate().setAir().generateStack(1));
            }
        }
        Collections.shuffle(items);
        ItemStack[] itemSt = items.toArray(new ItemStack[0]);
        lastRoll = itemSt;
        return itemSt;
    }

    public void verifyIntergirty(){
        if(id == null) throw new MalformedLootPoolException("A Loot Pool was registered without an id. Check all Pool files to make sure they have their ID listed in the file.");
        if(lootpool == null) throw new MalformedLootPoolException("Loot Pool @ "+id+"missing content.");

        if(minrolls == null) minrolls = 3;
        if(minrolls < 1) minrolls = 1;

        if(maxrolls == null) maxrolls = 10;
        if(maxrolls < 1) maxrolls = 1;

        if(minrolls > maxrolls) { minrolls = 3; maxrolls = 10; }

    }

    private void addRolls(SupplyRoll roll, List<SupplyRoll> pool, List<SupplyRoll> inventory){
        inventory.add(roll);
        List<SupplyRoll> rollremove = new ArrayList<>();
        rollremove.add(roll);
        int i = 0;
        for(SupplyRoll r : inventory){
            if (r == roll) i++;
        }
        if(i >= roll.getMaxAmount()){
            pool.removeAll(rollremove);
        }
    }

    public SupplyLoot setToDefault(){
        this.id = "default_pool";
        this.title = "&7&lCrate";
        List<SupplyRoll> dr = new ArrayList<>();
        dr.add(new SupplyRoll().setConfig( 100, 1, 1, false, 27,
                new ItemTemplate().setConfig(null, "minecraft:paper", null, null, null,null)));
        dr.add(new SupplyRoll().setConfig( 40, 27, 48, false, 15,
                new ItemTemplate().setConfig(null,"minecraft:dirt", null, null, null,null)));
        dr.add(new SupplyRoll().setConfig(70, 1, 64, false, 15,
                new ItemTemplate().setConfig(null,"minecraft:stone", "&c&lShiny rock", null, null,null)));
        dr.add(new SupplyRoll().setConfig( 1, 1, 1, true, 20,
                new ItemTemplate().setConfig(null,"minecraft:web", null, null, new SerializableItemKeys().setConfig(null, true, false), new SerializableItemEnchantment[]{new SerializableItemEnchantment().setConfig("minecraft:fire_aspect", 7)})));
        this.lootpool = dr.toArray(new SupplyRoll[0]);
        this.minrolls = 1;
        this.maxrolls = 5;
        this.verifyIntergirty();
        return this;
    }

    public ItemStack[] getLastRoll() { return lastRoll; }
    public SupplyRoll[] getLootpool() { return lootpool; }
    public String getId() { return id; }
    public String getTitle() {
        if(title == null) title = "&7&lCrate";
        return title;
    }
    public Integer getMinrolls() { return minrolls; }
    public Integer getMaxrolls() { return maxrolls; }

    public Text getTitleText() { return Text.of(Utils.parseToSpongeString(getTitle())); }
}
