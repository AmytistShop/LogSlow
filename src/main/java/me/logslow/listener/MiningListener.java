package me.logslow.listener;

import me.logslow.LogSlowPlugin;
import me.logslow.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MiningListener implements Listener {

    private final LogSlowPlugin plugin;

    public MiningListener(LogSlowPlugin plugin) {
        this.plugin = plugin;

        startTask();
    }

    private void startTask() {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            double multiplier = plugin.getConfig().getDouble("slow_multiplier", 2.0);

            int amplifier = calculateAmplifier(multiplier);

            for (Player player : Bukkit.getOnlinePlayers()) {

                Block target = player.getTargetBlockExact(5);

                if (target == null) {
                    remove(player);
                    continue;
                }

                Material material = target.getType();

                boolean onlyLogs = plugin.getConfig().getBoolean("only_logs", true);

                if (onlyLogs && !LogUtil.isLog(material)) {
                    remove(player);
                    continue;
                }

                PotionEffect effect = new PotionEffect(
                        PotionEffectType.SLOW_DIGGING,
                        10,
                        amplifier,
                        false,
                        false,
                        false
                );

                player.addPotionEffect(effect, true);
            }

        }, 1L, 1L);
    }

    private void remove(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    }

    private int calculateAmplifier(double multiplier) {

        if (multiplier <= 1.2) {
            return 0;
        }

        if (multiplier <= 1.8) {
            return 1;
        }

        if (multiplier <= 2.8) {
            return 2;
        }

        if (multiplier <= 4.0) {
            return 3;
        }

        return 4;
    }
}
