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
    private HashMap<ItemStackSnapshot, LocalDateTime> silentcooldowns;
    public AstroCooldownManager(){
        this.cooldowns = new HashMap<>();
        this.silentcooldowns = new HashMap<>();
    }

    // --- Use cooldowns ---

    public void addItemCooldown(ItemStackSnapshot item, LocalDateTime endTime){ cooldowns.put(item, endTime); } // Should override old cooldowns.
    public void addItemCooldownSeconds(ItemStackSnapshot item, TimeUnit timeUnit, int duration){
        long seconds = TimeUnit.SECONDS.convert(duration, timeUnit);
        this.addItemCooldown(item, LocalDateTime.now().plusSeconds(seconds));
    }
    public void removeItemCooldown(ItemStackSnapshot item){cooldowns.remove(item); }
    public Optional<LocalDateTime> getItemCooldown(ItemStackSnapshot item){ return Optional.ofNullable(cooldowns.get(item)); }
    public HashMap<ItemStackSnapshot, LocalDateTime> getCooldowns() { return new HashMap<>(cooldowns); }

    // --- Silent Use Cooldowns ---

    public void addSilentItemCooldown(ItemStackSnapshot item, LocalDateTime endTime){ silentcooldowns.put(item, endTime); } // Should override old cooldowns.
    public void addSilentItemCooldownSeconds(ItemStackSnapshot item, TimeUnit timeUnit, int duration){
        long seconds = TimeUnit.SECONDS.convert(duration, timeUnit);
        this.addSilentItemCooldown(item, LocalDateTime.now().plusSeconds(seconds));
    }
    public void removeSilentItemCooldown(ItemStackSnapshot item){silentcooldowns.remove(item); }
    public Optional<LocalDateTime> getSilentItemCooldown(ItemStackSnapshot item){ return Optional.ofNullable(silentcooldowns.get(item)); }
    public HashMap<ItemStackSnapshot, LocalDateTime> getSilentCooldowns() { return new HashMap<>(silentcooldowns); }


    // --- OTHER ---

    public boolean startCooldownGC(){
        if(AstroItemLib.getTaskManager().getManagerRoot().isPresent()) {
            AstroItemLib.getTaskManager().registerTask(new CooldownGCTask());
            return true;
        }
        return false;
    }
}
