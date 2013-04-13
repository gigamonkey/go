package com.gigamonkeys.go;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Container;
import javax.swing.*;

public class BoardPanel extends JPanel {

    private final static int   BOARD_SIZE  = 19;
    private final static Color BOARD_COLOR = new Color(211, 177, 101);
    
    public BoardPanel() {
        //setBorder(BorderFactory.createLineBorder(Color.black));
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        //System.out.println(g.getClass().getName());
        //g.drawString("This is my custom Panel!",10,20);
        // drawLine: x1, y1, x2, y2

        Dimension d = getSize();

        // Given the size of the panel, we want to make square board
        // such that after dividing it into squares, we can position
        // it within the panel such that there is enough space around
        // it for stones on the edge. A stone is basically the same
        // size as the square. That means that there needs to be half
        // a square of space around the board.

        int squareSize = Math.min(d.height, d.width) / (BOARD_SIZE + 1);
        int boardSize  = squareSize * BOARD_SIZE;

        int left   = (d.width - boardSize) / 2;
        int right  = left + boardSize;
        int top    = (d.height - boardSize) / 2;
        int bottom = top + boardSize;

        g.setColor(BOARD_COLOR);
        g.fillRect(left, top, right - left, bottom - top);
        g.setColor(Color.black);

        for (int i = 0; i < BOARD_SIZE + 1; i++) {
            int step = squareSize * i;
            g.drawLine(left, top + step, right,  top + step);
            g.drawLine(left + step, top, left + step, bottom);
        }

        drawBlackStone(g, left, top, squareSize, 0, 0);
        drawWhiteStone(g, left, top, squareSize, 1, 0);

    }

    private void drawBlackStone(Graphics g, int left, int top, int squareSize, int x, int y) {
        g.setColor(Color.black);
        int gx = (left - squareSize/2) + squareSize * x;
        int gy = (top - squareSize/2) + squareSize * y;
        g.fillOval(gx, gy, squareSize, squareSize);
    }

    private void drawWhiteStone(Graphics g, int left, int top, int squareSize, int x, int y) {
        squareSize--; // adjust for optical illusion
        int gx = (left - squareSize/2) + squareSize * x;
        int gy = (top - squareSize/2) + squareSize * y;
        g.setColor(Color.white);
        g.fillOval(gx, gy, squareSize, squareSize);
        g.setColor(Color.black);
        g.drawOval(gx, gy, squareSize, squareSize);
    }
    
}

