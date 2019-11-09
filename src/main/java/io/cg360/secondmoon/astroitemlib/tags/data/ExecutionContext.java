package io.cg360.secondmoon.astroitemlib.tags.data;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Provides ExecutionType specific data for Tag Processing.
 */
public class ExecutionContext {

    private Player player;

    public ExecutionContext(Player player) { this.player = player; }

    public Player getPlayer() { return player; }
}