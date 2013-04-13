package com.gigamonkeys.go;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Container;
import javax.swing.*;

public class GUI extends JFrame {

    public static void main(String args[]) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() { 
		    JFrame frame = new JFrame("Go Critters");
		    frame.setBounds(300, 300, 300, 300);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.add(new BoardPanel());
		    frame.setVisible(true);
		}
	    });
    }
    
}
