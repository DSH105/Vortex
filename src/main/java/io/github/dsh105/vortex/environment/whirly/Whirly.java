package io.github.dsh105.vortex.environment.whirly;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.Environment;
import io.github.dsh105.vortex.logger.ConsoleLogger;
import io.github.dsh105.vortex.util.Geometry;
import io.github.dsh105.vortex.util.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Whirly extends Environment {

    public Vector direction;
    public Location location;
    public float speed;
    public int height = 8;
    public int maxY;
    public int directionCount = 0;

    public Whirly(Location location, float speed, int liveTime, int height) {
        super(liveTime);
        this.location = location;
        this.direction = new Vector(Geometry.generateRandomFloat(), 0, Geometry.generateRandomFloat());
        this.speed = speed;
        this.height = height;
        this.maxY = this.location.getBlockY() + height;
        if (direction != null) {
            direction.normalize().multiply(this.speed);
        }
        this.start();
    }

    @Override
    public void onLive() {
        super.onLive();

        if (++this.directionCount >= (80 - VortexPlugin.r().nextInt(40))) {
            this.direction = new Vector(Geometry.generateRandomFloat(), 0, Geometry.generateRandomFloat());
            this.directionCount = 0;
        }

        new WhirlyParticle(this, this.location, Particle.WHIRLY_SMOKE);

        if (this.age >= this.liveTime) {
            this.end();
            ConsoleLogger.log("end");
        }
    }
}