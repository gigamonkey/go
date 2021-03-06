package com.gigamonkeys.go.gui;

import com.gigamonkeys.go.*;

import javax.swing.*;
import java.util.List;

/*
 * Copyright (c) 2013 Peter Seibel
 */

/**
 * Run a random game in a SwingWorker thread.
 */
public class GUIWorker extends SwingWorker<Boolean, BoardEvent> implements BoardListener {

    private final static boolean THROTTLE = true;

    private final BoardPanel boardPanel;
    private final int size;

    public GUIWorker(BoardPanel boardPanel, int size) {
        this.boardPanel = boardPanel;
        this.size       = size;
    }

    public void stoneAdded(BoardEvent be) {
        publish(be);
        // Wait a bit here to keep the animation from running a
        // gazillion moves at a time. However, the sleep seems to last
        // a lot longer than a billionth of a second.
        if (THROTTLE) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ie) {
            }
        }
    }
    public void stoneRemoved(BoardEvent be) { publish(be); }

    @Override
    protected Boolean doInBackground() throws Exception {
        Board b = new Board(size);
        b.addBoardListener(this);
        new Game(new RandomPlayer(), new RandomPlayer(), b).run();
        return true;
    }

    @Override
    protected void process(List<BoardEvent> events) {
        for (BoardEvent be: events) {
            if (be.added) {
                boardPanel.stoneAdded(be);
            } else {
                boardPanel.stoneRemoved(be);
            }
        }
    }
}
