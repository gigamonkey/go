package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Simple struct to represent the result of a game.
 */
public class Score {

    public final Color winner;
    public final int black;
    public final int white;

    public Score(Color winner, int black, int white) {
        this.winner = winner;
        this.black = black;
        this.white = white;
    }

}
