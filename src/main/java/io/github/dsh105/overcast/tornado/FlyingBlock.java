package io.github.dsh105.overcast.tornado;

import io.github.dsh105.overcast.Overcast;
import io.github.dsh105.overcast.geometry.Geometry;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Project by DSH105
 */

public class FlyingBlock extends BukkitRunnable {

    public Tornado tornado;

    public FallingBlock fb;
    public double maxY;
    public int age;

    public int floatTicks;
    public Vector lastVelocity;

    public FlyingBlock(FallingBlock fallingBlock, double maxY) {
        this.age = 0;
        this.fb = fallingBlock;
        this.maxY = maxY;
        this.runTaskTimer(Overcast.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        if (this.tornado.grabbedBlocks.contains(this)) {
            Location l1 = new Location(tornado.world, tornado.locX, this.fb.getLocation().getY(), tornado.locZ);
            Location l2 = new Location(tornado.world, tornado.locX, tornado.locY, tornado.locZ);
            Vector v = l2.toVector().subtract(this.fb.getLocation().toVector().normalize().multiply(0.2F));
            this.fb.setVelocity(v);
            this.tornado.grabbedBlocks.remove(this);
            this.tornado.findClosestLayer((int) this.fb.getLocation().getY()).addBlock(this);
        } else {
            BlockLayer layer = this.tornado.findLayer(this);
            if (layer != null) {
                this.fb.setVelocity(this.lastVelocity = new Vector(Math.cos(layer.getOrbitWidth()), this.randY(), Math.sin(layer.getOrbitWidth())));
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    public float randY() {
        return Geometry.generateRandomFloat(0.05F, 0.15F);
    }
}