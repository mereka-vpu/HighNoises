package com.nzst.highnoiseworlds;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class HighNoiseWorlds extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("HighNoiseWorlds plugin has been enabled!");
        getLogger().info("Registering custom world generator 'HighNoisx' for Multiverse-Core.");
    }

    @Override
    public void onDisable() {
        getLogger().info("HighNoiseWorlds plugin has been disabled!");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new HighNoiseGenerator();
    }
}
