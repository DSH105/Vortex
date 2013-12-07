package io.github.dsh105.vortex.util;

import io.github.dsh105.vortex.VortexPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;


public class Geometry {

    public static float generateRandomFloat(float min, float max) {
        float f = min + (VortexPlugin.r().nextFloat() * ((1 + max) - min));
        return VortexPlugin.r().nextBoolean() ? f : -f;
    }

    public static float generateRandomFloat() {
        float f = VortexPlugin.r().nextFloat();
        return VortexPlugin.r().nextBoolean() ? f : -f;
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

    public static List<Entity> getNearbyEntities(Location l, int range) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : l.getWorld().getEntities()) {
            if (isInBorder(l, entity.getLocation(), range)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public static boolean isInBorder(Location center, Location l, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = l.getBlockX(), z1 = l.getBlockZ();
        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }
        return true;
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