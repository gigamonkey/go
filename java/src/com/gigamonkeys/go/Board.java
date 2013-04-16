package com.gigamonkeys.go;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/*
 * Implement the main rules of Go as far as what happens when stones
 * are put on the board.
 */ 
public class Board {

    public final int size;
    public final int positions;
    private final BitSet blackStones;
    private final BitSet whiteStones;
    private final List<BoardListener> listeners = new ArrayList<BoardListener>(5);

    public Board (int size) {
        this.size        = size;
        this.positions   = size * size;
        this.blackStones = new BitSet(positions);
        this.whiteStones = new BitSet(positions);
    }

    public void addBoardListener(BoardListener listener) {
        listeners.add(listener);
    }

    /**
     * Get the list of neighboring points. Either two, three, or four
     * depending whether position is a corner, edge, or middle point.
     */
    public List<Integer> neighbors (int position) {

        int x   = position % size;
        int y   = position / size;
        int max = size - 1;

        List<Integer> neighbors = new ArrayList<Integer>(4);
        if (y > 0)   neighbors.add(position - size); // North
        if (y < max) neighbors.add(position + size); // South
        if (x > 0)   neighbors.add(position - 1);    // East
        if (x < max) neighbors.add(position + 1);    // West
        return neighbors;
    }

    public void placeStone (Color color, int position) {

        if (!isEmpty(position)) {
            throw new IllegalMoveException("Position occpupied.", position);
        }

        BitSet friends = friends(color);
        BitSet enemies = enemies(color);

        // Place the stone.
        friends.set(position);

        // Remove any neighboring enemy groups we killed .
        BitSet killed = killed(position, friends, enemies); 
        enemies.andNot(killed);
        
        // Check for Ko. Not actually implemented yet.
        if (isKo()) {
            enemies.or(killed);
            friends.clear(position);
            throw new IllegalMoveException("Ko", position);
        }

        // Check for suicide.
        Stones us = connectedTo(position);
        if (!us.alive) {
            // Don't have to worry about putting back killed because
            // if there were any killed then we won't end up in this
            // branch.
            friends.clear(position);
            throw new IllegalMoveException("Suicide.", position);
        }

        fireBoardEvents(position, color, killed);
    }

    private void fireBoardEvents(int position, Color color, BitSet killed) {

        BoardEvent added = new BoardEvent(this, position, color);
        List<BoardEvent> removed = new ArrayList<BoardEvent>();

        for (int i = killed.nextSetBit(0); i != -1; i = killed.nextSetBit(i + 1)) {
            removed.add(new BoardEvent(this, i, Color.EMPTY));
        }

        for (BoardListener listener: listeners) {
            listener.stoneAdded(added);
            for (BoardEvent e: removed) {
                listener.stoneRemoved(e);
            }
        }

    }

    /*
     * Get (possibly empty) set of stones killed by playing at position.
     */
    private BitSet killed(int position, BitSet friends, BitSet enemies) {
        
        BitSet live = new BitSet(positions);
        BitSet dead = new BitSet(positions);

        for (int n: neighbors(position)) {
            if (enemies.get(n) && !(live.get(n) || dead.get(n))) {
                Stones s = connectedTo(n);
                (s.alive ? live : dead).or(s.stones);
            }
        }
        return dead;
    }

    /**
     * Walk the group starting from the stone at the given position,
     * returning either the set of stones needed to determine that the
     * group is alive or the complete group if it is dead.
     */
    private Stones connectedTo (int position) {
        
        BitSet stones            = new BitSet(positions);
        Queue<Integer> toProcess = new LinkedList<Integer>();

        // Get the ball rolling ...
        stones.set(position);
        toProcess.add(position);

        BitSet friends = friends(color(position));

        while (!toProcess.isEmpty()) {
            int p = toProcess.remove();
            for (int n: neighbors(p)) {
                if (isEmpty(n)) {
                    return live(stones);
                } else if (friends.get(n) && !stones.get(n)) {
                    stones.set(n);
                    toProcess.add(n);
                }
            }
        }
        return dead(stones);
    }

    private boolean isKo () {
        return false; // TODO: implement
    }

    private Color color (int position) {
        return 
            blackStones.get(position) ? Color.BLACK :
            whiteStones.get(position) ? Color.WHITE :
            Color.EMPTY;
    }

    private BitSet friends (Color color) {
        switch (color) {
        case BLACK: return blackStones;
        case WHITE: return whiteStones;
        default: throw new IllegalArgumentException("Empty has no friends.");
        }
    }

    private BitSet enemies (Color color) {
        switch (color) {
        case BLACK: return whiteStones;
        case WHITE: return blackStones;
        default: throw new IllegalArgumentException("Empty has no enemies.");
        }
    }


    public BitSet empties () {
        BitSet empties = (BitSet)blackStones.clone();
        empties.or(whiteStones);
        empties.flip(0, positions);
        return empties;
    }
    
    public boolean isEmpty(int position) {
        return !(blackStones.get(position) || whiteStones.get(position));
    }


    private static class Stones {
        public final BitSet stones;
        public final boolean alive;
        Stones(BitSet stones, boolean alive) {
            this.stones = stones;
            this.alive = alive;
        }
    }

    private Stones live(BitSet stones) { return new Stones(stones, true); }

    private Stones dead(BitSet stones) { return new Stones(stones, false); }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int p = 0; p < positions; p++) {
            if ((p % size) == 0) buf.append("\n");
            switch (color(p)) {
            case BLACK: buf.append("@ "); break;
            case WHITE: buf.append("O "); break;
            case EMPTY: buf.append(". "); break;
            }
        }
        return buf.toString();
    }

}
