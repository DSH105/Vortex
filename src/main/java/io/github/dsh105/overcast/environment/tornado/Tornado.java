package io.github.dsh105.overcast.environment.tornado;

import io.github.dsh105.overcast.Overcast;
import io.github.dsh105.overcast.environment.Environment;
import io.github.dsh105.overcast.geometry.Direction;
import io.github.dsh105.overcast.geometry.Geometry;
import io.github.dsh105.overcast.logger.Logger;
import io.github.dsh105.overcast.util.Particle;
import io.github.dsh105.overcast.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Project by DSH105
 */

public class Tornado extends Environment {

    public World world;
    public double locX;
    public double locY;
    public double locZ;
    public Vector direction;
    public Location location;
    public float speed;
    public int liveTime;
    public int maxY = 50;
    public int maxBlocks = 70;
    public ArrayDeque<FlyingBlock> blocks = new ArrayDeque<FlyingBlock>();
    public HashSet<FlyingBlock> grabbedBlocks = new HashSet<FlyingBlock>();

    public Tornado(Location location, Vector direction, float speed, int liveTime, int maximumY, int maximumBlocks) {
        this.location = location;
        this.direction = direction;
        this.speed = speed;
        this.liveTime = liveTime;
        this.maxY = maximumY;
        this.maxBlocks = maximumBlocks;
        direction.normalize().multiply(this.speed);
        for (int i = 0; i < this.maxBlocks / (this.maxBlocks / 10); i++) {
            this.blocks.add(new FlyingBlock(this, this.world.spawnFallingBlock(this.getLocation(), Material.DIRT, (byte) 0)));
        }
        this.start();
    }

    @Override
    public void onLive() {
        if (this.direction != null) {
            this.location.add(this.direction);
        }
        if (Overcast.r().nextBoolean()) {
            List<Location> list = Geometry.circle(this.getLocation(), 20, this.maxY / 2, false, false, false);
            Block b = list.get(Overcast.r().nextInt(list.size())).getBlock();
            FlyingBlock fb = new FlyingBlock(this, this.world.spawnFallingBlock(b.getLocation(), b.getType(), b.getData()));
            this.grabbedBlocks.add(fb);
        }

        if (this.blocks.size() > this.maxBlocks) {
            this.removeBlock(blocks.getFirst());
        }

        if (this.age >= this.liveTime) {
            this.dissipate();
        }
    }

    public void removeBlock(FlyingBlock flyingBlock) {
        flyingBlock.cancel();
        this.blocks.remove(flyingBlock);
    }

    public Location getLocation() {
        return new Location(this.world, this.locX, this.locY, this.locZ);
    }

    public void dissipate() {
        this.end();
        Iterator<FlyingBlock> i = this.blocks.iterator();
        while (i.hasNext()) {
            FlyingBlock fb = i.next();
            this.removeBlock(fb);
            try {
                Particle.CLOUD.sendTo(fb.fb.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Failed to generate Tornado Cloud particle.", e, true);
            }
        }
    }
}