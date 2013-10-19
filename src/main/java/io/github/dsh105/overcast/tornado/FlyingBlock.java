package io.github.dsh105.overcast.tornado;

import io.github.dsh105.overcast.Overcast;
import io.github.dsh105.overcast.geometry.Geometry;
import io.github.dsh105.overcast.geometry.Operator;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftFallingSand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

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
    public float lastY;
    public double nextY;

    public FlyingBlock(FallingBlock fallingBlock, double maxY) {
        this.age = 0;
        this.fb = fallingBlock;
        this.maxY = maxY;
        this.runTaskTimer(Overcast.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        if (++age > 15 * 20) {

        } else {
            Location l1 = new Location(tornado.world.getWorld(), tornado.locX, this.fb.getLocation().getY(), tornado.locZ);
            if (l1.distance(this.fb.getLocation()) > 6) {
                Location l2 = new Location(tornado.world.getWorld(), tornado.locX, tornado.locY, tornado.locZ);
                Vector v = l2.toVector().subtract(this.fb.getLocation().toVector().normalize().multiply(0.3F));
                this.fb.setVelocity(v);
            } else {
                this.fb.setVelocity(this.lastVelocity = new Vector(Math.cos(this.nextY), this.findY(this.fb.getLocation().getY(), this.maxY), Math.sin(this.nextY)));
                this.nextY += 0.2D;
            }
        }
        /*float motX = (float) (tornado.locX - this.fb.getLocation().getX());
        float motY = (float) (tornado.boundingBox.c + tornado.width / 2.0F - (this.fb.getLocation().getY() + ((CraftFallingSand) this.fb).getHandle().width / 2.0F));
        float motZ = (float) (tornado.locZ - this.fb.getLocation().getZ());*/
    }

    public float findY(double d0, double max) {
        if (++floatTicks < 40) {
            double d1 = max + 1,
                    d2 = max - 1;
            if (d0 >= d1) {
                return this.lastY = -0.1F;
            } else if (d0 <= d2) {
                return this.lastY = 0.3F;
            } else {
                return this.lastY = Geometry.generateRandomFloat(0.3F, -0.3F);
            }
        } else {
            floatTicks = 0;
            return this.lastY;
        }
    }
}