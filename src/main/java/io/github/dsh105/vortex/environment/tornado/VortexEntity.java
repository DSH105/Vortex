package io.github.dsh105.vortex.environment.tornado;

import io.github.dsh105.vortex.logger.Logger;
import io.github.dsh105.vortex.util.Particle;
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
        this.entity.setMetadata("tornado", new FixedMetadataValue(io.github.dsh105.vortex.VortexPlugin.getInstance(), "true"));
        if (entity instanceof FallingBlock) {
            ((FallingBlock) entity).setDropItem(false);
        }
        this.runTaskTimer(io.github.dsh105.vortex.VortexPlugin.getInstance(), 1L, 1L);
    }

    @Override
    public void run() {
        age++;
        if (this.entity.getLocation().getY() >= this.tornado.maxY) {
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

        if (io.github.dsh105.vortex.VortexPlugin.r().nextInt(3) == 0 && !this.entity.isDead()) {
            if (this.entityType.equals(VortexEntityType.BLOCK)) {
                /*try {
                    Particle.CLOUD.sendTo(this.entity.getLocation());
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Tornado Cloud particle.", e, true);
                }*/
            } else if (this.entityType.equals(VortexEntityType.ITEM)) {
                Particle p = io.github.dsh105.vortex.VortexPlugin.r().nextInt(3) == 0 ? Particle.WHIRLY_SMOKE : Particle.WHIRLY_CLOUD;
                try {
                    p.sendTo(this.entity.getLocation(), new Vector(0, 0, 0));
                } catch (Exception e) {
                    Logger.log(Logger.LogLevel.WARNING, "Failed to generate Tornado Cloud particle.", e, true);
                }
            }
        }

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
    public void cancel() {
        super.cancel();
        this.entity.removeMetadata("tornado", io.github.dsh105.vortex.VortexPlugin.getInstance());
    }

    public enum VortexEntityType {
        BLOCK, ENTITY, ITEM;
    }
}