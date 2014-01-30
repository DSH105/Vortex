package com.dsh105.vortex.environment.earthquake;

import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.util.GeometryUtil;
import com.dsh105.vortex.environment.Environment;
import com.dsh105.dshutils.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Earthquake extends Environment {

    public Location epicentre;
    public int shakeRadius = 50;
    private int timer = 0;
    private int maxMagnitude = 35;
    private int[] magnitude;
    private float[] fValues = new float[] {0F, 0.05F, 0.1F, 0.15F};

    public ArrayList<ShakeEntity> entities = new ArrayList<ShakeEntity>();

    public Earthquake(Location epicentre, int shakeRadius, int liveTime) {
        super(liveTime);
        this.magnitude = new int[] {0, this.maxMagnitude / 2};
        this.epicentre = epicentre;
        this.shakeRadius = shakeRadius;
        this.start();
    }

    @Override
    public void onLive() {
        super.onLive();
        if (++this.timer >= 10) {
            this.timer = 0;
            for (int i = 0; i < magnitude.length; i++) {
                if (this.magnitude[i]++ < this.maxMagnitude) {
                    for (Location l : GeometryUtil.circle(this.epicentre, this.magnitude[i], 4, true, false, false)) {
                        float[] f = new float[] {this.fValues[StringUtil.r().nextInt(this.fValues.length)], this.fValues[StringUtil.r().nextInt(this.fValues.length)]};
                        for (int j = 0; i < f.length; i++) {
                            if (StringUtil.r().nextBoolean()) {
                                f[j] = -f[j];
                            }
                        }

                        Block b = l.getBlock();
                        FallingBlock fb = this.epicentre.getWorld().spawnFallingBlock(l, b.getType(), b.getData());
                        b.setType(Material.AIR);
                        fb.setVelocity(new Vector(f[0], 0.7, f[1]));
                    }
                } else {
                    this.magnitude[i] = 0;
                }
            }
        }

        /*List<Entity> entityList = GeometryUtil.getNearbyEntities(this.getEpicentre(), this.shakeRadius);
        if (entityList != null && !entityList.isEmpty()) {
            for (Entity e : entityList) {
                this.entities.add(new ShakeEntity(this, e, -1, true));
            }
        }*/

        if (this.age >= this.liveTime) {
            this.end();
        }
    }

    @Override
    public void end() {
        super.end();
        Iterator<ShakeEntity> i = this.entities.iterator();
        while (i.hasNext()) {
            ShakeEntity e = i.next();
            e.cancel();
            i.remove();
        }
    }

    public Location getEpicentre() {
        return this.epicentre;
    }
}