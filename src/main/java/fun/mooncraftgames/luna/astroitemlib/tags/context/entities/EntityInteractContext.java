package fun.mooncraftgames.luna.astroitemlib.tags.context.entities;

import fun.mooncraftgames.luna.astroitemlib.tags.ClickType;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;

public class EntityInteractContext extends ExecutionContext {

    private ClickType clickType;
    private Entity targetEntity;

    private boolean isCancelled;

    public EntityInteractContext(Player player, ClickType clickType, Entity target, boolean isCancelled) {
        super(player);
        this.clickType = clickType;
        this.targetEntity = target;
        this.isCancelled = isCancelled;
    }

    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public ClickType getClickType() { return clickType; }
    public Entity getTargetEntity() { return targetEntity; }
    public boolean isCancelled() { return isCancelled; }
}
