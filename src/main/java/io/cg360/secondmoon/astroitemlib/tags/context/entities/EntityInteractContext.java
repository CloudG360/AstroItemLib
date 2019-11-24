package io.cg360.secondmoon.astroitemlib.tags.context.entities;

import io.cg360.secondmoon.astroitemlib.tags.ClickType;
import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;

public class EntityInteractContext extends ExecutionContext {

    private ClickType clickType;
    private Entity targetEntity;

    private boolean isCancelled;

    public EntityInteractContext(Player player, ClickType clickType, Entity target) {
        super(player);
        this.clickType = clickType;
    }

    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public ClickType getClickType() { return clickType; }
    public Entity getTargetEntity() { return targetEntity; }
    public boolean isCancelled() { return isCancelled; }
}
