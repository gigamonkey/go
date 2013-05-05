package com.gigamonkeys.go.gui;

import com.gigamonkeys.go.Board;
import com.gigamonkeys.go.BoardEvent;
import com.gigamonkeys.go.BoardListener;
import com.gigamonkeys.go.Color;
import com.gigamonkeys.go.Game;
import com.gigamonkeys.go.IllegalMoveException;
import com.gigamonkeys.go.Player;
import com.gigamonkeys.go.RandomPlayer;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.*;

public class GUI extends JFrame {

    public static void main(String args[]) {

        final int size = args.length > 0 ? Integer.parseInt(args[0]) : 19;

        final BoardPanel boardGUI = new BoardPanel(size);

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Go Critters");
                    frame.setBounds(100, 100, 600, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(boardGUI);
                    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                    frame.setVisible(true);
                }
            });

        Board b  = new Board(size);
        Random r = new Random();

        b.addBoardListener(boardGUI);
        b.addBoardListener(new BoardListener() {
                public void stoneAdded(BoardEvent be) {
                    // Let the main thread take a break so the Swing
                    // threads can do their thing and redraw.
                    try { Thread.sleep(0, 1); } catch (InterruptedException ie) {}
                }
                public void stoneRemoved(BoardEvent be) {}

            });

        new Game(new RandomPlayer(), new RandomPlayer(), b).run();
        System.out.println("Done.");
    }
}
