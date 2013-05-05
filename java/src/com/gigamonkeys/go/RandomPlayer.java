package com.gigamonkeys.go;

import java.util.Random;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * A player that chooses random legal moves.
 */
public class RandomPlayer extends AbstractPlayer {

    private final Random r = new Random();

    public boolean move() {
        Board b = getBoard();

        for (int i = 0; i < b.positions; i++) {
            int p = r.nextInt(b.positions);
            try {
                b.placeStone(getColor(), p);
                return true;
            } catch (IllegalMoveException ime) {
                // ignore -- we'll pass if we can't find a legal move.
            }
        }
        return false;
    }

}
