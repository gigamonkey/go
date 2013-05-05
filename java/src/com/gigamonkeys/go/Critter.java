package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * One critter. A player is made up of many thousands of these. At the
 * start of the game the critter is positioned randomly on the board
 * and facing in a random direction. The VM keeps the position and
 * direction in 'registers' in the execute methed but resets the
 * fields on the critter at the end of execution.
 */
public class Critter {

    private final byte[] genes;
    private final Op code;

    private int direction = 0;
    private int position = 0;


    public Critter(byte[] genes) {
        this.genes = genes;
        this.code  = VM.compile(genes).get(0);
    }

    public void position(int position, int direction) {
        this.position = position;
        this.direction = direction;
    }

    public Op getCode() { return code; }

    public int getDirection() { return direction; }

    public int getPosition() { return position; }


}
