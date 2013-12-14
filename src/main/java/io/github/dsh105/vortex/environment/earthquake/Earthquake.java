package io.github.dsh105.vortex.environment.earthquake;

import io.github.dsh105.dshutils.util.GeometryUtil;
import io.github.dsh105.vortex.environment.Environment;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Earthquake extends Environment {

    public Location hypocentre;
    public int shakeRadius = 50;
    public int liveTime;

    public ArrayList<ShakeEntity> entities = new ArrayList<ShakeEntity>();

    public Earthquake(Location hypocentre, int shakeRadius, int liveTime) {
        super(liveTime);
        this.hypocentre = hypocentre;
        this.shakeRadius = shakeRadius;
        this.start();
    }

    @Override
    public void onLive() {
        super.onLive();
        List<Entity> entityList = GeometryUtil.getNearbyEntities(this.getHypocentre(), this.shakeRadius);
        if (entityList != null && !entityList.isEmpty()) {
            for (Entity e : entityList) {
                this.entities.add(new ShakeEntity(this, e, -1, true));
            }
        }

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

    public Location getHypocentre() {
        return this.hypocentre;
    }
}