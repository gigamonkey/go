package com.gigamonkeys.go;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Container;
import javax.swing.*;

public class GUI extends JFrame {

    public static void main(String args[]) {
        final int size = args.length > 0 ? Integer.parseInt(args[0]) : 19;

	SwingUtilities.invokeLater(new Runnable() {
		public void run() { 
		    JFrame frame = new JFrame("Go Critters");
		    frame.setBounds(100, 100, 600, 600);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.add(new BoardPanel(size));
		    frame.setVisible(true);
		}
	    });
    }
    
}
