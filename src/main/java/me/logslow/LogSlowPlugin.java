package me.logslow;

import me.logslow.listener.MiningPacketListener;
import me.logslow.mining.MiningManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LogSlowPlugin extends JavaPlugin {

    private static LogSlowPlugin instance;

    private MiningManager miningManager;

    public static LogSlowPlugin getInstance() {
        return instance;
    }

    public MiningManager getMiningManager() {
        return miningManager;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        miningManager = new MiningManager(this);

        new MiningPacketListener(this);

        getLogger().info("Advanced mining engine enabled.");
    }
}
