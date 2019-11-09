package io.cg360.secondmoon.astroitemlib.tags.data;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DamageEntityEvent;

public class HitContext extends ExecutionContext {

    private DamageEntityEvent event;

    public HitContext(Player player, DamageEntityEvent event) {
        super(player);
        this.event = event;
    }

    public DamageEntityEvent getEvent() { return event; }
}
