package io.github.dsh105.vortex.environment.volcano;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.Environment;
import io.github.dsh105.vortex.environment.earthquake.ShakeEntity;
import io.github.dsh105.vortex.environment.tornado.VortexEntity;
import io.github.dsh105.vortex.logger.ConsoleLogger;
import io.github.dsh105.vortex.logger.Logger;
import io.github.dsh105.vortex.util.Geometry;
import io.github.dsh105.vortex.util.Particle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Volcano extends Environment {

    public static int volcanoes = 0;
    public int id;

    public Location location;
    public boolean isBuilding;
    public int height;
    public int maxY;
    public int lavaCount;
    protected ArrayDeque<FallingBlock> lavaBlocks = new ArrayDeque<FallingBlock>();

    public ArrayList<ShakeEntity> entities = new ArrayList<ShakeEntity>();

    public Volcano(Location location, int height, int liveTime, boolean build) {
        super(liveTime);
        this.id = volcanoes++;
        this.location = location;
        this.location.setY(location.getWorld().getHighestBlockYAt(location));
        this.height = height;
        this.maxY = this.location.getBlockY() + height;
        if (build) {
            new BuildTask(this);
            List<Entity> entityList = Geometry.getNearbyEntities(this.getLocation(), 50);
            if (entityList != null && !entityList.isEmpty()) {
                for (Entity e : entityList) {
                    this.entities.add(new ShakeEntity(this, e, this.height * 4, false));
                    if (e instanceof Player) {
                        ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*10, 1, false));
                    }
                }
            }
        }
        this.start();
    }

    public Location getLocation() {
        return this.location;
    }

    public Location getSpewLocation() {
        Location l = this.getLocation().clone();
        l.setY(this.maxY);
        return l;
    }

    @Override
    public void onLive() {
        super.onLive();

        if (!this.lavaBlocks.isEmpty()) {
            Iterator<FallingBlock> i = this.lavaBlocks.iterator();
            while (i.hasNext()) {
                try {
                    Particle.FIRE.sendTo(i.next().getLocation());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Volcano Explode particle.", e, true);
                }
            }
        }
        if (!this.isBuilding && ++this.lavaCount >= 20 && VortexPlugin.r().nextBoolean()) {
            this.lavaCount = 0;
            FallingBlock fb = this.getLocation().getWorld().spawnFallingBlock(this.getSpewLocation(), Material.LAVA, (byte) 0);
            fb.setVelocity(new Vector(Geometry.generateRandomFloat(), 0.7F, Geometry.generateRandomFloat(0.02F, 0.15F)));
            fb.setDropItem(false);
            try {
                Particle.HUGE_EXPLOSION.sendTo(fb.getLocation());
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.WARNING, "Failed to generate Volcano Explode particle.", e, true);
            }
            this.getLocation().getWorld().playSound(this.getLocation(), Sound.WITHER_SHOOT, 10F, 1F);
        }

        if (this.age >= this.liveTime) {
            this.end();
        }
    }

    public void saveVolcano() {

    }
}