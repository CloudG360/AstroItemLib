package fun.mooncraftgames.luna.astroitemlib.items;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.data.AstroItemData;
import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import fun.mooncraftgames.luna.astroitemlib.data.impl.AstroItemDataImpl;
import fun.mooncraftgames.luna.astroitemlib.exceptions.InvalidItemException;
import fun.mooncraftgames.luna.astroitemlib.utilities.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemTemplate {

    private String uid;
    private String id;

    private String display_name;
    private String[] lore;
    private String[] astro_tags;

    private SerializableItemKeys keys;
    private SerializableItemEnchantment[] enchantments;

    public ItemTemplate(){
        this.uid = "server:internal";
        this.id = "minecraft:air";
        this.display_name = "IGNORE";
        this.lore = new String[] {"IGNORE"};
        this.astro_tags = new String[] {"IGNORE"};
        this.keys = new SerializableItemKeys().setEmpty();
        this.enchantments = new SerializableItemEnchantment[] { new SerializableItemEnchantment().setEmpty() };
    }

    public ItemTemplate setConfig(String uid, String id, String display_name, String[] lore, SerializableItemKeys keys, SerializableItemEnchantment[] enchantments) {
        this.uid = uid;
        this.id = id;
        this.display_name = display_name;
        this.lore = lore;
        this.keys = keys;
        this.enchantments = enchantments;
        this.verifyIntegrity();
        return this;
    }

    public ItemTemplate setDefault(){
        this.uid = "server:default_item";
        this.id = "minecraft:paper";
        this.display_name = "IGNORE";
        this.lore = new String[] {"IGNORE"};
        this.keys = new SerializableItemKeys().setEmpty();
        this.astro_tags = new String[] {"IGNORE"};
        this.enchantments = new SerializableItemEnchantment[] { new SerializableItemEnchantment().setEmpty() };
        this.verifyIntegrity();
        return this;
    }

    public ItemTemplate setAir(){
        this.id = "minecraft:air";
        this.verifyIntegrity();
        return this;
    }

    public void verifyIntegrity(){
        if(uid == null) this.uid = "DEFAULT";
        if(id == null) this.id = "minecraft:paper";
        if(!getType().isPresent()) throw new InvalidItemException(InvalidItemException.ExceptionType.ITEM_ID, id, "Invalid id specified in an item template");

        if (display_name == null) this.display_name = "IGNORE";
        if (lore == null) this.lore = new String[] {"IGNORE"};
        if (astro_tags == null) this.astro_tags = new String[] {"IGNORE"};

        if (keys == null) this.keys = new SerializableItemKeys().setEmpty();
        if (enchantments == null) this.enchantments = new SerializableItemEnchantment[] { new SerializableItemEnchantment().setEmpty() };

        for(int e = 0; e < enchantments.length; e++){
            if(enchantments[e] == null){
                AstroItemLib.getLogger().warn(String.format("Malformed Enchantment in item: %s [Entry %d]  (Null line; Is there an extra comma?)", uid, e+1));
                enchantments[e] = new SerializableItemEnchantment().setDefault();
            }
        }

        for(int l = 0; l < lore.length; l++){
            if(lore[l] == null){
                AstroItemLib.getLogger().warn(String.format("Malformed Lore in item: %s [Lore Ln %d] (Null line; Is there an extra comma?)", uid, l+1));
                lore[l] = "ERROR";
            }
        }

        List<String> newTags = new ArrayList<>();
        for(int t = 0; t < astro_tags.length; t++){
            if(astro_tags[t] == null){
                AstroItemLib.getLogger().warn(String.format("Malformed Tag in item: %s [Tag Ln %d] (Null line; Is there an extra comma?)", uid, t+1));
                astro_tags[t] = "ERROR";
            } else {
                newTags.add(astro_tags[t]);
            }
        }
        astro_tags = newTags.toArray(new String[0]);
    }

    public ItemStack generateStack(int quantity){
        verifyIntegrity();
        if(!getType().isPresent()) throw new InvalidItemException(InvalidItemException.ExceptionType.ITEM_ID, id, "Invalid id specified in Supply roll of a loot table");
        ItemStack stack = ItemStack.builder().itemType(getType().get()).quantity(quantity).build();
        if(!display_name.equals("IGNORE")) stack.offer(Keys.DISPLAY_NAME, Text.of(Utils.parseToSpongeString(display_name)));
        if(!lore[0].equals("IGNORE")) {
            List<Text> loretext = new ArrayList<>();
            for(String ln: lore){
                loretext.add(Text.of(Utils.parseToSpongeString(ln)));
            }
            stack.offer(Keys.ITEM_LORE, loretext);
        }
        if(!enchantments[0].getId().equals("IGNORE")){
            EnchantmentData data = stack.getOrCreate(EnchantmentData.class).get();
            for(SerializableItemEnchantment e : enchantments) {
                Optional<EnchantmentType> type = Sponge.getRegistry().getType(EnchantmentType.class, e.getId());
                if(!type.isPresent()){
                    AstroItemLib.getLogger().warn("Malformed enchantment ID: "+e.getId());
                    continue;
                }
                data.set(data.enchantments().add(Enchantment.of(type.get(), e.getLevel())));
            }
            stack.offer(data);
        }
        if(!astro_tags[0].equals("IGNORE")){
            AstroItemData astro = stack.get(AstroItemData.class).orElse(new AstroItemDataImpl());
            stack.offer(astro);
            stack.offer(AstroKeys.FUNCTION_TAGS, Arrays.asList(astro_tags));

        }

        return keys.applyKeys(stack);
    }

    public Optional<ItemType> getType () { return Sponge.getRegistry().getType(ItemType.class, id); }
    public String getUniqueID() { return uid; }
    public String getId() { return id; }
    public String getDisplayName() { return display_name; }
    public String[] getLore() { return lore; }
    public SerializableItemEnchantment[] getEnchantments() { return enchantments; }

}
