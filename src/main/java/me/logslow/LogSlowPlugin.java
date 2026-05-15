package me.logslow;

import me.logslow.listener.BreakCooldownListener;

import org.bukkit.plugin.java.JavaPlugin;

public class LogSlowPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(
                new BreakCooldownListener(this),
                this
        );

        getLogger().info("LogSlow enabled.");
    }
}
