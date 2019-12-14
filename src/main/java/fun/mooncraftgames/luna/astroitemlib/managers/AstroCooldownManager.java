package fun.mooncraftgames.luna.astroitemlib.managers;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.tasks.CooldownGCTask;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AstroCooldownManager {

    private HashMap<String, LocalDateTime> cooldowns;
    private HashMap<String, LocalDateTime> silentcooldowns;
    public AstroCooldownManager(){
        this.cooldowns = new HashMap<>();
        this.silentcooldowns = new HashMap<>();
    }

    // --- Use cooldowns ---

    public void addItemCooldown(String id, LocalDateTime endTime){ this.cooldowns.put(id, endTime); } // Should override old cooldowns.
    public void addItemCooldownMillis(String id, int duration){
        long nanos = TimeUnit.NANOSECONDS.convert(duration, TimeUnit.MILLISECONDS);
        this.addItemCooldown(id, LocalDateTime.now().plusNanos(nanos));
    }
    public void removeItemCooldown(String id){cooldowns.remove(id); }
    public Optional<LocalDateTime> getItemCooldown(String id){ return Optional.ofNullable(cooldowns.get(id)); }
    public HashMap<String, LocalDateTime> getCooldowns() { return new HashMap<>(cooldowns); }

    // --- Silent Use Cooldowns ---

    public void addSilentItemCooldown(String id, LocalDateTime endTime){ this.silentcooldowns.put(id, endTime); } // Should override old cooldowns.
    public void addSilentItemCooldownMillis(String id, int duration){
        long nanos = TimeUnit.NANOSECONDS.convert(duration, TimeUnit.MILLISECONDS);
        this.addSilentItemCooldown(id, LocalDateTime.now().plusNanos(nanos));
    }
    public void removeSilentItemCooldown(String id){silentcooldowns.remove(id); }
    public Optional<LocalDateTime> getSilentItemCooldown(String id){ return Optional.ofNullable(silentcooldowns.get(id)); }
    public HashMap<String, LocalDateTime> getSilentCooldowns() { return new HashMap<>(silentcooldowns); }


    // --- OTHER ---

    public boolean startCooldownGC(){
        if(AstroItemLib.getTaskManager().getManagerRoot().isPresent()) {
            AstroItemLib.getTaskManager().registerTask(new CooldownGCTask());
            return true;
        }
        return false;
    }
}
