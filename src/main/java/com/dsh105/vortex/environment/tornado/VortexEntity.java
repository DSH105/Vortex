package com.dsh105.vortex.environment.tornado;

import com.dsh105.vortex.VortexPlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class VortexEntity extends BukkitRunnable {

    public Tornado tornado;
    public VortexEntityType entityType;

    public boolean remove = false;
    public Entity entity;
    public int age = 0;
    public float shiftV = 0.0F;
    public float shiftH = (float) (Math.random() * Math.PI * 2);
    public Vector lastVelocity;

    public VortexEntity(Tornado tornado, Entity entity, boolean remove, VortexEntityType type) {
        this.tornado = tornado;
        this.entity = entity;
        this.remove = remove;
        this.entityType = type;
        this.entity.setMetadata("tornado", new FixedMetadataValue(VortexPlugin.getInstance(), "true"));
        if (entity instanceof FallingBlock) {
            ((FallingBlock) entity).setDropItem(false);
        }
        this.runTaskTimer(VortexPlugin.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        if (++this.age >= 20*6 || this.entity.getLocation().getY() >= this.tornado.maxY) {
            if (this.entityType == VortexEntityType.BLOCK) {
                this.tornado.removeBlock(this);
            }
            else {
                this.tornado.removeMob(this);
            }
        }
        double d0 = Math.sin(this.shiftV < 1.0F ? this.shiftV += 0.05F : this.shiftV);
        float f1 = this.shiftH += 0.8F;
        this.entity.setVelocity(this.lastVelocity = new Vector(d0 * Math.cos(f1), 0.5D, Math.sin(f1) * d0));

        if (this.entityType == VortexEntityType.BLOCK) {
            Block b = this.entity.getLocation().add(this.lastVelocity).getBlock();
            if (b.getType() != Material.AIR) {
                this.tornado.blocks.add(new VortexEntity(this.tornado, this.tornado.getWorld().spawnFallingBlock(this.tornado.getLocation(), b.getType(), b.getData()), false, VortexEntityType.BLOCK));
                //this.lastBlock = new BlockData(b.getType(), b.getData());
                b.setType(Material.AIR);
            }
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.entity.removeMetadata("tornado", VortexPlugin.getInstance());
    }

    public enum VortexEntityType {
        BLOCK, ENTITY
    }
}