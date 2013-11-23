package io.github.dsh105.overcast.environment.whirly;

import io.github.dsh105.overcast.logger.Logger;
import io.github.dsh105.overcast.util.Particle;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

import java.util.Iterator;
import java.util.List;

/**
 * Project by DSH105
 */

public class Whirly extends Entity {

    public int maxY;

    public float speed;
    public float angle;
    public float curve;

    public Entity target;

    public Whirly(Location loc, int height) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), height);
    }

    public Whirly(World world, double locX, double locY, double locZ, int height) {
        super(((CraftWorld) world).getHandle());
        this.a(0.6F, 0.8F);
        this.locX = locX;
        this.locY = locY;
        this.locZ = locZ;
        this.maxY = height;
        this.angle = (this.random.nextFloat() * 360.0F);
        this.speed = (this.random.nextFloat() * 0.025F + 0.025F);
        this.curve = ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
    }

    @Override
    protected void a() {}

    @Override
    public void l_() {
        super.l_();
        if (this.target == null) {
            this.motX = (Math.cos(0.01745329F * this.angle) * this.speed);
            this.motZ = (Math.cos(0.01745329F * this.angle) * this.speed);
        }

        for (int i = 1; i < this.height; i++) {
            double x = this.locX + (0.1 * i);
            double y = this.locY + 0.125F;
            double z = this.locZ + (0.1 * i);
            Particle particle = (this.random.nextInt(4) == 0) ? Particle.WHIRLY_SMOKE : Particle.WHIRLY_CLOUD;
            try {
                particle.sendTo(this.world.getWorld(), x, y, z);
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.SEVERE, "Whirly particle effect creation has failed :(.", e, true);
            }
        }

        this.whirl(this.world.getEntities(this, this.boundingBox.grow(2.0D, 1.0D, 2.0D)));
    }

    private void whirl(List<Entity> list) {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Entity e = (Entity) i.next();
            if (e instanceof EntityLiving) {
                new SpinEntity(e.getBukkitEntity(), this.maxY);
            }
        }
    }

    public Location getLocation() {
        return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    protected void a(NBTTagCompound nbtTagCompound) {
        this.speed = nbtTagCompound.getFloat("Speed");
        this.angle = nbtTagCompound.getFloat("Angle");
        this.curve = nbtTagCompound.getFloat("Curve");
        this.maxY = nbtTagCompound.getInt("MaxY");
    }

    @Override
    protected void b(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setFloat("Speed", this.speed);
        nbtTagCompound.setFloat("Angle", this.angle);
        nbtTagCompound.setFloat("Curve", this.curve);
        nbtTagCompound.setInt("MaxY", this.maxY);
    }
}