package io.cg360.secondmoon.astroitemlib.tags;

import com.google.common.annotations.Beta;

public enum ExecutionTypes {

    /** Continuous execution at an interval when an item is held in one of a player's hands. Interval can be specified in constructor.*/
    ITEM_HELD,

    /** Continuous execution at an interval when a piece of armour is equipped. Interval can be specified in constructor.*/
    ITEM_EQUIPPED,

    /** Continuous execution at an interval when an item is present in the inventory.
     * Interval can be specified in constructor.
     *
     * BETA: Currently unimplemented due to potential resource hog.
     * */
    @Beta
    ITEM_CARRIED,

    /** Continuous execution at an interval when an item is present in the hotbar.
     * Interval can be specified in constructor.
     *
     * BETA: Currently unimplemented.
     * */
    @Beta
    ITEM_CARRIED_HOTBAR,

    /** Single Execution, supports cooldowns.
     * Executes when used. Doesn't have
     * to be used on an entity or block
     */
    ITEM_USED,

    ITEM_CLICKED,

    /** Single Execution, triggers when dropped.*/
    ITEM_DROPPED,

    /** Single Execution, triggers when picked up.*/
    ITEM_PICKUP,

    /** Single Execution, supports cooldowns.
     * Only supports left-click hit. Triggered when successfully
     * hitting an entity.
     */
    ENTITY_HIT,

    /** Single Execution, supports cooldowns.
     * Only supports Right-Click interact. Triggered when successfully
     * interacting with an entity.
     */
    ENTITY_INTERACT,

    BLOCK_INTERACT,

    BLOCK_PLACE,

    BLOCK_BREAK

}
