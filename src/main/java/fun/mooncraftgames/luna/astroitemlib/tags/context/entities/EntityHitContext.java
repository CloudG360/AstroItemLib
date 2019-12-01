package fun.mooncraftgames.luna.astroitemlib.tags.context.entities;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DamageEntityEvent;

public class EntityHitContext extends ExecutionContext {

    private DamageEntityEvent event;

    public EntityHitContext(Player player, DamageEntityEvent event) {
        super(player);
        this.event = event;
    }

    public DamageEntityEvent getEvent() { return event; }
}
