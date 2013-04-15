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

    private final int size;
    private final BitSet blackStones;
    private final BitSet whiteStones;

    public Board (int size) {
        this.size = size;
        this.blackStones = new BitSet(size * size);
        this.whiteStones = new BitSet(size * size);
    }

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
        
        // Check for Ko.
        if (isKo()) {
            enemies.or(killed);
            friends.clear(position);
            throw new IllegalMoveException("Ko", position);
        }

        // Check for suicide.
        Stones us = connectedTo(position);
        if (!us.alive) {
            // Don't have to worry about putting back killed because
            // if there were any killed then this group can't be dead.
            friends.clear(position);
            throw new IllegalMoveException("Suicide.", position);
        }
    }

    /*
     * Get (possibly empty) set of stones killed by playing at position.
     */
    private BitSet killed(int position, BitSet friends, BitSet enemies) {
        
        BitSet live = new BitSet(size * size);
        BitSet dead = new BitSet(size * size);

        for (int n: neighbors(position)) {
            
            if (enemies.get(n) && !(live.get(n) || dead.get(n))) {
                Stones s = connectedTo(n);
                if (s.alive) {
                    live.or(s.stones);
                } else {
                    dead.or(s.stones);
                }
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
        
        BitSet stones            = new BitSet(size * size);
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
        empties.flip(0, size * size);
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
        for (int p = 0; p < size * size; p++) {
            if ((p % size) == 0) buf.append("\n");
            switch (color(p)) {
            case BLACK: buf.append("@ "); break;
            case WHITE: buf.append("O "); break;
            case EMPTY: buf.append(". "); break;
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        
        final int size = args.length > 0 ? Integer.parseInt(args[0]) : 19;

        Board b = new Board(size);

        Random r = new Random();

        for (int i = 0; i < size * size; i++) {
            int p = r.nextInt(size * size);
            try {
                b.placeStone(i % 2 == 0 ? Color.BLACK : Color.WHITE, p);
                System.out.println(b);
                System.out.println("Empty:");
                BitSet empties = b.empties();
                int e = 0;
                while ((e = empties.nextSetBit(e)) != -1) {
                    System.out.print(e + " ");
                    e++;
                }
                System.out.println();
            } catch (IllegalMoveException ime) {
                System.out.println(ime);
            }
        }


        /*
        for (int p = 0; p < size * size; p++) {
            System.out.print(p + ":");
            for (int n: b.neighbors(p)) {
                System.out.print(" " + n);
            }
            System.out.println();
        }
        */
    }



}
