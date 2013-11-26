package io.github.dsh105.vortex.environment.earthquake;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.Environment;
import io.github.dsh105.vortex.util.Geometry;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShakeEntity extends BukkitRunnable {

    public Environment environment;
    public Entity entity;
    public float amount;
    public boolean reverse;
    public boolean horizontal;
    public int count = 0;
    public double maxCount;

    public ShakeEntity(Environment environment, Entity entity, int time, boolean horizontal) {
        this.entity = entity;
        this.entity = entity;
        this.horizontal = horizontal;
        this.maxCount = time / 2;
        this.runTaskTimer(VortexPlugin.getInstance(), 0L, 10L);
    }

    @Override
    public void run() {
        if (this.count >= this.maxCount) {
            this.cancel();
        }
        if (this.horizontal) {
            this.entity.setVelocity(new Vector(this.amount, 0, 0));
        } else {
            this.entity.setVelocity(new Vector(0, this.amount, 0));
        }
        this.reverse = !this.reverse;
        this.amount = -this.amount;
        count++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}