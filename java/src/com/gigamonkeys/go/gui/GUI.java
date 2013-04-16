package com.gigamonkeys.go.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.*;

import com.gigamonkeys.go.Board;
import com.gigamonkeys.go.Color;
import com.gigamonkeys.go.IllegalMoveException;


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
		    frame.setVisible(true);
		}
	    });
                            
        Board b  = new Board(size);
        Random r = new Random();

        b.addBoardListener(boardGUI);
        
        for (int i = 0; i < 2 * b.positions; i++) {
            while (true) {
                int p = r.nextInt(b.positions);
                try {
                    b.placeStone(i % 2 == 0 ? Color.BLACK : Color.WHITE, p);
                    System.out.println(b);
                    System.out.println();
                    break;
                } catch (IllegalMoveException ime) {
                    System.out.println(ime);
                }
            }
        }
        System.out.println("Done.");
    }
}
