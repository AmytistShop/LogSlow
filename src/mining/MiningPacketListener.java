package me.logslow.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.logslow.LogSlowPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MiningPacketListener {

    private final LogSlowPlugin plugin;

    public MiningPacketListener(LogSlowPlugin plugin) {
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

                        Block block = player.getTargetBlockExact(6);

                        if (block == null) {
                            return;
                        }

                        switch (digType) {

                            case START_DESTROY_BLOCK -> {

                                event.setCancelled(true);

                                plugin.getMiningManager().startMining(player, block);
                            }

                            case ABORT_DESTROY_BLOCK,
                                 STOP_DESTROY_BLOCK -> {

                                plugin.getMiningManager().stopMining(player);
                            }
                        }
                    }
                }
        );
    }
}
