package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Unit test for com.gigamonkeys.go.Gradients.
 */
public class GradientsTest {

    public static void main(String[] argv) {
        Gradients g = new Gradients(19);
        for (int i = 0; i < 19 * 19; i++) { g.addPoint(i); }
        //g.addPoint(2 * 19 + 7);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(String.format("%8d", g.values[i * 19 + j]));
            }
            System.out.println();
        }

        for (int i = 0; i < 19 * 19; i++) { g.removePoint(i); }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(String.format("%8d", g.values[i * 19 + j]));
            }
            System.out.println();
        }
    }
}
