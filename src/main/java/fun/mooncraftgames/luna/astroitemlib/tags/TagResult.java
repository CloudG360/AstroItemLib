package fun.mooncraftgames.luna.astroitemlib.tags;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public abstract class TagResult {

    private Boolean shouldCancelTags;
    private Boolean shouldCancelPostTags;

    private HashMap<String, String> sharedData;
    private HashMap<AbstractTag, String> postTags;

    private ArrayList<String> removeAbstractTags;

    private TagResult(boolean shouldCancelTags, boolean shouldCancelPostTags, HashMap<AbstractTag, String> postTags, HashMap<String, String> sharedData, ArrayList<String> removeAbstractTags) {
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




    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{

        private Boolean shouldCancelTags;
        private Boolean shouldCancelPostTags;

        private HashMap<AbstractTag, String> postTags;
        private HashMap<String, String> sharedData;
        private ArrayList<String> removeAbstractTags;

        private Builder(){
            this.shouldCancelTags = false;
            this.postTags = new HashMap<>();
            this.sharedData = new HashMap<>();
            this.removeAbstractTags = new ArrayList<>();
        }

        private void validate(){
            if(this.shouldCancelTags == null) this.shouldCancelTags = false;
            if(this.postTags == null) this.postTags = new HashMap<>();
            if(this.sharedData == null) this.sharedData = new HashMap<>();
            if(this.removeAbstractTags == null) this.removeAbstractTags = new ArrayList<>();
        }

        public Builder setShouldCancelPostTags(Boolean shouldCancelPostTags) { this.shouldCancelPostTags = shouldCancelPostTags; return this; }
        public Builder setShouldCancelTags(Boolean shouldCancelTags) { this.shouldCancelTags = shouldCancelTags; return this; }

        public Builder offerSharedDataEdits(HashMap<String, String> sharedData){
            this.sharedData = sharedData;
            return this;
        }

        @SafeVarargs
        public final Builder addPostTags(Pair<AbstractTag, String>... tags) { for(Pair<AbstractTag, String> pair : tags){ postTags.put(pair.getKey(), pair.getValue()); } return this; }
        public Builder removePostTags(String... tags) {
            List<String> tgs = Arrays.asList(tags);
            postTags.keySet().removeIf(t -> tgs.contains(t.getId().toLowerCase()));
            return this;
        }

        public Builder setPostTags(HashMap<AbstractTag, String> postTags) { this.postTags = postTags; return this; }
        public Builder clearRemovalQueue() { this.removeAbstractTags = new ArrayList<>(); return this; }

        public TagResult build(){
            validate();
            return new TagResult(shouldCancelTags, shouldCancelPostTags, postTags, sharedData, removeAbstractTags) {
                @Override public int hashCode() {
                    return super.hashCode();
                }
            };
        }
    }

}
