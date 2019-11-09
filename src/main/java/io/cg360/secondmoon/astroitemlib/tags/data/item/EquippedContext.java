package io.cg360.secondmoon.astroitemlib.tags.data.item;

import io.cg360.secondmoon.astroitemlib.tags.data.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;

public class EquippedContext extends ExecutionContext {

    //TODO: Slot
    private SlotType slotType;

    public enum SlotType { HELM, CHEST, LEGS, BOOTS }
    public EquippedContext(Player player, SlotType slotType) {
        super(player);
        this.slotType = slotType;
    }

    public SlotType getSlotType() { return slotType; }
}
