package fun.mooncraftgames.luna.astroitemlib.items;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class SerializableItemKeys {

    private String spawnable_entity_type;

    private Boolean unbreakable;
    private Boolean hide_unbreakable;

    public SerializableItemKeys setEmpty(){
        this.spawnable_entity_type = "DEFAULT";
        this.unbreakable = false;
        this.hide_unbreakable = false;
        return this;
    }

    public SerializableItemKeys setConfig(String spawnable_entity_type, boolean unbreakable, boolean hide_unbreakable){
        this.spawnable_entity_type = spawnable_entity_type;
        this.unbreakable = unbreakable;
        this.hide_unbreakable = hide_unbreakable;
        this.verifyIntegrity();
        return this;
    }

    public void verifyIntegrity(){
        if(spawnable_entity_type == null) spawnable_entity_type = "DEFAULT";
        if(unbreakable == null) unbreakable = false;
        if(hide_unbreakable == null) hide_unbreakable = false;
    }

    public ItemStack applyKeys(ItemStack item){
        //Make sure to update FixInventoryItems command if new features are added.
        verifyIntegrity();
        if(!spawnable_entity_type.equals("DEFAULT")){
            Optional<EntityType> e = Sponge.getRegistry().getType(EntityType.class, spawnable_entity_type);
            e.ifPresent(entityType -> item.offer(Keys.SPAWNABLE_ENTITY_TYPE, entityType));
        }
        if(unbreakable) item.offer(Keys.UNBREAKABLE, true);
        if(hide_unbreakable) item.offer(Keys.HIDE_UNBREAKABLE, true);
        return item;
    }

    public String getEntityType() { return spawnable_entity_type; }

    public boolean isUnbreakable() { return unbreakable; }
    public boolean getHideUnbreakable() { return hide_unbreakable; }
}
