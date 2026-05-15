package me.logslow.listener;

import me.logslow.LogSlowPlugin;
import me.logslow.util.LogUtil;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreakCooldownListener implements Listener {

    private final LogSlowPlugin plugin;

    private final Map<UUID, Long> miningStart = new HashMap<>();

    public BreakCooldownListener(LogSlowPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        boolean onlyLogs =
                plugin.getConfig().getBoolean("only_logs", true);

        if (onlyLogs && !LogUtil.isLog(block.getType())) {
            return;
        }

        double multiplier =
                plugin.getConfig().getDouble("slow_multiplier", 2.0);

        long requiredTime = (long) (1000L * multiplier);

        UUID uuid = player.getUniqueId();

        long now = System.currentTimeMillis();

        if (!miningStart.containsKey(uuid)) {

            miningStart.put(uuid, now);

            event.setCancelled(true);

            return;
        }

        long start = miningStart.get(uuid);

        long elapsed = now - start;

        if (elapsed < requiredTime) {

            event.setCancelled(true);

            return;
        }

        miningStart.remove(uuid);
    }
}
