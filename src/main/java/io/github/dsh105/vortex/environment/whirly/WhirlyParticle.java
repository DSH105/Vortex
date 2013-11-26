package io.github.dsh105.vortex.environment.whirly;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.logger.ConsoleLogger;
import io.github.dsh105.vortex.logger.Logger;
import io.github.dsh105.vortex.util.Particle;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WhirlyParticle extends BukkitRunnable {

    public Whirly whirly;
    public Location location;
    public Particle particle;

    public Vector lastVelocity;
    public float shiftV = 0.0F;
    public float shiftH = (float) (Math.random() * Math.PI * 2);

    public WhirlyParticle(Whirly whirly, Location startLocation, Particle particle) {
        this.whirly = whirly;
        this.location = startLocation;
        this.particle = particle;
        this.runTaskTimer(VortexPlugin.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        ConsoleLogger.log("" + this.location.getY() + " : " + this.whirly.maxY);
        if (this.location.getBlockY() >= this.whirly.maxY) {
            this.cancel();
        }

        try {
            this.particle.sendTo(this.location);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.WARNING, "Failed to generate Whirly Cloud particle.", e, true);
        }

        double d0 = Math.sin(this.shiftV < 1.0F ? this.shiftV += 0.05F : this.shiftV);
        float f1 = this.shiftH += 0.8F;
        this.location.add(this.lastVelocity = new Vector(d0 * Math.cos(f1), 0.5D, Math.sin(f1) * d0));
    }
}