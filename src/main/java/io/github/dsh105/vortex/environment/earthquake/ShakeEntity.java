package io.github.dsh105.vortex.environment.earthquake;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.util.Geometry;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShakeEntity extends BukkitRunnable {

    public Earthquake earthquake;
    public Entity entity;

    public ShakeEntity(Earthquake earthquake, Entity entity) {
        this.earthquake = earthquake;
        this.entity = entity;
        this.runTaskTimer(VortexPlugin.getInstance(), 0L, 10L);
    }

    @Override
    public void run() {
        this.entity.setVelocity(new Vector(Geometry.generateRandomFloat(0.05F, 0.2F), 0, Geometry.generateRandomFloat(0.05F, 0.2F)));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}