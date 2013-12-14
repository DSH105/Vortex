package io.github.dsh105.vortex.environment.whirly;

import io.github.dsh105.dshutils.Particle;
import io.github.dsh105.dshutils.util.GeometryUtil;
import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.Environment;
import io.github.dsh105.dshutils.logger.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Whirly extends Environment {

    public Vector direction;
    public Location location;
    public float speed;
    public int height = 8;
    public int maxY;
    public int directionCount = 0;
    public ArrayList<WhirlyParticle> particles = new ArrayList<WhirlyParticle>();

    public Whirly(Location location, float speed, int liveTime, int height) {
        super(liveTime);
        this.location = location;
        this.direction = new Vector(GeometryUtil.generateRandomFloat(), 0, GeometryUtil.generateRandomFloat());
        this.speed = speed;
        this.height = height;
        this.maxY = this.location.getBlockY() + height;
        if (direction != null) {
            direction.normalize().multiply(this.speed);
        }
        this.start();
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLive() {
        super.onLive();

        if (this.direction != null) {
            this.location.add(this.direction);
        }

        if (++this.directionCount >= (80 - VortexPlugin.r().nextInt(40))) {
            this.direction = new Vector(GeometryUtil.generateRandomFloat(), GeometryUtil.generateRandomFloat(), GeometryUtil.generateRandomFloat());
            this.direction.normalize().multiply(this.speed);
            this.directionCount = 0;
        }

        try {
            Particle.CLOUD.sendTo(this.location, new Vector(0, 0, 0), 0F, 1);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Failed to generate Whirly Cloud particle.", e, true);
        }

        List<Entity> entityList = GeometryUtil.getNearbyEntities(this.location, 2);
        if (entityList != null && !entityList.isEmpty()) {
            for (Entity e : entityList) {
                e.setVelocity(new Vector(0, 0.4, 0));
                Location l = e.getLocation().clone();
                l.setY(l.getY() - 1D);
                int id = l.getBlock().getTypeId();
                try {
                    Particle.BLOCK_DUST.sendDataParticle(e.getLocation(), id == 0 ? Material.WEB.getId() : id, (int) l.getBlock().getData());
                } catch (Exception ex) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Whirly Block Dust particle.", ex, true);
                }
            }
        }

        //this.particles.add(new WhirlyParticle(this, this.location, Particle.WHIRLY_CLOUD));

        if (this.age >= this.liveTime) {
            this.end();
        }
    }

    @Override
    public void end() {
        super.end();
        Iterator<WhirlyParticle> i = this.particles.iterator();
        while (i.hasNext()) {
            i.next().cancel();
        }
    }
}