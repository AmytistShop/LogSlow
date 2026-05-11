package me.logslow.listener;

import me.logslow.LogSlowPlugin;
import me.logslow.util.LogUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MiningListener implements Listener {

    private final LogSlowPlugin plugin;

    public MiningListener(LogSlowPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(BlockDamageEvent event) {

        Player player = event.getPlayer();

        Material material = event.getBlock().getType();

        boolean onlyLogs = plugin.getConfig().getBoolean("only_logs", true);

        if (onlyLogs && !LogUtil.isLog(material)) {
            return;
        }

        double multiplier = plugin.getConfig().getDouble("slow_multiplier", 2.0);

        int amplifier = calculateAmplifier(multiplier);

        PotionEffect effect = new PotionEffect(
                PotionEffectType.SLOW_DIGGING,
                20,
                amplifier,
                false,
                false,
                false
        );

        player.addPotionEffect(effect, true);
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
