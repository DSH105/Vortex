package com.dsh105.vortex.util;

import org.bukkit.Material;

public class BlockData {

    public Material blockType;
    public byte blockMeta;

    public BlockData(Material blockType, byte blockMeta) {
        this.blockType = blockType;
        this.blockMeta = blockMeta;
    }
}