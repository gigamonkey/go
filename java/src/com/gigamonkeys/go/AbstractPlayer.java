package com.gigamonkeys.go;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Simple base class for Player implementations.
 */
public abstract class AbstractPlayer implements Player {

    private Board board;
    private Color color;

    public void init(Board board, Color color) {
        this.board = board;
        this.color = color;
    }

    public abstract boolean move();

    public void won(Score score) {}

    public void lost(Score score) {}

    public Color getColor() { return color; }

    public Board getBoard() { return board; }



}
