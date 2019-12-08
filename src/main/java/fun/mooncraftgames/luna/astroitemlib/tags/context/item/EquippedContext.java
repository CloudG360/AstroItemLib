package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;

public class EquippedContext extends ExecutionContext {
    private SlotType slotType;

    public enum SlotType { HELM, CHEST, LEGS, BOOTS }
    public EquippedContext(Player player, SlotType slotType) {
        super(player);
        this.slotType = slotType;
    }

    public SlotType getSlotType() { return slotType; }
}
