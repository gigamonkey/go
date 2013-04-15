package com.gigamonkeys.go;

/*
 * Implement the main rules of Go as far as what happens when stones
 * are put on the board.
 */ 
public class IllegalMoveException extends RuntimeException {

    private final int position;

    public IllegalMoveException(String reason, int position) {
        super(reason);
        this.position = position;
    }
    

}
