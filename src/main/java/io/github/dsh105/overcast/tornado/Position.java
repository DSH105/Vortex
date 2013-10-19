package io.github.dsh105.overcast.tornado;

/**
 * Project by DSH105
 */

public class Position {

    public AxisPoint[] pos;

    public Position(AxisPoint ax1, AxisPoint ax2, AxisPoint ax3, AxisPoint ax4) {
        this.pos = new AxisPoint[] {ax1, ax2, ax3, ax4};
    }
}