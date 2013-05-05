package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Interface needed by the GameController.
 */
public interface Player {

    /**
     * Set the board the player is playing on. Called once at the
     * beginning of a game.
     */
    public void init(Board board, Color color);

    /**
     * Make a move on the board and return true or return false to
     * indicate pass. This interface puts a fair bit of trust in the
     * player implementations; they have to actually make exactly one
     * move and then return true or make no moves and then return
     * false. The advantage, such as it is, is that players can try to
     * make moves on the board and then count on the board to throw an
     * IllegalMoveException if there's something wrong with the
     * move. However this means that a badly behaving Player could sit
     * in a loop trying the same move over and over again.
     */
    public boolean move();


    public void won(Score score);

    public void lost(Score score);

}
