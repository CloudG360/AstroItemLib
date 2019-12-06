package fun.mooncraftgames.luna.astroitemlib.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class TagResult {

    private boolean shouldCancelTags;
    private boolean shouldCancelPostTags;

    private ArrayList<AbstractTag> postTags;
    private ArrayList<String> removeAbstractTags;

    private TagResult(boolean shouldCancelTags, boolean shouldCancelPostTags, ArrayList<AbstractTag> postTags, ArrayList<String> removeAbstractTags) {
        this.shouldCancelTags = shouldCancelTags;
        this.shouldCancelPostTags = shouldCancelPostTags;
        this.postTags = postTags;
        this.removeAbstractTags = removeAbstractTags;
    }

    public boolean shouldCancelTags() { return shouldCancelTags; }
    public boolean shouldCancelPostTags() { return shouldCancelPostTags; }
    public ArrayList<AbstractTag> getPostTags() { return postTags; }
    public ArrayList<String> getQueueRemoveTags() { return removeAbstractTags; }

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{

        private Boolean shouldCancelTags;
        private Boolean shouldCancelPostTags;

        private ArrayList<AbstractTag> postTags;
        private ArrayList<String> removeAbstractTags;

        private Builder(){
            this.shouldCancelTags = false;
            this.shouldCancelPostTags = false;
            this.postTags = new ArrayList<>();
            this.removeAbstractTags = new ArrayList<>();
        }

        private void validate(){
            if(this.shouldCancelTags == null) this.shouldCancelTags = false;
            if(this.shouldCancelPostTags == null) this.shouldCancelPostTags = false;
            if(this.postTags == null) this.postTags = new ArrayList<>();
            if(this.removeAbstractTags == null) this.removeAbstractTags = new ArrayList<>();
        }

        public Builder setShouldCancelPostTags(Boolean shouldCancelPostTags) { this.shouldCancelPostTags = shouldCancelPostTags; return this; }
        public Builder setShouldCancelTags(Boolean shouldCancelTags) { this.shouldCancelTags = shouldCancelTags; return this; }

        public Builder addPostTags(AbstractTag... tags) { this.postTags.addAll(Arrays.asList(tags)); return this; }
        public Builder removePostTags(String... tags) {
            List<String> tgs = Arrays.asList(tags);
            ArrayList<AbstractTag> clone = new ArrayList<>(postTags);
            for(int i = 0; i < postTags.size(); i++) {
                int li = tgs.lastIndexOf(postTags.get(i).getId());
                if(li == -1){ // Not found, escalate to full list.
                    removeAbstractTags.add(tgs.get(li));
                } else {
                    clone.set(i, null);
                }
            }
            clone.removeIf(Objects::isNull);
            postTags = clone;
            return this;
        }

        public Builder setPostTags(ArrayList<AbstractTag> postTags) { this.postTags = postTags; return this; }
        public Builder clearRemovalQueue() { this.removeAbstractTags = new ArrayList<>(); return this; }

        public TagResult build(){
            validate();
            return new TagResult(shouldCancelTags, shouldCancelPostTags, postTags, removeAbstractTags) {
                @Override
                public int hashCode() {
                    return super.hashCode();
                }
            };
        }
    }

}
