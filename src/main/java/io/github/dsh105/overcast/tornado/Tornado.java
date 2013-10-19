package io.github.dsh105.overcast.tornado;

import io.github.dsh105.overcast.geometry.Direction;
import io.github.dsh105.overcast.logger.Logger;
import io.github.dsh105.overcast.util.ReflectionUtil;
import net.minecraft.server.MathHelper;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.HashSet;

/**
 * Project by DSH105
 */

public class Tornado extends Entity {

    public Position position;
    public double posX;
    public double posY;
    public double posZ;
    public float motX;
    public float motZ;
    public Direction direction;
    public float speed;
    public float strength;
    public int age;
    public boolean dying;
    public boolean spawning;
    public int invisTicks;
    public int spawnTicks;
    public int dyingTicks;
    public int pickupRadius = 20;
    public int pickupCounter;
    public int pickupChance = 350;
    public int launchCounter;
    public int maxY = 80;
    public int dirChange;
    public HashSet<FlyingBlock> activeBlocks = new HashSet<FlyingBlock>();

    public Tornado(World world) {
        super(world);
    }

    public Tornado(World world, double posX, double posY, double posZ) {
        super(world);
    }

    public Vector calculateNewVector(double posY, Location currentLocation) {

    }

    public void setDirection(double x, double y, double z) {
        double motX = x - this.locX;
        double motZ = z - this.locZ;
        double d0 = MathHelper.sqrt(motX * motX + motZ * motZ);
        this.motX = (float) (motX / d0 * this.speed);
        this.motZ = (float) (motZ / d0 * this.speed);
    }

    @Override
    public void l_() {
        super.l_();

        if (this.world.isStatic) {
            Storm.tornadoStorm(this.locX, this.locY, this.locZ);
        }

        if (this.age > pathTime) {
            this.dissipate();
        }

        if (this.dying) {
            if (this.strength <= 0.0F) {
                this.strength = 0.0F;
            }
            if (this.spawnY < 128.0) {
                this.locY = this.locY + (0.3000000000189895 - this.random.nextGaussian() * 2.0D);
                this.locX += (this.random.nextGaussian() * 2.0D - 0.5D) * 0.2D;
                this.locZ += (this.random.nextGaussian() * 2.0D - 0.5D) * 0.2D;
                this.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
            } else {
                this.dying = false;
                this.die();
            }
        } else if (this.spawning) {
            this.strength = this.strength + 0.5F;
            if (this.strength < 30.0F) {
                this.strength = 30.0F;
            } else if (this.strength > 100.0F) {
                this.strength = 100.0F;
            }

            if (spawnY >= this.maxY) {
                this.spawnY = this.locY - (0.3000000000189895 - this.random.nextGaussian() * 2.0D);
                this.locX += (this.random.nextGaussian() * 2.0D - 0.5D) * 0.2D;
                this.locZ += (this.random.nextGaussian() * 2.0D - 0.5D) * 0.2D;
                this.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
            } else {
                this.spawning = false;

            }
        }

        this.rotate();

        if (!this.world.isStatic) {
            for (int i = 0; i < this.locY + 72; i++) {
                int j = i / 4;

                if (this.pickupCounter >= this.pickupChance) {
                    this.pickupCounter = 0;
                    int x = (int) this.locX + this.random.nextInt(this.pickupRadius + j) - j / 2;
                    int y = (int) this.locY + i;
                    int z = (int) this.locZ + this.random.nextInt(this.pickupRadius + j) - j / 2;

                    Block b = world.getWorld().getBlockAt(x, y, z);
                    if (canPickup(b)) {
                        Location l = new Location(this.world.getWorld(), x, y, z);
                        FallingBlock fb = world.getWorld().spawnFallingBlock(l, b.getType(), b.getData());
                        FlyingBlock flyBlock = new FlyingBlock(fb, this.maxY);
                        this.activeBlocks.add(flyBlock);
                        world.getWorld().playSound(l, Sound.ANVIL_BREAK, 10F, 1F);
                        b.setType(Material.AIR);
                    }
                }
            }
        }

        for (int y = 0; y < this.maxY; y++) {
            for (int j = 0; j < (y == 0 ? 2 : y / 3); j++) {
                double x = this.locX + this.random.nextDouble() * 2.0D - 2.0D;
                double z = this.locZ + this.random.nextDouble() * 2.0D - 2.0D;
                this.sendTornadoSmoke(x, y, z);
            }
        }
    }

    public boolean sendTornadoSmoke(double x, double y, double z) {
        try {
            Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersionString() + ".Packet63WorldParticles").getConstructor().newInstance();
            ReflectionUtil.setValue(packet, "a", "largesmoke");
            ReflectionUtil.setValue(packet, "b", (float) x);
            ReflectionUtil.setValue(packet, "c", (float) y);
            ReflectionUtil.setValue(packet, "d", (float) z);
            ReflectionUtil.setValue(packet, "e", 0F);
            ReflectionUtil.setValue(packet, "f", 0F);
            ReflectionUtil.setValue(packet, "g", 0F);
            ReflectionUtil.setValue(packet, "h", 0F);
            ReflectionUtil.setValue(packet, "i", 20);
            ReflectionUtil.sendPacket(new Location(this.world.getWorld(), x, y, z), packet);
            return true;
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Packet Object (Packet63WorldParticles).", e, true);
        }
        return false;
    }

    @Override
    protected void a() {}

    @Override
    protected void a(NBTTagCompound nbtTagCompound) {
        this.age = nbtTagCompound.getShort("age");
    }

    @Override
    protected void b(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setShort("age", (short) ((byte) this.age));
    }
}