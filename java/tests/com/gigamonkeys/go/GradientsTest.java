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

        g.set(0);
        show(g);

        g.clear(0);
        show(g);

        for (int i = 0; i < 19 * 19; i++) { g.set(i); }
        show(g);

        for (int i = 0; i < 19 * 19; i++) { g.clear(i); }
        show(g);
    }

    private static void show(Gradients g) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                System.out.print(String.format("%8d", g.values[i * 19 + j]));
            }
            System.out.println();
        }
        System.out.println();
    }
}
