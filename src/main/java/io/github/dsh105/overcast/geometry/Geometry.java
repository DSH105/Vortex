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

    public static boolean nearby(double d0, double d1, Operator op) {
        if (op == Operator.EQUAL) {

        } else if (op == Operator.GREATER_OR_EQUAL) {

        } else if (op == Operator.LESS_OR_EQUAL) {

        }
        return false;
    }
}