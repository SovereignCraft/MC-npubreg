package com.sovereigncraft.mcnpubreg;

import org.bukkit.plugin.java.JavaPlugin;
import com.sovereigncraft.mcnpubreg.Commands.*;
import lombok.SneakyThrows;

public final class MC_npubreg extends JavaPlugin {
    private static MC_npubreg instance;
    @Override
    @SneakyThrows
    public void onEnable() {

        // Plugin startup logic
        instance = this;
        this.getCommand("npubreg").setExecutor(new npubregCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
