package io.github.dsh105.overcast.tornado;

import java.util.HashSet;

/**
 * Project by DSH105
 */

public class BlockLayer {

    public Tornado tornado;
    public int yHeight;
    public int distanceFromCentre;
    public int blockCount;
    public HashSet<FlyingBlock> blocks = new HashSet<FlyingBlock>();

    public BlockLayer(Tornado tornado, int yHeight) {
        this.tornado = tornado;
        this.yHeight = yHeight;
        this.distanceFromCentre = (int) Math.ceil((double) this.yHeight / 4);
    }

    public void addBlock(FlyingBlock fb) {
        this.blocks.add(fb);
    }

    public float getOrbitWidth() {
        return (this.yHeight / 40) * 5;
    }
}