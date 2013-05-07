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

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Go Critters");
                    BoardPanel panel = new BoardPanel(size);
                    frame.setBounds(100, 100, 600, 600);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(panel);
                    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                    frame.setVisible(true);
                    new GUIWorker(panel, size).execute();
                }
            });

        System.out.println("Done.");
    }
}
