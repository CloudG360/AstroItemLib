package io.cg360.secondmoon.astroitemlib.tags;

public enum TagPriority {

    /**
     * Cooldown should be reserved for Cooldown's or
     * other tag controlling behaviors only.
     */
    COOLDOWN(100),
    HIGHEST(10),
    HIGH(7),
    NORMAL(5),
    LOW(3),
    LOWEST(0);

    private final int priority;
    private TagPriority (int priority) {
        this.priority = priority;
    }

    /**
     * @return Returns an int indicating a tag's level.
     */
    public int getIntegerPriority() { return priority; }
}
