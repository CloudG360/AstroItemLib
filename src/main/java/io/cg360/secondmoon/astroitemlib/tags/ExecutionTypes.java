package io.cg360.secondmoon.astroitemlib.tags;

import com.google.common.annotations.Beta;

public enum ExecutionTypes {

    /** Continuous execution at an interval when an item is held in one of a player's hands. Interval can be specified in constructor.*/
    HELD,

    /** Continuous execution at an interval when a piece of armour is equipped. Interval can be specified in constructor.*/
    EQUIPPED,

    /** Continuous execution at an interval when an item is present in the inventory.
     * Interval can be specified in constructor.
     *
     * BETA: Currently unimplemented due to potential resource hog.
     * */
    @Beta
    CARRIED,

    /** Continuous execution at an interval when an item is present in the hotbar.
     * Interval can be specified in constructor.
     *
     * BETA: Currently unimplemented.
     * */
    @Beta
    CARRIED_HOTBAR,

    /** Single Execution, supports cooldowns.
     * Executes when used. Doesn't have
     * to be used on an entity or block
     */
    USED,

    /** Single Execution, supports cooldowns.
     * Only supports left-click hit. Triggered when successfully
     * hitting an entity.
     */
    HIT,

    @Beta
    CLICKED,

    /** Single Execution, triggers when dropped.
     *
     * BETA: Currently Unimplemented
     */
    @Beta
    DROPPED,

    INTERACT_BLOCK,

    INTERACT_ENTITY,

    PLACE_BLOCK,

    BREAK_BLOCK

}
