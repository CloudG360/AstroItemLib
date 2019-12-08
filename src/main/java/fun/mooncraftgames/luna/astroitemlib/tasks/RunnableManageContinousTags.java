package fun.mooncraftgames.luna.astroitemlib.tasks;

import fun.mooncraftgames.luna.astroitemlib.AstroItemLib;
import fun.mooncraftgames.luna.astroitemlib.data.AstroKeys;
import fun.mooncraftgames.luna.astroitemlib.managers.AstroTagManager;
import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.ExecutionTypes;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import fun.mooncraftgames.luna.astroitemlib.tags.context.ExecutionContext;
import fun.mooncraftgames.luna.astroitemlib.tags.context.item.HoldingContext;
import fun.mooncraftgames.luna.astroitemlib.tasks.interfaces.IAstroTask;
import fun.mooncraftgames.luna.astroitemlib.utilities.HashMapBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.*;

public class RunnableManageContinousTags implements IAstroTask {


    private ArrayList<UUID> players;
    private int repeatRate;
    private int delay;

    public RunnableManageContinousTags(int repeatRate, int delay){
        this.players = new ArrayList<>();
        this.repeatRate = repeatRate;
        this.delay = delay;
    }

    @Override public String getName() { return "astrotaghandler"; }
    @Override public String getDescription() { return "Handles the processing of Astro's item tags."; }
    @Override public boolean isAsync() { return false; }

    @Override public int getRepeatRate() { return repeatRate; }
    @Override public int getDelay() { return delay; }

    @Override public void onRegister(UUID uuid) { AstroItemLib.getTagManager().addTagProcessor(uuid); }
    @Override public void onUnregister(UUID uuid) { AstroItemLib.getTagManager().removeTagProcessor(uuid); }

    public ArrayList<UUID> getPlayers() { return players; }
    public RunnableManageContinousTags addPlayer(UUID uuid){ players.add(uuid); return this; }

    @Override
    public void run() {
        ArrayList<UUID> plist = new ArrayList<>(players);
        for(UUID uuid:plist){
            Optional<Player> p = Sponge.getServer().getPlayer(uuid);

            if(p.isPresent()){
                Player player = p.get();
                // Main hand holding
                if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                    ItemStackSnapshot istack = player.getItemInHand(HandTypes.MAIN_HAND).get().createSnapshot();
                    Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
                    if (tgs.isPresent()) {
                        List<String> tags = tgs.get();

                        String[] otags = AstroItemLib.getTagManager().orderedTags(tags.toArray(new String[0]));

                        HashMap<String, String> sharedData = new HashMap<>();

                        boolean postCancelled = false;
                        HashMap<AbstractTag, String> postTags = new HashMap<>();
                        for (String tag : otags) {
                            if (AstroItemLib.getTagManager().getTag(tag).isPresent()) {
                                AbstractTag t = AstroItemLib.getTagManager().getTag(tag).get();
                                String[] arguments = AstroTagManager.getTagArguments(tag);
                                TagResult result = TagResult.builder().build();
                                if (t.getType() == ExecutionTypes.ITEM_HOLDING) { result = t.run(ExecutionTypes.ITEM_HOLDING, tag, arguments, istack, false, new HoldingContext(player, sharedData, HandTypes.MAIN_HAND)); }
                                postTags = AstroTagManager.removePostTags(postTags, result.getQueueRemoveTags());
                                postTags.putAll(result.getPostTags());
                                sharedData.putAll(result.getSharedDataSubmission());
                                if(result.shouldCancelPostTags().isPresent()){ postCancelled = result.shouldCancelPostTags().get(); }
                                if(result.shouldCancelTags()) break;
                            }
                        }
                        if(!postCancelled){
                            for (Map.Entry<AbstractTag, String> pair:postTags.entrySet()){
                                String tag = pair.getValue();
                                AbstractTag t = pair.getKey();
                                String[] arguments = AstroTagManager.getTagArguments(tag);
                                TagResult result = TagResult.builder().build();
                                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_HOLDING, tag, arguments, istack, true, new HoldingContext(player, sharedData, HandTypes.MAIN_HAND)); }
                                if(t.getType() == ExecutionTypes.POST_PROCESSING){ result = t.run(ExecutionTypes.POST_PROCESSING, tag, arguments, istack, true, new ExecutionContext.Generic(player, sharedData)); }
                                sharedData.putAll(result.getSharedDataSubmission());
                                if(result.shouldCancelPostTags().orElse(false)){ break; }
                            }
                        }
                    }
                }

                //Secondary Hand Holding
                if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
                    ItemStackSnapshot istack = player.getItemInHand(HandTypes.OFF_HAND).get().createSnapshot();
                    Optional<List<String>> tgs = istack.get(AstroKeys.FUNCTION_TAGS);
                    if (tgs.isPresent()) {
                        List<String> tags = tgs.get();

                        String[] otags = AstroItemLib.getTagManager().orderedTags(tags.toArray(new String[0]));

                        HashMap<String, String> sharedData = new HashMap<>();

                        boolean postCancelled = false;
                        HashMap<AbstractTag, String> postTags = new HashMap<>();
                        for (String tag : otags) {
                            if (AstroItemLib.getTagManager().getTag(tag).isPresent()) {
                                AbstractTag t = AstroItemLib.getTagManager().getTag(tag).get();
                                String[] arguments = AstroTagManager.getTagArguments(tag);
                                TagResult result = TagResult.builder().build();
                                if (t.getType() == ExecutionTypes.ITEM_HOLDING) { result = t.run(ExecutionTypes.ITEM_HOLDING, tag, arguments, istack, false, new HoldingContext(player, sharedData, HandTypes.OFF_HAND)); }
                                postTags = AstroTagManager.removePostTags(postTags, result.getQueueRemoveTags());
                                postTags.putAll(result.getPostTags());
                                sharedData.putAll(result.getSharedDataSubmission());
                                if(result.shouldCancelPostTags().isPresent()){ postCancelled = result.shouldCancelPostTags().get(); }
                                if(result.shouldCancelTags()) break;
                            }
                        }
                        if(!postCancelled){
                            for (Map.Entry<AbstractTag, String> pair:postTags.entrySet()){
                                String tag = pair.getValue();
                                AbstractTag t = pair.getKey();
                                String[] arguments = AstroTagManager.getTagArguments(tag);
                                TagResult result = TagResult.builder().build();
                                if(t.getType() == ExecutionTypes.ITEM_USED) { result = t.run(ExecutionTypes.ITEM_HOLDING, tag, arguments, istack, true, new HoldingContext(player, sharedData, HandTypes.OFF_HAND)); }
                                if(t.getType() == ExecutionTypes.POST_PROCESSING){ result = t.run(ExecutionTypes.POST_PROCESSING, tag, arguments, istack, true, new ExecutionContext.Generic(player, sharedData)); }
                                sharedData.putAll(result.getSharedDataSubmission());
                                if(result.shouldCancelPostTags().orElse(false)){ break; }
                            }
                        }
                    }
                }



            } else {
                players.remove(uuid);
            }
        }
    }

    @Override
    public HashMap<String, String> toDataStringMap() {
        return HashMapBuilder.builder(String.class, String.class)
                .addField("Players", Arrays.toString(players.toArray(new UUID[0])))
                .build();
    }
}
