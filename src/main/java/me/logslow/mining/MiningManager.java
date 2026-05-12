package me.logslow.mining;

import me.logslow.LogSlowPlugin;
import me.logslow.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MiningManager {

    private final LogSlowPlugin plugin;

    private final Map<UUID, MiningSession> sessions = new HashMap<>();

    public MiningManager(LogSlowPlugin plugin) {
        this.plugin = plugin;

        startTickLoop();
    }

    public void startMining(Player player, Block block) {

        boolean onlyLogs = plugin.getConfig().getBoolean("only_logs", true);

        if (onlyLogs && !LogUtil.isLog(block.getType())) {
            return;
        }

        sessions.put(player.getUniqueId(), new MiningSession(player, block));
    }

    public void stopMining(Player player) {

        sessions.remove(player.getUniqueId());
    }

    private void startTickLoop() {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            Iterator<MiningSession> iterator = sessions.values().iterator();

            while (iterator.hasNext()) {

                MiningSession session = iterator.next();

                Player player = session.getPlayer();
                Block block = session.getBlock();

                if (!player.isOnline()) {
                    iterator.remove();
                    continue;
                }

                if (block.getType() == Material.AIR) {
                    iterator.remove();
                    continue;
                }

                double multiplier =
                        plugin.getConfig().getDouble("slow_multiplier", 2.0);

                double speed = 0.05 / multiplier;

                session.addProgress(speed);

                int stage = (int) Math.floor(session.getProgress() * 10);

                if (stage > 9) {
                    stage = 9;
                }

                player.sendBlockDamage(
                        block.getLocation(),
                        stage / 9f
                );

                if (session.getProgress() >= 1.0) {

                    block.breakNaturally(
                            player.getInventory().getItemInMainHand()
                    );

                    block.getWorld().playSound(
                            block.getLocation(),
                            Sound.BLOCK_WOOD_BREAK,
                            1f,
                            1f
                    );

                    iterator.remove();
                }
            }

        }, 1L, 1L);
    }
}
