package com.gigamonkeys.go.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.*;

/*
 * A heatmap of the locations of critters by percentage of the total
 * population. Arrange things so that if critters were completely
 * uniformly spread (i.e. every point had 1/Nth of the critters where
 * N is the number of points on the board), the heatmap would be
 * basically white, i.e. 6500 K in the color spectrum in Blackbody.
 *
 * So take the number of points (size^2) 
 */    
public class Heatmap extends JPanel {

    private final int size;
    private final int population;

    public Heatmap(int size, int population) {
        this.size            = size;
        this.population      = population;
    }
    
    
}

