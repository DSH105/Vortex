package io.github.dsh105.vortex.environment.volcano;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.Environment;
import io.github.dsh105.vortex.logger.ConsoleLogger;
import io.github.dsh105.vortex.util.Geometry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;


public class Volcano extends Environment {

    public Location location;
    public boolean isBuilding;
    public int height;
    public int maxY;

    public Volcano(Location location, int height, int liveTime, boolean build) {
        super(liveTime);
        this.location = location;
        this.location.setY(location.getWorld().getHighestBlockYAt(location));
        this.height = height;
        this.maxY = this.location.getBlockY() + height;
        if (build) {
            new BuildTask(this);
        }
        this.start();
    }

    public Location getLocation() {
        return this.location;
    }

    public Location getSpewLocation() {
        Location l = this.getLocation().clone();
        l.setY(this.maxY);
        return l;
    }

    @Override
    public void onLive() {
        super.onLive();
        if (!this.isBuilding && VortexPlugin.r().nextBoolean()) {
            FallingBlock fb = this.getLocation().getWorld().spawnFallingBlock(this.getSpewLocation(), Material.LAVA, (byte) 0);
            fb.setVelocity(new Vector(Geometry.generateRandomFloat(), Geometry.generateRandomFloat(), Geometry.generateRandomFloat(0.02F, 0.15F)));
        }

        if (this.age >= this.liveTime) {
            this.end();
        }
    }

    public void saveVolcano() {

    }
}