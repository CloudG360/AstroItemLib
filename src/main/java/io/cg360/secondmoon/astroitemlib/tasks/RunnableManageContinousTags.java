package io.cg360.secondmoon.astroitemlib.tasks;

import io.cg360.secondmoon.astroitemlib.AstroItemLib;
import io.cg360.secondmoon.astroitemlib.data.AstroKeys;
import io.cg360.secondmoon.astroitemlib.tags.AbstractTag;
import io.cg360.secondmoon.astroitemlib.tags.ExecutionTypes;
import io.cg360.secondmoon.astroitemlib.tags.data.item.HoldingContext;
import io.cg360.secondmoon.astroitemlib.tasks.interfaces.IAstroTask;
import io.cg360.secondmoon.astroitemlib.utilities.HashMapBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.*;

public class RunnableManageContinousTags implements IAstroTask {


    private ArrayList<UUID> players;

    public RunnableManageContinousTags(){
        players = new ArrayList<>();
    }

    @Override public String getName() { return "astrotaghandler"; }
    @Override public String getDescription() { return "Handles the processing of Astro's item tags."; }
    @Override public boolean isAsync() { return false; }

    @Override
    public void run() {
        for(UUID uuid:players){
            Sponge.getServer().getPlayer(uuid).ifPresent(player -> {

                // Main hand holding
                if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                    Optional<List<String>> tgs = player.getItemInHand(HandTypes.MAIN_HAND).get().get(AstroKeys.FUNCTION_TAGS);
                    if (tgs.isPresent()) {
                        List<String> tags = tgs.get();
                        ItemStackSnapshot istack = player.getItemInHand(HandTypes.MAIN_HAND).get().createSnapshot();

                        String[] otags = AstroItemLib.getTagManager().orderedTags(tags.toArray(new String[0]));

                        for (String tag : otags) {
                            if (AstroItemLib.getTagManager().getTag(tag).isPresent()) {
                                AbstractTag t = AstroItemLib.getTagManager().getTag(tag).get();
                                boolean result = true;
                                if (t.getType() == ExecutionTypes.ITEM_HOLDING) {
                                    result = t.run(ExecutionTypes.ITEM_HOLDING, tag, istack, new HoldingContext(player, HandTypes.MAIN_HAND));
                                }
                                if (!result) return;
                            }
                        }

                    }
                }

                //Secondary Hand Holding
                if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
                    Optional<List<String>> tgs = player.getItemInHand(HandTypes.OFF_HAND).get().get(AstroKeys.FUNCTION_TAGS);
                    if (tgs.isPresent()) {
                        List<String> tags = tgs.get();
                        ItemStackSnapshot istack = player.getItemInHand(HandTypes.OFF_HAND).get().createSnapshot();

                        String[] otags = AstroItemLib.getTagManager().orderedTags(tags.toArray(new String[0]));

                        for (String tag : otags) {
                            if (AstroItemLib.getTagManager().getTag(tag).isPresent()) {
                                AbstractTag t = AstroItemLib.getTagManager().getTag(tag).get();
                                boolean result = true;
                                if (t.getType() == ExecutionTypes.ITEM_HOLDING) {
                                    result = t.run(ExecutionTypes.ITEM_HOLDING, tag, istack, new HoldingContext(player, HandTypes.OFF_HAND));
                                }
                                if (!result) return;
                            }
                        }

                    }
                }



            });
        }
    }

    @Override
    public HashMap<String, String> toDataStringMap() {
        return HashMapBuilder.builder(String.class, String.class)
                .addField("Players", Arrays.toString(players.toArray(new UUID[0])))
                .build();
    }
}
