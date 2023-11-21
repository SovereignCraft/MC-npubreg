package com.sovereigncraft.mcnpubreg;

import org.bukkit.plugin.java.JavaPlugin;
import com.sovereigncraft.mcnpubreg.Commands.npubregCommand;

public final class MC_npubreg extends JavaPlugin {
    private static MC_npubreg instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register commands
        this.getCommand("npubreg").setExecutor(new npubregCommand());

        // Any other startup logic
        getLogger().info("MC_npubreg has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MC_npubreg has been disabled.");
    }

    public static MC_npubreg getInstance() {
        return instance;
    }
}
