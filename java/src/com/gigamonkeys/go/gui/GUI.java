package com.gigamonkeys.go.gui;

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
