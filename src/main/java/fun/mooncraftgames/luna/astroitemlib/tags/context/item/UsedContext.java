package fun.mooncraftgames.luna.astroitemlib.tags.context.item;

import com.flowpowered.math.vector.Vector3d;
import fun.mooncraftgames.luna.astroitemlib.tags.ClickType;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Optional;

public class UsedContext extends ExecutionContext {

    private HandType handType;
    private ClickType clickType;

    private Vector3d clickPos;

    private boolean cancelled;

    /**
     *
     * @param player The player using the item.
     * @param handType The hand the item is held in.
     * @param clickType Was the use action a punch or a place?
     * @param clickPos The position of an interaction. Precision depends on event
     */
    public UsedContext(Player player, HashMap<String, String> sharedData, HandType handType, ClickType clickType, Vector3d clickPos) {
        super(player, sharedData);

        this.handType = handType;
        this.clickType = clickType;

        this.clickPos = clickPos;

        this.cancelled = false;
    }

    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    /** If true, all events connected with the context are cancelled (Like a block change)*/
    public boolean isCancelled() { return cancelled; }
    public HandType getHandType() { return handType; }
    public ClickType getClickType() { return clickType; }
    public Optional<Vector3d> getClickPos() { return Optional.ofNullable(clickPos); }
}
