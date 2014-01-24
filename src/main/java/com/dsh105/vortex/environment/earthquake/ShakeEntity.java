package com.dsh105.vortex.environment.earthquake;

import com.dsh105.vortex.VortexPlugin;
import com.dsh105.vortex.environment.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShakeEntity extends BukkitRunnable {

    public Environment environment;
    public Entity entity;
    public float amount = VortexPlugin.r().nextFloat();
    public boolean horizontal;
    public int count = 0;
    public double maxCount;
    public Vector lastEntityVelocity;

    public ShakeEntity(Environment environment, Entity entity, int time, boolean horizontal) {
        this.environment = environment;
        this.entity = entity;
        this.horizontal = horizontal;
        this.maxCount = time * 2;
        this.lastEntityVelocity = entity.getVelocity();
        this.runTaskTimer(VortexPlugin.getInstance(), 0L, 2L);
    }

    @Override
    public void run() {
        if (this.horizontal) {
            this.entity.setVelocity(new Vector(this.amount, 0, 0));
        } else {
            this.entity.setVelocity(new Vector(0, this.amount, 0));
        }
        this.amount = -this.amount;

        if (this.maxCount >= 0 && ++this.count >= this.maxCount) {
            this.cancel();
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.entity.setVelocity(this.lastEntityVelocity);
    }
}