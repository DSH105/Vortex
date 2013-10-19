package io.github.dsh105.overcast;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;

/**
 * Project by DSH105
 */

public class Hook {

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Overcast.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}