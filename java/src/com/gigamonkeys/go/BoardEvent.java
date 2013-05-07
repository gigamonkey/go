package com.gigamonkeys.go;

import java.util.EventObject;

/*
 * Implement the main rules of Go as far as what happens when stones
 * are put on the board.
 */
public class BoardEvent extends EventObject {

    public final int position;
    public final Color color;
    public final boolean added;

    /**
     * New BoardEvent representing a stone being added or removed from
     * the board. The color represents the color of the stone in
     * either case.
     */
    public BoardEvent(Object source, int position, Color color, boolean added) {
        super(source);
        this.position = position;
        this.color    = color;
        this.added    = added;
    }

}
