package io.github.dsh105.overcast.environment.whirly;

import io.github.dsh105.overcast.Overcast;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Project by DSH105
 */

public class SpinEntity extends BukkitRunnable {

    Random r = new Random();

    public int maxY;
    public Entity e;
    public float spiral = 0.1F;

    public SpinEntity(Entity e, int maxY) {
        this.e = e;
        this.maxY = maxY;
        this.runTaskTimer(Overcast.getInstance(), 0L, 1L);
    }

    @Override
    public void run() {
        if (this.e.getLocation().getY() > this.maxY) {
            this.e.setVelocity(new Vector(this.r.nextFloat() / 6, 0.85F, this.r.nextFloat() / 6));
            this.cancel();
        } else {
            this.e.setVelocity(new Vector(Math.cos(this.spiral), 0.3, Math.sin(this.spiral)));
            this.spiral += 0.1;
        }
    }
}