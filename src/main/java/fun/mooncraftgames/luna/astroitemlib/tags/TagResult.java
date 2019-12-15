package fun.mooncraftgames.luna.astroitemlib.tags;

import fun.mooncraftgames.luna.astroitemlib.factory.builders.TagResult.TagResultBuilder;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TagResult {

    private Tristate shouldCancelTags;
    private Tristate shouldCancelPostTags;

    private HashMap<String, String> sharedData;
    private HashMap<AbstractTag, String> postTags;

    private ArrayList<String> removeAbstractTags;

    protected TagResult(Tristate shouldCancelTags, Tristate shouldCancelPostTags, HashMap<AbstractTag, String> postTags, HashMap<String, String> sharedData, ArrayList<String> removeAbstractTags) {
        this.shouldCancelTags = shouldCancelTags;
        this.shouldCancelPostTags = shouldCancelPostTags;
        this.postTags = postTags;
        this.sharedData = sharedData;
        this.removeAbstractTags = removeAbstractTags;
    }

    public Tristate shouldCancelTags() { return shouldCancelTags; }

    public Tristate shouldCancelPostTags() { return shouldCancelPostTags; }
    public HashMap<AbstractTag, String> getPostTags() { return postTags; }
    public HashMap<String, String> getSharedDataSubmission() { return sharedData; }
    public ArrayList<String> getQueueRemoveTags() { return removeAbstractTags; }

    public static TagResultBuilder builder(){
        return new TagResultBuilder();
    }

}
