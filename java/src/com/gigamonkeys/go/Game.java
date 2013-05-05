package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Controller that runs a single game.
 */
public class Game {

    public final static int PASS = -1;

    public final Player black;
    public final Player white;
    public final Board board;

    public Game(Player black, Player white, Board board) {
        this.black = black;
        this.white = white;
        this.board = board;
        black.init(board, Color.BLACK);
        white.init(board, Color.WHITE);
    }

    public void run() {

        Player toPlay = black;
        int passes = 0;

        while (passes < 2) {
            passes = toPlay.move() ? 0 : passes + 1;
            toPlay = toPlay == black ? white : black;
        }

        Score score = board.score();

        if (score.winner == Color.BLACK) {
            black.won(score);
            white.lost(score);
        } else {
            white.won(score);
            black.lost(score);
        }
    }

}
