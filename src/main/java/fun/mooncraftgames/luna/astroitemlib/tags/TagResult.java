package fun.mooncraftgames.luna.astroitemlib.tags;

import fun.mooncraftgames.luna.astroitemlib.factory.builders.TagResult.TagResultBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public abstract class TagResult {

    private Boolean shouldCancelTags;
    private Boolean shouldCancelPostTags;

    private HashMap<String, String> sharedData;
    private HashMap<AbstractTag, String> postTags;

    private ArrayList<String> removeAbstractTags;

    protected TagResult(boolean shouldCancelTags, boolean shouldCancelPostTags, HashMap<AbstractTag, String> postTags, HashMap<String, String> sharedData, ArrayList<String> removeAbstractTags) {
        this.shouldCancelTags = shouldCancelTags;
        this.shouldCancelPostTags = shouldCancelPostTags;
        this.postTags = postTags;
        this.sharedData = sharedData;
        this.removeAbstractTags = removeAbstractTags;
    }

    public boolean shouldCancelTags() { return shouldCancelTags; }

    public Optional<Boolean> shouldCancelPostTags() { return Optional.ofNullable(shouldCancelPostTags); }
    public HashMap<AbstractTag, String> getPostTags() { return postTags; }
    public HashMap<String, String> getSharedDataSubmission() { return sharedData; }
    public ArrayList<String> getQueueRemoveTags() { return removeAbstractTags; }

    public static TagResultBuilder builder(){
        return new TagResultBuilder();
    }

}
