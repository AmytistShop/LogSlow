package me.logslow.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.logslow.LogSlowPlugin;
import me.logslow.util.LogUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PacketMiningListener {

    private final LogSlowPlugin plugin;

    public PacketMiningListener(LogSlowPlugin plugin) {
        this.plugin = plugin;

        register();
    }

    private void register() {

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Client.BLOCK_DIG) {

                    @Override
                    public void onPacketReceiving(PacketEvent event) {

                        Player player = event.getPlayer();

                        EnumWrappers.PlayerDigType digType =
                                event.getPacket().getPlayerDigTypes().read(0);

                        if (digType != EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                            return;
                        }

                        Block block = player.getTargetBlockExact(6);

                        if (block == null) {
                            return;
                        }

                        Material material = block.getType();

                        boolean onlyLogs =
                                plugin.getConfig().getBoolean("only_logs", true);

                        if (onlyLogs && !LogUtil.isLog(material)) {
                            return;
                        }

                        double multiplier =
                                plugin.getConfig().getDouble("slow_multiplier", 2.0);

                        int amplifier = calculateAmplifier(multiplier);

                        PotionEffect effect = new PotionEffect(
                                PotionEffectType.SLOW_DIGGING,
                                25,
                                amplifier,
                                false,
                                false,
                                false
                        );

                        player.addPotionEffect(effect, true);
                    }
                }
        );
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
