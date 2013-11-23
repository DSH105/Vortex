package io.github.dsh105.overcast.geometry;

import io.github.dsh105.overcast.Overcast;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project by DSH105
 */

public class Geometry {

    public static float generateRandomFloat(float min, float max) {
        return min + (Overcast.r().nextFloat() * ((1 + max) - min));
    }

    public static boolean nearby(Location l1, Location l2, int radius) {
        for (int x = l2.getBlockX() - radius; x <= l2.getBlockX() + radius; x++) {
            for (int y = l2.getBlockY() - radius; y <= l2.getBlockY() + radius; y++) {
                for (int z = l2.getBlockZ() - radius; z <= l2.getBlockZ() + radius; z++) {
                    if (l1.getWorld().getName().equals(l2.getWorld().getName())
                            && l1.getBlockX() == l2.getBlockX()
                            && l1.getBlockY() == l2.getBlockY()
                            &&l1.getBlockZ() == l2.getBlockZ()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Location> circle(Location loc, int r, int h, boolean hollow, boolean sphere, boolean includeAir) {
        List<Location> blocks = new ArrayList<Location>();
        int cx = loc.getBlockX(),
                cy = loc.getBlockY(),
                cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        if (!includeAir && l.getBlock().getType() == Material.AIR) {
                            continue;
                        }
                        blocks.add(l);
                    }
                }
        return blocks;
    }
}