package io.github.dsh105.overcast;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project by DSH105
 */

public class Overcast extends JavaPlugin {

    private static Overcast instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static Overcast getInstance() {
        return instance;
    }
}