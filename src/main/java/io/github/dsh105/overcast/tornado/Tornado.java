package io.github.dsh105.overcast.tornado;

import io.github.dsh105.overcast.geometry.Direction;
import io.github.dsh105.overcast.logger.Logger;
import io.github.dsh105.overcast.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.HashSet;

/**
 * Project by DSH105
 */

public class Tornado {

    public World world;
    public double locX;
    public double locY;
    public double locZ;
    public Direction direction;
    public float speed;
    public float strength;
    public int age;
    public boolean disspating;
    public boolean forming;
    public int formTicks;
    public int dissipateTicks;
    public int maxY = 80;
    public int dirChange;
    public HashSet<BlockLayer> layers = new HashSet<BlockLayer>();
    public HashSet<FlyingBlock> grabbedBlocks = new HashSet<FlyingBlock>();

    public Vector calculateNewVector(double posY, Location currentLocation) {

    }

    public void setPosition(double x, double y, double z) {
        this.locX = x;
        this.locY = y;
        this.locZ = z;
    }

    public BlockLayer findLayer(FlyingBlock fb) {
        for (BlockLayer bl : layers) {
            if (bl.blocks.contains(fb)) {
                return bl;
            }
        }
        return null;
    }

    public BlockLayer findLayer(int y) {
        for (BlockLayer bl : layers) {
            if (bl.yHeight == y) {
                return bl;
            }
        }
        return null;
    }

    public BlockLayer findClosestLayer(int y) {
        for (BlockLayer bl : layers) {
            if (bl.yHeight + 2 >= y && bl.yHeight - 2 <= y) {
                return bl;
            }
        }
        return null;
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
            ReflectionUtil.sendPacket(new Location(this.world, x, y, z), packet);
            return true;
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to create Packet Object (Packet63WorldParticles).", e, true);
        }
        return false;
    }
}