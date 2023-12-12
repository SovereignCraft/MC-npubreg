package com.sovereigncraft.mcnpubreg;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
    private static FileConfiguration getConfig() {
        return MC_npubreg.getInstance().getConfig();
    }
    public static String getDomain() { return getConfig().getString("domain"); }
    public static String getSecret() { return getConfig().getString("secret"); }
}
