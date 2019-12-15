package fun.mooncraftgames.luna.astroitemlib.factory.builders.TagResult;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TagResultBuilder {
    private Tristate shouldCancelTags;
    private Tristate shouldCancelPostTags;

    private HashMap<AbstractTag, String> postTags;
    private HashMap<String, String> sharedData;
    private ArrayList<String> removeAbstractTags;

    public TagResultBuilder(){
        this.shouldCancelTags = Tristate.UNDEFINED;
        this.shouldCancelPostTags = Tristate.UNDEFINED;
        this.postTags = new HashMap<>();
        this.sharedData = new HashMap<>();
        this.removeAbstractTags = new ArrayList<>();
    }

    private void validate(){
        if(this.shouldCancelTags == null) this.shouldCancelTags = Tristate.UNDEFINED;
        if(this.shouldCancelPostTags == null) this.shouldCancelPostTags = Tristate.UNDEFINED;
        if(this.postTags == null) this.postTags = new HashMap<>();
        if(this.sharedData == null) this.sharedData = new HashMap<>();
        if(this.removeAbstractTags == null) this.removeAbstractTags = new ArrayList<>();
    }

    public TagResultBuilder setShouldCancelPostTags(Tristate shouldCancelPostTags) { this.shouldCancelPostTags = shouldCancelPostTags; return this; }
    public TagResultBuilder setShouldCancelTags(Tristate shouldCancelTags) { this.shouldCancelTags = shouldCancelTags; return this; }

    public TagResultBuilder offerSharedDataEdits(HashMap<String, String> sharedData){
        this.sharedData = sharedData;
        return this;
    }

    @SafeVarargs
    public final TagResultBuilder addPostTags(Pair<AbstractTag, String>... tags) { for(Pair<AbstractTag, String> pair : tags){ postTags.put(pair.getKey(), pair.getValue()); } return this; }
    public TagResultBuilder removePostTags(String... tags) {
        List<String> tgs = Arrays.asList(tags);
        postTags.keySet().removeIf(t -> tgs.contains(t.getId().toLowerCase()));
        return this;
    }

    public TagResultBuilder setPostTags(HashMap<AbstractTag, String> postTags) { this.postTags = postTags; return this; }
    public TagResultBuilder clearRemovalQueue() { this.removeAbstractTags = new ArrayList<>(); return this; }

    public TagResult build(){
        this.validate();
        return new TagResultBuilt(this.shouldCancelTags, this.shouldCancelPostTags, this.postTags, this.sharedData, this.removeAbstractTags);
    }

    public class TagResultBuilt extends TagResult {
        private TagResultBuilt(Tristate shouldCancelTags, Tristate shouldCancelPostTags, HashMap<AbstractTag, String> postTags, HashMap<String, String> sharedData, ArrayList<String> removeAbstractTags) {
            super(shouldCancelTags, shouldCancelPostTags, postTags, sharedData, removeAbstractTags);
        }
    }
}
