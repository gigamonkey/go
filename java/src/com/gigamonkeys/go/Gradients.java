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
 * amount falling off as we get farther away from the stone.
 */
public class Gradients {

    public final int size;
    public final int[] values;

    public Gradients(int size) {
        this.size   = size;
        this.values = new int[size * size];
    }

    public void addPoint(int p) {
        for (int i = 0; i < values.length; i++) {
            values[i] += score(p, i);
        }
    }

    public void removePoint(int p) {
        for (int i = 0; i < values.length; i++) {
            values[i] -= score(p, i);
        }
    }

    private int score(int p, int i) {
        int m        = size - 1;
        int distance = abs((p % size) - (i % size)) + abs((p / size) - (i / size));
        int x        = (2 * m) - distance;

        return x == 0 ? 0 : (int)round(1000 * (log(x * x) / log(m)));
    }

}
