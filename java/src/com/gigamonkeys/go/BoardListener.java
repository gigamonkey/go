package com.gigamonkeys.go;

import java.util.EventListener;

/*
 * Implement the main rules of Go as far as what happens when stones
 * are put on the board.
 */ 
public interface BoardListener extends EventListener {

    public void stoneAdded(BoardEvent be);

    public void stoneRemoved(BoardEvent be);

}
