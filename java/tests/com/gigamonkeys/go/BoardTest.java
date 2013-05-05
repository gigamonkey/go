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
public class BoardTest {

    public static void main(String[] args) {
        
        final int size = args.length > 0 ? Integer.parseInt(args[0]) : 19;

        Board b  = new Board(size);
        Random r = new Random();

        for (int i = 0; i < 2 * b.positions; i++) {
            while (true) {
                int p = r.nextInt(b.positions);
                try {
                    b.placeStone(i % 2 == 0 ? Color.BLACK : Color.WHITE, p);
                    System.out.println(b);
                    System.out.println();
                    break;
                } catch (IllegalMoveException ime) {
                    //System.out.println(ime);
                }
            }
        }
    }

    private static void printNeighbors (int size) {
        
        Board b = new Board(size);

        for (int p = 0; p < b.positions; p++) {
            System.out.print(p + ":");
            for (int n: b.neighbors(p)) {
                System.out.print(" " + n);
            }
            System.out.println();
        }
    }



}
