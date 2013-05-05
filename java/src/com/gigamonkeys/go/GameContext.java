package com.gigamonkeys.go;

import java.util.BitSet;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Represenation of the state of the board that is passed into the
 * VM.run method. At the beginning of the game we make a GameContext
 * for each side. When running the critters to compute a move, the
 * GameContext is passed to the VM's execute() method which pulls out
 * the fields for fast access. Once the move is made, we
 */
public class GameContext implements BoardListener {

    public final int size;
    private final Color color;
    private final BitSet mine;
    private final BitSet theirs;
    private final BitSet empty;
    private final Gradients mineGradient;
    private final Gradients theirsGradient;
    private final Gradients emptyGradient;
    private final Gradients cornerGradient;

    public GameContext(Board board, Color color) {
        this.size           = board.size;
        this.color          = color;
        this.mine           = new BitSet(size);
        this.theirs         = new BitSet(size);
        this.empty          = makeEmptyBits(size);

        this.mineGradient   = makeGradient(mine);
        this.theirsGradient = makeGradient(theirs);
        this.emptyGradient  = makeGradient(empty);
        this.cornerGradient = makeCornerGradient();

        board.addBoardListener(this);
    }

    public BitSet getMine() { return mine; }
    public BitSet getTheirs() { return theirs; }
    public BitSet getEmpty() { return empty; }
    public int[] getMineGradient() { return mineGradient.toArray(); }
    public int[] getTheirsGradient() { return theirsGradient.toArray(); }
    public int[] getEmptyGradinet() { return emptyGradient.toArray(); }
    public int[] getCornerGradient() { return cornerGradient.toArray(); }

    public void stoneAdded(BoardEvent be) {
        int p = be.position;
        if (be.color == color) {
            this.mine.set(p);
            this.mineGradient.set(p);
        } else {
            this.theirs.set(p);
            this.theirsGradient.set(p);
        }
        this.empty.clear(p);
        this.emptyGradient.clear(p);
    }

    public void stoneRemoved(BoardEvent be) {
        int p = be.position;
        if (be.color == color) {
            this.mine.clear(p);
            this.mineGradient.clear(p);
        } else {
            this.theirs.clear(p);
            this.theirsGradient.clear(p);
        }
        this.empty.set(p);
        this.emptyGradient.set(p);
    }

    private BitSet makeEmptyBits(int size) {
        BitSet bits = new BitSet(size);
        bits.flip(0, size);
        return bits;
    }

    private Gradients makeGradient(BitSet bits) {
        Gradients g = new Gradients(size);
        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
            g.set(i);
        }
        return g;
    }

    private Gradients makeCornerGradient() {
        Gradients g = new Gradients(size);
        g.set(0);
        g.set(size - 1);
        g.set(size * (size - 1));
        g.set((size * size) - 1);
        return g;
    }
}
