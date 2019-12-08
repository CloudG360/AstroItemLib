package fun.mooncraftgames.luna.astroitemlib.tags;

import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public abstract class AbstractTag {

    private String id;
    private TagPriority priority;
    private ExecutionTypes type;

    private Integer tickInterval;


    public AbstractTag(String id, TagPriority priority, ExecutionTypes type){
        this.id = id;
        this.priority = priority;
        this.type = type;
    }

    //TODO: Replace boolean return with .set functions. Should be able to disable tag in the future.
    /**
     * Handles execution for a tag's functions. Designed to
     * be run by the AstroTagManager's listener events.
     *
     * @param fullTag An unprocessed tag raw from an item.
     * @param args Processed arguments of a tag.
     * @param itemStack the stack which possesses the tag.
     * @param context Provides ExecutionType dependant data.
     *
     * @return Use a TagResult.builder()
     */
    public abstract TagResult run(ExecutionTypes type, String fullTag, String[] args, ItemStackSnapshot itemStack, boolean isAppended, ExecutionContext context);

    /**
     * For continuous ExecutionTypes. Sets how many ticks
     * pass between each call of #Run()
     *
     * @param tickInterval In Minecraft Ticks
     * @return
     */
    public final AbstractTag setTickInterval(Integer tickInterval) { this.tickInterval = tickInterval; return this; }

    public final String getId() { return id; }
    public final TagPriority getPriority() { return priority; }
    public final ExecutionTypes getType() { return type; }
    public final Integer getTickInterval() { return tickInterval; }


}
