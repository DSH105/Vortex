package io.github.dsh105.overcast.environment.tornado;

import io.github.dsh105.overcast.Overcast;
import io.github.dsh105.overcast.geometry.Direction;
import io.github.dsh105.overcast.geometry.Geometry;
import io.github.dsh105.overcast.logger.Logger;
import io.github.dsh105.overcast.util.Particle;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Project by DSH105
 */

public class FlyingBlock extends BukkitRunnable {

    public Tornado tornado;

    public FallingBlock fb;
    public int age = 0;
    public float shiftV = 0.0F;
    public float shiftH = (float) (Math.random() * Math.PI * 2);
    public Vector lastVelocity;

    public FlyingBlock(Tornado tornado, FallingBlock fallingBlock) {
        this.tornado = tornado;
        this.fb = fallingBlock;
        this.runTaskTimer(Overcast.getInstance(), 1L, 1L);
        this.fb.setMetadata("tornado", new FixedMetadataValue(Overcast.getInstance(), "true"));
    }

    @Override
    public void run() {
        age++;
        if (this.tornado.grabbedBlocks.contains(this)) {
            if (Geometry.nearby(fb.getLocation(), this.tornado.getLocation(), 1)) {
                this.tornado.grabbedBlocks.remove(this);
                this.tornado.blocks.add(this);
            }
            this.fb.setVelocity(this.lastVelocity = this.tornado.getLocation().toVector().subtract(this.fb.getLocation().toVector().normalize().multiply(0.2F)));
        } else {
            if (this.fb.getLocation().getY() >= this.tornado.maxY) {
                this.tornado.removeBlock(this);
            }
            double d0 = Math.sin(this.shiftV < 1.0F ? this.shiftV += 0.05F : this.shiftV);
            float f1 = this.shiftH += 0.8F;
            this.fb.setVelocity(this.lastVelocity = new Vector(d0 * Math.cos(f1), 0.5D, Math.sin(f1) * d0));

            if (Overcast.r().nextInt(3) == 0) {
                try {
                    Particle.CLOUD.sendTo(this.fb.getLocation());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Tornado Cloud particle.", e, true);
                }
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        this.fb.removeMetadata("tornado", Overcast.getInstance());
    }
}