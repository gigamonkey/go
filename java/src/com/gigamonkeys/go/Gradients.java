package com.gigamonkeys.go;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.log;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Maintain a set of gradients overlayed on the board that are
 * available to critters. For example, each time we put down a stone,
 * we add a certain amount to every point on the board, with the
 * amount falling off as we get farther away from the stone. Thus
 * critters could compare the gradient scores of neighboring points
 * and head toward (or away from) concentrations of their side's
 * stones, the other side's stones, or empty points.
 */
public class Gradients {

    public final int size;
    public final int[] values;
    private final int coefficient;

    public Gradients(int size) {
        this.size   = size;
        this.values = new int[size * size];

        // We want a coefficent such that the difference in the score
        // between the farthest away point and the second farthest
        // away point will be one, regardless of the size of the
        // board. Basically, given the score function below, let m be
        // the the maximum distance (a function of the board
        // size). Then the score of the farthest away point is:
        //
        //   x / (m^2 + 1)
        //
        // and the score of the next farthest away point is:
        //
        //   x / ((m - 1)^2 + 1)
        //
        // So set:
        //
        //   (x / (m^2 + 1)) + 1 = x / ((m - 1)^2 + 1)
        //
        // And solve for x. Use that as the coefficient.

        int m = (size - 1) * 2;
        this.coefficient = (m * (m * (m * (m - 2) + 3) - 2) + 2) / (2 * m - 1);
    }

    public void set(int p) {
        for (int i = 0; i < values.length; i++) {
            values[i] += score(p, i);
        }
    }

    public void clear(int p) {
        for (int i = 0; i < values.length; i++) {
            values[i] -= score(p, i);
        }
    }

    public int[] toArray() {
        // Should be treated as read only.
        return values;
    }

    private int score(int p, int i) {
        int d = abs((p % size) - (i % size)) + abs((p / size) - (i / size));
        return round(coefficient / (d * d + 1));
    }
}
