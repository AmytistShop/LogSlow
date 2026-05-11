package me.logslow;

import me.logslow.listener.PacketMiningListener;
import org.bukkit.plugin.java.JavaPlugin;

public class LogSlowPlugin extends JavaPlugin {

    private static LogSlowPlugin instance;

    public static LogSlowPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        new PacketMiningListener(this);

        getLogger().info("LogSlow enabled.");
    }

    @Override
    public void onDisable() {

        getLogger().info("LogSlow disabled.");
    }
}
