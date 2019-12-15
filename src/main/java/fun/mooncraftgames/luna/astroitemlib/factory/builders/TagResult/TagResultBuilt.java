package fun.mooncraftgames.luna.astroitemlib.factory.builders.TagResult;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import org.spongepowered.api.util.Tristate;

import java.util.ArrayList;
import java.util.HashMap;

public class TagResultBuilt extends TagResult {
    //TODO: Replace with tristates for cancelling post tags to replace nulls
    protected TagResultBuilt(Tristate shouldCancelTags, Tristate shouldCancelPostTags, HashMap<AbstractTag, String> postTags, HashMap<String, String> sharedData, ArrayList<String> removeAbstractTags) {
        super(shouldCancelTags, shouldCancelPostTags, postTags, sharedData, removeAbstractTags);
    }
}
