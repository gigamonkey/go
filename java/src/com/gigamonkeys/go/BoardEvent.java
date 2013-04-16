package com.gigamonkeys.go;

import java.util.EventObject;

/*
 * Implement the main rules of Go as far as what happens when stones
 * are put on the board.
 */ 
public class BoardEvent extends EventObject {

    public final int position;
    public final Color color;

    public BoardEvent(Object source, int position, Color color) {
        super(source);
        this.position = position;
        this.color    = color;
    }

}
