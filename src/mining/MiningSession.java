package me.logslow.mining;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MiningSession {

    private final Player player;
    private final Block block;

    private double progress;

    public MiningSession(Player player, Block block) {
        this.player = player;
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public double getProgress() {
        return progress;
    }

    public void addProgress(double amount) {
        this.progress += amount;
    }
}
