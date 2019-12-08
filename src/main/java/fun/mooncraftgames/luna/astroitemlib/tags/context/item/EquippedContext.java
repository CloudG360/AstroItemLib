package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class EquippedContext extends ExecutionContext {
    private SlotType slotType;

    public enum SlotType { HELM, CHEST, LEGS, BOOTS }
    public EquippedContext(Player player, HashMap<String, String> sharedData, SlotType slotType) {
        super(player, sharedData);
        this.slotType = slotType;
    }

    public SlotType getSlotType() { return slotType; }
}
