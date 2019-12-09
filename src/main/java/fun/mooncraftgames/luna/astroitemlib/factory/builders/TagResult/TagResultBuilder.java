package fun.mooncraftgames.luna.astroitemlib.factory.builders.TagResult;

import fun.mooncraftgames.luna.astroitemlib.tags.AbstractTag;
import fun.mooncraftgames.luna.astroitemlib.tags.TagResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TagResultBuilder {

        private Boolean shouldCancelTags;
        private Boolean shouldCancelPostTags;

        private HashMap<AbstractTag, String> postTags;
        private HashMap<String, String> sharedData;
        private ArrayList<String> removeAbstractTags;

        public TagResultBuilder(){
            this.shouldCancelTags = false;
            this.shouldCancelPostTags = false;
            this.postTags = new HashMap<>();
            this.sharedData = new HashMap<>();
            this.removeAbstractTags = new ArrayList<>();
        }

        private void validate(){
            if(this.shouldCancelTags == null) this.shouldCancelTags = false;
            if(this.shouldCancelPostTags == null) this.shouldCancelPostTags = false;
            if(this.postTags == null) this.postTags = new HashMap<>();
            if(this.sharedData == null) this.sharedData = new HashMap<>();
            if(this.removeAbstractTags == null) this.removeAbstractTags = new ArrayList<>();
        }

        public TagResultBuilder setShouldCancelPostTags(Boolean shouldCancelPostTags) { this.shouldCancelPostTags = shouldCancelPostTags; return this; }
        public TagResultBuilder setShouldCancelTags(Boolean shouldCancelTags) { this.shouldCancelTags = shouldCancelTags; return this; }

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
}
