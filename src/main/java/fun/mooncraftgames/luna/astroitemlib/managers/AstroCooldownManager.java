package fun.mooncraftgames.luna.astroitemlib.managers;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tasks.CooldownGCTask;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AstroCooldownManager {

    private HashMap<ItemStackSnapshot, LocalDateTime> cooldowns;
    public AstroCooldownManager(){
        this.cooldowns = new HashMap<>();
    }

    public void addItemCooldown(ItemStackSnapshot item, LocalDateTime endTime){ cooldowns.put(item, endTime); } // Should override old cooldowns.
    public void addItemCooldownSeconds(ItemStackSnapshot item, TimeUnit timeUnit, int duration){
        long seconds = TimeUnit.SECONDS.convert(duration, timeUnit);
        this.addItemCooldown(item, LocalDateTime.now().plusSeconds(seconds));
    }
    public void removeItemCooldown(ItemStackSnapshot item){cooldowns.remove(item); }
    public Optional<LocalDateTime> getItemCooldown(ItemStackSnapshot item){ return Optional.ofNullable(cooldowns.get(item)); }

    public HashMap<ItemStackSnapshot, LocalDateTime> getCooldowns() { return new HashMap<>(cooldowns); }

    public boolean startCooldownGC(){
        if(AstroItemLib.getTaskManager().getManagerRoot().isPresent()) {
            AstroItemLib.getTaskManager().registerTask(new CooldownGCTask());
            return true;
        }
        return false;
    }
}
