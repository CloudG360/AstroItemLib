package io.cg360.secondmoon.astroitemlib.tags.context.blocks;

import com.flowpowered.math.vector.Vector3i;
import io.cg360.secondmoon.astroitemlib.tags.context.ExecutionContext;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class BlockChangeContext extends ExecutionContext {

    private boolean cancelAllChanges;
    private HashMap<String, BlockChange> blockChanges;

    public BlockChangeContext(Player player, List<Transaction<BlockSnapshot>> transactions) {
        super(player);
        this.cancelAllChanges = false;
        this.blockChanges = new HashMap<>();
        for(Transaction<BlockSnapshot> transaction: transactions){
            BlockSnapshot o = transaction.getOriginal();
            BlockSnapshot f = transaction.getFinal();
            if(transaction.getFinal().getState().getType().equals(BlockTypes.AIR)){
                blockChanges.put(generateBlockID(f), new BlockChange(o, f, BlockChangeType.BREAK, true));
            } else {
                blockChanges.put(generateBlockID(f), new BlockChange(o, f, BlockChangeType.PLACE, true));
            }
        }
    }
    /** @return String id. Empty if block change failed. */
    public Optional<String> registerBlockPlaceChange(Location<World> location, BlockState replacement) {
        if(replacement.getType() == BlockTypes.AIR) return Optional.empty();
        BlockSnapshot o = location.getBlock().snapshotFor(location);
        BlockSnapshot f = replacement.snapshotFor(location);
        String id = generateBlockID(f);
        if(blockChanges.containsKey(id)){
            BlockChange change = blockChanges.get(id);
            if(change.isOriginalTransaction()){
                BlockChange blockChange = new BlockChange(o, f, BlockChangeType.PLACE, true);
                blockChange.setModified(true);
                blockChange.setOriginalType(change.getOriginalType());
                blockChanges.put(id, blockChange);
            } else {
                BlockChange blockChange = new BlockChange(o, f, BlockChangeType.PLACE, false);
                blockChange.setModified(true);
                blockChange.setOriginalType(change.getOriginalType());
                blockChanges.put(id, blockChange);
            }
        } else { blockChanges.put(id, new BlockChange(o, f, BlockChangeType.PLACE, false)); }
        return Optional.of(id);
    }
    /** @return String id. Empty if block change failed. */
    public Optional<String> registerBlockDestroyChange(Location<World> location) {
        if(location.getBlock().getType() == BlockTypes.AIR) return Optional.empty();
        BlockSnapshot o = location.getBlock().snapshotFor(location);
        BlockSnapshot f = BlockState.builder().blockType(BlockTypes.AIR).build().snapshotFor(location);
        String id = generateBlockID(f);
        if(blockChanges.containsKey(id)){
            BlockChange change = blockChanges.get(id);
            if(change.isOriginalTransaction()){
                BlockChange blockChange = new BlockChange(o, f, BlockChangeType.BREAK, true);
                blockChange.setModified(true);
                blockChange.setOriginalType(change.getOriginalType());
                blockChanges.put(id, blockChange);
            } else {
                BlockChange blockChange = new BlockChange(o, f, BlockChangeType.BREAK, false);
                blockChange.setModified(true);
                blockChange.setOriginalType(change.getOriginalType());
                blockChanges.put(id, blockChange);
            }
        } else { blockChanges.put(id, new BlockChange(o, f, BlockChangeType.BREAK, false)); }
        return Optional.of(id);
    }

    private static String generateBlockID(BlockSnapshot blockSnapshot){ Vector3i pos = blockSnapshot.getPosition(); return String.format("%s_%s_%s", pos.getX(), pos.getY(), pos.getZ()); }
    /** @return a <i>clone</i>  of the block changes list.*/
    public HashMap<String, BlockChange> getBlockChanges() { return new HashMap<>(blockChanges); };

    public Optional<BlockChange> getBlockChange(String id){ return Optional.ofNullable(blockChanges.get(id)); }

    public boolean areAllChangesCancelled() { return cancelAllChanges; }
    public void setCancelAllChanges(boolean cancelAllChanges) { this.cancelAllChanges = cancelAllChanges; }
    // ------------ Change Stuff

    public enum BlockChangeType { PLACE, BREAK }
    public class BlockChange {
        private boolean isOriginalTransaction;
        private boolean isCancelled;
        private boolean isModified;
        private ArrayList<ItemStackSnapshot> itemDrops;
        private BlockSnapshot block;
        private BlockChangeType blockChangeType;
        private Direction direction;
        // Used for changing a block change.
        private BlockSnapshot originalb;
        private BlockSnapshot finalb;
        private BlockChangeType originalType;

        private BlockChange (BlockSnapshot originalb, BlockSnapshot finalb, BlockChangeType blockChangeType, boolean isOriginalTransaction){
            this.originalb = originalb;
            this.finalb = finalb;
            this.blockChangeType = blockChangeType;
            this.isOriginalTransaction = isOriginalTransaction;
            this.originalType = blockChangeType;

            this.isCancelled = false;
            this.isModified = !isOriginalTransaction;

            this.block = blockChangeType == BlockChangeType.BREAK ? originalb : finalb;
            this.itemDrops = new ArrayList<>();
        }

        /**
         * @param blockin Sets the resultant block. If AIR or null, it's a block break now.
         * @return If operation was successful.
         */
        public boolean setBlock(BlockSnapshot blockin) {
            this.isModified = true;
            BlockSnapshot b = blockin == null ? BlockSnapshot.builder().blockState(BlockState.builder().blockType(BlockTypes.AIR).build()).build() : blockin;
            if(b.getState().getType() == BlockTypes.AIR){
                blockChangeType = BlockChangeType.BREAK; this.block = originalb;
            } else {
                blockChangeType = BlockChangeType.PLACE; this.block = b;
            }
            return true;
        }

        /** Internal method used by registering. */
        private void setModified(boolean modified) { isModified = modified; }
        /** Internal method used by registering. */
        private void setOriginalType(BlockChangeType originalType) { this.originalType = originalType; }

        /** @param direction Sets the direction of a block change.*/
        public void setDirection(Direction direction) { this.isModified = true; this.direction = direction; }
        /** @param cancelled Sets the block change as cancelled.*/
        public void setCancelled(boolean cancelled) { this.isModified = true; this.isCancelled = cancelled; }
        /** @param itemDrops Sets what the change's block (If of type BREAK) will drop if it's destroyed. If empty, it drops the regular drops. */
        public void setDrops(ArrayList<ItemStackSnapshot> itemDrops) { this.isModified = true;this.itemDrops = itemDrops; }
        /** @return boolean indicating if the change came from the sponge event or not.*/
        public boolean isOriginalTransaction() { return isOriginalTransaction; }
        /** @return If the change has been cancelled.*/
        public boolean isCancelled() { return isCancelled; }
        /** @return boolean of if a block change has been modified (Or if it was added by a tag)*/
        public boolean isModified() { return isModified; }
        /** @return list of what a block (If set to be broken) will drop.*/
        public ArrayList<ItemStackSnapshot> getDrops() { return itemDrops; }
        /** @return BlockSnapshot of the block either being broke or being placed.*/
        public BlockSnapshot getBlock() { return block; }
        /** @return BlockChangeType of if it's a destroy action or a place.*/
        public BlockChangeType getBlockChangeType() { return blockChangeType; }
        /** @return The original BlockSnapshot being replaced.*/
        public BlockSnapshot getOriginalBlock() { return originalb; }
        /** @return The original transaction type.*/
        public BlockChangeType getOriginalType() { return originalType; }
        /** @return the direction a block is facing.*/
        public Direction getDirection() { return direction == null ? Direction.DOWN : direction; }
    }

}
