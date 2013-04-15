package com.gigamonkeys.go.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.*;

public class BoardPanel extends JPanel {

    private final static int EMPTY = 0;
    private final static int BLACK = 1;
    private final static int WHITE = 2;

    private final static Color BOARD_COLOR = new Color(211, 177, 101);
    private final static Color CRITTER_COLOR = new Color(0, 0, 255, 10); // new Color(70, 127, 250, 10);
    
    private final int size;
    private final int[] stones;
    private final int[] critters;

    public BoardPanel(int size) {
        this.size     = size;
        this.stones   = new int[size * size];
        this.critters = new int[size * size];
        randomStones();
        randomCritters();
    }

    private void randomStones() {
        Random r = new Random();
        for (int i = 0; i < stones.length; i++) {
            this.stones[i] = r.nextInt(WHITE + 1);
        }
    }

    private void randomCritters() {
        Random r = new Random();
        for (int i = 0; i < stones.length; i++) {
            this.critters[i] = r.nextInt(10000);
        }
    }

    private void checker() {
        for (int i = 0; i < stones.length; i++) {
            this.stones[i] = (i % 2) + 1;
        }
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       


        // Given the size of the panel, we want to make square board
        // such that after dividing it into squares, we can position
        // it within the panel such that there is enough space around
        // it for stones on the edge. A stone is basically the same
        // size as the square. That means that there needs to be half
        // a square of space around the board.

        Dimension d    = getSize();
        int squareSize = Math.min(d.height, d.width) / size;
        int boardSize  = squareSize * (size - 1);
        int left       = (d.width - boardSize) / 2;
        int right      = left + boardSize;
        int top        = (d.height - boardSize) / 2;
        int bottom     = top + boardSize;

        g.setColor(BOARD_COLOR);
        g.fillRect(left, top, right - left, bottom - top);
        g.setColor(Color.black);

        for (int i = 0; i < size; i++) {
            int step = squareSize * i;
            g.drawLine(left, top + step, right,  top + step);
            g.drawLine(left + step, top, left + step, bottom);
        }
        drawStones(g, left, top, squareSize);
        drawCritters(g, left, top, squareSize);
    }

    private void drawStones(Graphics g, int left, int top, int squareSize) {
        for (int i = 0; i < stones.length; i++) {
            if (stones[i] == BLACK) {
                drawBlackStone(g, left, top, squareSize, i % size, i / size);
            } else if (stones[i] == WHITE) {
                drawWhiteStone(g, left, top, squareSize, i % size, i / size);
            }
        }
    }

    private void drawBlackStone(Graphics g, int left, int top, int squareSize, int x, int y) {
        g.setColor(Color.black);
        int gx = (left - squareSize/2) + squareSize * x;
        int gy = (top - squareSize/2) + squareSize * y;
        g.fillOval(gx, gy, squareSize, squareSize);
    }

    private void drawWhiteStone(Graphics g, int left, int top, int squareSize, int x, int y) {
        int gx = (left - squareSize/2) + squareSize * x;
        int gy = (top - squareSize/2) + squareSize * y;
        
        // adjust for optical illusion that makes white stones with
        // border look bigger.
        if (squareSize > 5) {
            gx++;
            gy++;
            squareSize -= 2;
        }

        g.setColor(Color.white);
        g.fillOval(gx, gy, squareSize, squareSize);
        if (squareSize > 5) {
            g.setColor(Color.black);
            g.drawOval(gx, gy, squareSize, squareSize);
        }
    }

    private void drawCritters(Graphics g, int left, int top, int squareSize) {
        for (int i = 0; i < critters.length; i++) {
            drawCrittersAt(g, left, top, squareSize, i % size, i / size, critters[i]);
        }
    }
    
    private void drawCrittersAt(Graphics g, int left, int top, int squareSize, int x, int y, int num) {
        // Pick a spot a random distance from the exact center.
        int gx = left + squareSize * x;
        int gy = top + squareSize * y;
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            drawOneCritter(g, r, gx, gy, squareSize);
        }
    }

    private void drawOneCritter(Graphics g, Random r, int gx, int gy, int squareSize) {
        double maxJitter = squareSize/6;
        int critterSize = 2;
        int xJitter = (int)(r.nextGaussian() * maxJitter);
        int yJitter = (int)(r.nextGaussian() * maxJitter);
        g.setColor(CRITTER_COLOR);
        g.fillOval(gx - critterSize/2 + xJitter, gy - critterSize/2 + yJitter, critterSize, critterSize);
    }

    
    
}

