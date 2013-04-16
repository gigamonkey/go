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
        
        int[] passes = {0, 0};

        for (int i = 0; i < 10 * b.positions; i++) {
            int who = i % 2;
            if (move(b, who == 0 ? Color.BLACK : Color.WHITE, r)) {
                passes[who] = 0;
                try { Thread.sleep(1); } catch (InterruptedException ie) {}

            } else {
                passes[who]++;
                if (passes[who] == 2) {
                    break;
                }
            }
        }
        System.out.println("Done.");
    }

    private static boolean move(Board b, Color color, Random r) {
        for (int i = 0; i < b.positions; i++) {
            int p = r.nextInt(b.positions);
            try {
                b.placeStone(color, p);
                return true;
            } catch (IllegalMoveException ime) {
                //System.out.println(ime);
            }
        }
        return false;
    }
}
