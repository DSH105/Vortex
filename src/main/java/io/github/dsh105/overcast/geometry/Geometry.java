package io.github.dsh105.overcast.geometry;

import java.util.Random;

/**
 * Project by DSH105
 */

public class Geometry {

    static Random r = new Random();

    public static float generateRandomFloat(float min, float max) {
        return min + (r.nextFloat() * ((1 + max) - min));
    }
}