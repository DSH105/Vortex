package io.github.dsh105.vortex.environment.volcano;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.logger.ConsoleLogger;
import io.github.dsh105.vortex.logger.Logger;
import io.github.dsh105.vortex.util.Geometry;
import io.github.dsh105.vortex.util.Particle;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BuildTask extends BukkitRunnable {

    public Volcano volcano;
    public Location buildLocation;
    public int builtLayers = 0;
    public int minRadius = 4;
    public int buildRadius;
    public int maxRadius;
    public int height;

    public BuildTask(Volcano volcano) {
        this.volcano = volcano;
        this.buildLocation = this.volcano.getLocation().clone();
        this.height = this.volcano.height;
        this.maxRadius = this.minRadius + this.height;
        this.buildRadius = this.minRadius;
        this.volcano.isBuilding = true;
        this.runTaskTimer(VortexPlugin.getInstance(), 10L, 10L);
    }

    @Override
    public void run() {
        // Are we done yet?
        if (this.builtLayers > this.height) {
            for (Location location : Geometry.circle(this.volcano.getLocation(), 2, this.builtLayers, false, false, true)) {
                location.getBlock().setType(Material.LAVA);
            }
            this.cancel();
        }

        if (this.builtLayers > 0) {
            for (int i = this.height; i >= 0; i--) {
                Location l = this.buildLocation.clone();
                l.setY(this.buildLocation.getY() + i);
                for (Location l2 : Geometry.circle(l, this.maxRadius, 1, false, false, false)) {
                    Block b = l2.getBlock();
                    if (b.hasMetadata("volcano-" + this.volcano.id)) {
                        Location l3 = b.getLocation().clone();
                        l3.setY(l2.getY() + 1);
                        Block b1 = l3.getBlock();
                        b1.setTypeIdAndData(b.getTypeId(), b.getData(), false);
                        b.removeMetadata("volcano-" + this.volcano.id, VortexPlugin.getInstance());
                        b1.setMetadata("volcano-" + this.volcano.id, new FixedMetadataValue(VortexPlugin.getInstance(), "true"));
                        b.setType(Material.AIR);
                    }
                }
            }
        }

        // Generate a new layer
        for (Location location : Geometry.circle(this.buildLocation, this.buildRadius, 1, false, false, true)) {
            if (VortexPlugin.r().nextInt(5) <= 2) {
                Material m = VortexPlugin.r().nextInt(3) == 2 ? Material.COAL_BLOCK : VortexPlugin.r().nextBoolean() ? Material.OBSIDIAN : Material.STONE;
                Block block = location.getBlock();
                block.setType(m);
                block.setMetadata("volcano-" + this.volcano.id, new FixedMetadataValue(VortexPlugin.getInstance(), "true"));
                /*try {
                    Particle.BLOCK_BREAK.sendBlockBreak(location, m.getId(), b.getData());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Volcano Block Break particle.", e, true);
                }*/
            }
        }
        this.buildLocation.getWorld().playSound(this.buildLocation, Sound.ENDERDRAGON_HIT, 10F, 1F);

        this.buildRadius++;
        this.builtLayers++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.volcano.isBuilding = false;
    }
}