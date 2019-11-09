package io.cg360.secondmoon.astroitemlib.tags.item.devtest;

import com.flowpowered.math.vector.Vector3d;
import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.TagPriority;
import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import io.cg360.secondmoon.astroitemlib.tags.data.HitContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class TagDevCatapult extends AbstractTag {


    public TagDevCatapult(String id, TagPriority priority, ExecutionTypes type) {
        super(id, priority, type);
    }

    @Override
    public boolean run(ExecutionTypes type, String tag, ItemStackSnapshot itemStack, ExecutionContext context) {
        if(getType() == ExecutionTypes.HIT) {
            HitContext hitContext = (HitContext) context;
            hitContext.getEvent().setCancelled(true);
            hitContext.getEvent().getTargetEntity().setVelocity(new Vector3d(0, 5, 0));
            ParticleEffect e = ParticleEffect.builder().type(ParticleTypes.HUGE_EXPLOSION).quantity(1).build();

            hitContext.getEvent().getTargetEntity().getLocation().getExtent()
                    .spawnParticles(e, hitContext.getEvent().getTargetEntity().getLocation().getPosition());

            return false;
        }
        return true;
    }
}
