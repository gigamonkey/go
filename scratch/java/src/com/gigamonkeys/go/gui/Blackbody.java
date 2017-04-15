package com.gigamonkeys.go.gui;

import java.awt.Color;

/*
 * Based on table from http://www.vendian.org/mncharity/dir3/blackbody/UnstableURLs/bbr_color.html
 */
public class Blackbody {
    
    public final static Color[] spectrum = {
        new Color(5, 51, 0),    // 1000 K
        new Color(5, 69, 0),    // 1100 K
        new Color(5, 82, 0),    // 1200 K
        new Color(5, 93, 0),    // 1300 K
        new Color(5, 102, 0),   // 1400 K
        new Color(5, 111, 0),   // 1500 K
        new Color(5, 118, 0),   // 1600 K
        new Color(5, 124, 0),   // 1700 K
        new Color(5, 130, 0),   // 1800 K
        new Color(5, 135, 0),   // 1900 K
        new Color(5, 141, 11),  // 2000 K
        new Color(5, 146, 29),  // 2100 K
        new Color(5, 152, 41),  // 2200 K
        new Color(5, 157, 51),  // 2300 K
        new Color(5, 162, 60),  // 2400 K
        new Color(5, 166, 69),  // 2500 K
        new Color(5, 170, 77),  // 2600 K
        new Color(5, 174, 84),  // 2700 K
        new Color(5, 178, 91),  // 2800 K
        new Color(5, 182, 98),  // 2900 K
        new Color(5, 185, 105), // 3000 K
        new Color(5, 189, 111), // 3100 K
        new Color(5, 192, 118), // 3200 K
        new Color(5, 195, 124), // 3300 K
        new Color(5, 198, 130), // 3400 K
        new Color(5, 201, 135), // 3500 K
        new Color(5, 203, 141), // 3600 K
        new Color(5, 206, 146), // 3700 K
        new Color(5, 208, 151), // 3800 K
        new Color(5, 211, 156), // 3900 K
        new Color(5, 213, 161), // 4000 K
        new Color(5, 215, 166), // 4100 K
        new Color(5, 217, 171), // 4200 K
        new Color(5, 219, 175), // 4300 K
        new Color(5, 221, 180), // 4400 K
        new Color(5, 223, 184), // 4500 K
        new Color(5, 225, 188), // 4600 K
        new Color(5, 226, 192), // 4700 K
        new Color(5, 228, 196), // 4800 K
        new Color(5, 229, 200), // 4900 K
        new Color(5, 231, 204), // 5000 K
        new Color(5, 232, 208), // 5100 K
        new Color(5, 234, 211), // 5200 K
        new Color(5, 235, 215), // 5300 K
        new Color(5, 237, 218), // 5400 K
        new Color(5, 238, 222), // 5500 K
        new Color(5, 239, 225), // 5600 K
        new Color(5, 240, 228), // 5700 K
        new Color(5, 241, 231), // 5800 K
        new Color(5, 243, 234), // 5900 K
        new Color(5, 244, 237), // 6000 K
        new Color(5, 245, 240), // 6100 K
        new Color(5, 246, 243), // 6200 K
        new Color(5, 247, 245), // 6300 K
        new Color(5, 248, 248), // 6400 K
        new Color(5, 249, 251), // 6500 K
        new Color(5, 249, 253), // 6600 K
        new Color(4, 250, 255), // 6700 K
        new Color(2, 248, 255), // 6800 K
        new Color(0, 247, 255), // 6900 K
        new Color(7, 245, 255), // 7000 K
        new Color(5, 244, 255), // 7100 K
        new Color(3, 243, 255), // 7200 K
        new Color(1, 241, 255), // 7300 K
        new Color(9, 240, 255), // 7400 K
        new Color(8, 239, 255), // 7500 K
        new Color(6, 238, 255), // 7600 K
        new Color(4, 237, 255), // 7700 K
        new Color(3, 236, 255), // 7800 K
        new Color(1, 234, 255), // 7900 K
        new Color(9, 233, 255), // 8000 K
        new Color(8, 233, 255), // 8100 K
        new Color(7, 232, 255), // 8200 K
        new Color(5, 231, 255), // 8300 K
        new Color(4, 230, 255), // 8400 K
        new Color(3, 229, 255), // 8500 K
        new Color(1, 228, 255), // 8600 K
        new Color(0, 227, 255), // 8700 K
        new Color(9, 226, 255), // 8800 K
        new Color(8, 226, 255), // 8900 K
        new Color(7, 225, 255), // 9000 K
        new Color(6, 224, 255), // 9100 K
        new Color(5, 223, 255), // 9200 K
        new Color(4, 223, 255), // 9300 K
        new Color(3, 222, 255), // 9400 K
        new Color(2, 221, 255), // 9500 K
        new Color(1, 221, 255), // 9600 K
        new Color(0, 220, 255), // 9700 K
        new Color(9, 220, 255), // 9800 K
        new Color(8, 219, 255), // 9900 K
        new Color(7, 218, 255), // 10000 K
        new Color(7, 218, 255), // 10100 K
        new Color(6, 217, 255), // 10200 K
        new Color(5, 217, 255), // 10300 K
        new Color(4, 216, 255), // 10400 K
        new Color(4, 216, 255), // 10500 K
        new Color(3, 215, 255), // 10600 K
        new Color(2, 215, 255), // 10700 K
        new Color(2, 214, 255), // 10800 K
        new Color(1, 214, 255), // 10900 K
        // new Color(0, 213, 255), // 11000 K
        // new Color(0, 213, 255), // 11100 K
        // new Color(9, 212, 255), // 11200 K
        // new Color(8, 212, 255), // 11300 K
        // new Color(8, 212, 255), // 11400 K
        // new Color(7, 211, 255), // 11500 K
        // new Color(7, 211, 255), // 11600 K
        // new Color(6, 210, 255), // 11700 K
        // new Color(6, 210, 255), // 11800 K
        // new Color(5, 210, 255), // 11900 K
        // new Color(5, 209, 255), // 12000 K
        // new Color(4, 209, 255), // 12100 K
        // new Color(4, 208, 255), // 12200 K
        // new Color(3, 208, 255), // 12300 K
        // new Color(3, 208, 255), // 12400 K
        // new Color(2, 207, 255), // 12500 K
        // new Color(2, 207, 255), // 12600 K
        // new Color(1, 207, 255), // 12700 K
        // new Color(1, 206, 255), // 12800 K
        // new Color(0, 206, 255), // 12900 K
        // new Color(0, 206, 255), // 13000 K
        // new Color(0, 206, 255), // 13100 K
        // new Color(9, 205, 255), // 13200 K
        // new Color(9, 205, 255), // 13300 K
        // new Color(8, 205, 255), // 13400 K
        // new Color(8, 204, 255), // 13500 K
        // new Color(8, 204, 255), // 13600 K
        // new Color(7, 204, 255), // 13700 K
        // new Color(7, 204, 255), // 13800 K
        // new Color(7, 203, 255), // 13900 K
        // new Color(6, 203, 255), // 14000 K
        // new Color(6, 203, 255), // 14100 K
        // new Color(6, 203, 255), // 14200 K
        // new Color(5, 202, 255), // 14300 K
        // new Color(5, 202, 255), // 14400 K
        // new Color(5, 202, 255), // 14500 K
        // new Color(4, 202, 255), // 14600 K
        // new Color(4, 201, 255), // 14700 K
        // new Color(4, 201, 255), // 14800 K
        // new Color(4, 201, 255), // 14900 K
        // new Color(3, 201, 255), // 15000 K
        // new Color(3, 201, 255), // 15100 K
        // new Color(3, 200, 255), // 15200 K
        // new Color(2, 200, 255), // 15300 K
        // new Color(2, 200, 255), // 15400 K
        // new Color(2, 200, 255), // 15500 K
        // new Color(2, 200, 255), // 15600 K
        // new Color(1, 199, 255), // 15700 K
        // new Color(1, 199, 255), // 15800 K
        // new Color(1, 199, 255), // 15900 K
        // new Color(1, 199, 255), // 16000 K
        // new Color(0, 199, 255), // 16100 K
        // new Color(0, 198, 255), // 16200 K
        // new Color(0, 198, 255), // 16300 K
        // new Color(0, 198, 255), // 16400 K
        // new Color(9, 198, 255), // 16500 K
        // new Color(9, 198, 255), // 16600 K
        // new Color(9, 198, 255), // 16700 K
        // new Color(9, 197, 255), // 16800 K
        // new Color(9, 197, 255), // 16900 K
        // new Color(8, 197, 255), // 17000 K
        // new Color(8, 197, 255), // 17100 K
        // new Color(8, 197, 255), // 17200 K
        // new Color(8, 197, 255), // 17300 K
        // new Color(8, 196, 255), // 17400 K
        // new Color(7, 196, 255), // 17500 K
        // new Color(7, 196, 255), // 17600 K
        // new Color(7, 196, 255), // 17700 K
        // new Color(7, 196, 255), // 17800 K
        // new Color(7, 196, 255), // 17900 K
        // new Color(6, 196, 255), // 18000 K
        // new Color(6, 195, 255), // 18100 K
        // new Color(6, 195, 255), // 18200 K
        // new Color(6, 195, 255), // 18300 K
        // new Color(6, 195, 255), // 18400 K
        // new Color(6, 195, 255), // 18500 K
        // new Color(5, 195, 255), // 18600 K
        // new Color(5, 195, 255), // 18700 K
        // new Color(5, 194, 255), // 18800 K
        // new Color(5, 194, 255), // 18900 K
        // new Color(5, 194, 255), // 19000 K
        // new Color(5, 194, 255), // 19100 K
        // new Color(4, 194, 255), // 19200 K
        // new Color(4, 194, 255), // 19300 K
        // new Color(4, 194, 255), // 19400 K
        // new Color(4, 194, 255), // 19500 K
        // new Color(4, 194, 255), // 19600 K
        // new Color(4, 193, 255), // 19700 K
        // new Color(4, 193, 255), // 19800 K
        // new Color(3, 193, 255), // 19900 K
        // new Color(3, 193, 255), // 20000 K
        // new Color(3, 193, 255), // 20100 K
        // new Color(3, 193, 255), // 20200 K
        // new Color(3, 193, 255), // 20300 K
        // new Color(3, 193, 255), // 20400 K
        // new Color(3, 193, 255), // 20500 K
        // new Color(3, 192, 255), // 20600 K
        // new Color(2, 192, 255), // 20700 K
        // new Color(2, 192, 255), // 20800 K
        // new Color(2, 192, 255), // 20900 K
        // new Color(2, 192, 255), // 21000 K
        // new Color(2, 192, 255), // 21100 K
        // new Color(2, 192, 255), // 21200 K
        // new Color(2, 192, 255), // 21300 K
        // new Color(2, 192, 255), // 21400 K
        // new Color(1, 192, 255), // 21500 K
        // new Color(1, 192, 255), // 21600 K
        // new Color(1, 191, 255), // 21700 K
        // new Color(1, 191, 255), // 21800 K
        // new Color(1, 191, 255), // 21900 K
        // new Color(1, 191, 255), // 22000 K
        // new Color(1, 191, 255), // 22100 K
        // new Color(1, 191, 255), // 22200 K
        // new Color(1, 191, 255), // 22300 K
        // new Color(0, 191, 255), // 22400 K
        // new Color(0, 191, 255), // 22500 K
        // new Color(0, 191, 255), // 22600 K
        // new Color(0, 191, 255), // 22700 K
        // new Color(0, 190, 255), // 22800 K
        // new Color(0, 190, 255), // 22900 K
        // new Color(0, 190, 255), // 23000 K
        // new Color(0, 190, 255), // 23100 K
        // new Color(0, 190, 255), // 23200 K
        // new Color(0, 190, 255), // 23300 K
        // new Color(9, 190, 255), // 23400 K
        // new Color(9, 190, 255), // 23500 K
        // new Color(9, 190, 255), // 23600 K
        // new Color(9, 190, 255), // 23700 K
        // new Color(9, 190, 255), // 23800 K
        // new Color(9, 190, 255), // 23900 K
        // new Color(9, 190, 255), // 24000 K
        // new Color(9, 190, 255), // 24100 K
        // new Color(9, 189, 255), // 24200 K
        // new Color(9, 189, 255), // 24300 K
        // new Color(9, 189, 255), // 24400 K
        // new Color(8, 189, 255), // 24500 K
        // new Color(8, 189, 255), // 24600 K
        // new Color(8, 189, 255), // 24700 K
        // new Color(8, 189, 255), // 24800 K
        // new Color(8, 189, 255), // 24900 K
        // new Color(8, 189, 255), // 25000 K
        // new Color(8, 189, 255), // 25100 K
        // new Color(8, 189, 255), // 25200 K
        // new Color(8, 189, 255), // 25300 K
        // new Color(8, 189, 255), // 25400 K
        // new Color(8, 189, 255), // 25500 K
        // new Color(8, 189, 255), // 25600 K
        // new Color(7, 188, 255), // 25700 K
        // new Color(7, 188, 255), // 25800 K
        // new Color(7, 188, 255), // 25900 K
        // new Color(7, 188, 255), // 26000 K
        // new Color(7, 188, 255), // 26100 K
        // new Color(7, 188, 255), // 26200 K
        // new Color(7, 188, 255), // 26300 K
        // new Color(7, 188, 255), // 26400 K
        // new Color(7, 188, 255), // 26500 K
        // new Color(7, 188, 255), // 26600 K
        // new Color(7, 188, 255), // 26700 K
        // new Color(7, 188, 255), // 26800 K
        // new Color(7, 188, 255), // 26900 K
        // new Color(7, 188, 255), // 27000 K
        // new Color(6, 188, 255), // 27100 K
        // new Color(6, 188, 255), // 27200 K
        // new Color(6, 188, 255), // 27300 K
        // new Color(6, 187, 255), // 27400 K
        // new Color(6, 187, 255), // 27500 K
        // new Color(6, 187, 255), // 27600 K
        // new Color(6, 187, 255), // 27700 K
        // new Color(6, 187, 255), // 27800 K
        // new Color(6, 187, 255), // 27900 K
        // new Color(6, 187, 255), // 28000 K
        // new Color(6, 187, 255), // 28100 K
        // new Color(6, 187, 255), // 28200 K
        // new Color(6, 187, 255), // 28300 K
        // new Color(6, 187, 255), // 28400 K
        // new Color(6, 187, 255), // 28500 K
        // new Color(6, 187, 255), // 28600 K
        // new Color(5, 187, 255), // 28700 K
        // new Color(5, 187, 255), // 28800 K
        // new Color(5, 187, 255), // 28900 K
        // new Color(5, 187, 255), // 29000 K
        // new Color(5, 187, 255), // 29100 K
        // new Color(5, 187, 255), // 29200 K
        // new Color(5, 187, 255), // 29300 K
        // new Color(5, 187, 255), // 29400 K
        // new Color(5, 186, 255), // 29500 K
        // new Color(5, 186, 255), // 29600 K
        // new Color(5, 186, 255), // 29700 K
        // new Color(5, 186, 255), // 29800 K
        // new Color(5, 186, 255), // 29900 K
        // new Color(5, 186, 255), // 30000 K
        // new Color(5, 186, 255), // 30100 K
        // new Color(5, 186, 255), // 30200 K
        // new Color(5, 186, 255), // 30300 K
        // new Color(5, 186, 255), // 30400 K
        // new Color(5, 186, 255), // 30500 K
        // new Color(4, 186, 255), // 30600 K
        // new Color(4, 186, 255), // 30700 K
        // new Color(4, 186, 255), // 30800 K
        // new Color(4, 186, 255), // 30900 K
        // new Color(4, 186, 255), // 31000 K
        // new Color(4, 186, 255), // 31100 K
        // new Color(4, 186, 255), // 31200 K
        // new Color(4, 186, 255), // 31300 K
        // new Color(4, 186, 255), // 31400 K
        // new Color(4, 186, 255), // 31500 K
        // new Color(4, 186, 255), // 31600 K
        // new Color(4, 186, 255), // 31700 K
        // new Color(4, 186, 255), // 31800 K
        // new Color(4, 186, 255), // 31900 K
        // new Color(4, 185, 255), // 32000 K
        // new Color(4, 185, 255), // 32100 K
        // new Color(4, 185, 255), // 32200 K
        // new Color(4, 185, 255), // 32300 K
        // new Color(4, 185, 255), // 32400 K
        // new Color(4, 185, 255), // 32500 K
        // new Color(4, 185, 255), // 32600 K
        // new Color(3, 185, 255), // 32700 K
        // new Color(3, 185, 255), // 32800 K
        // new Color(3, 185, 255), // 32900 K
        // new Color(3, 185, 255), // 33000 K
        // new Color(3, 185, 255), // 33100 K
        // new Color(3, 185, 255), // 33200 K
        // new Color(3, 185, 255), // 33300 K
        // new Color(3, 185, 255), // 33400 K
        // new Color(3, 185, 255), // 33500 K
        // new Color(3, 185, 255), // 33600 K
        // new Color(3, 185, 255), // 33700 K
        // new Color(3, 185, 255), // 33800 K
        // new Color(3, 185, 255), // 33900 K
        // new Color(3, 185, 255), // 34000 K
        // new Color(3, 185, 255), // 34100 K
        // new Color(3, 185, 255), // 34200 K
        // new Color(3, 185, 255), // 34300 K
        // new Color(3, 185, 255), // 34400 K
        // new Color(3, 185, 255), // 34500 K
        // new Color(3, 185, 255), // 34600 K
        // new Color(3, 185, 255), // 34700 K
        // new Color(3, 185, 255), // 34800 K
        // new Color(3, 185, 255), // 34900 K
        // new Color(3, 184, 255), // 35000 K
        // new Color(3, 184, 255), // 35100 K
        // new Color(2, 184, 255), // 35200 K
        // new Color(2, 184, 255), // 35300 K
        // new Color(2, 184, 255), // 35400 K
        // new Color(2, 184, 255), // 35500 K
        // new Color(2, 184, 255), // 35600 K
        // new Color(2, 184, 255), // 35700 K
        // new Color(2, 184, 255), // 35800 K
        // new Color(2, 184, 255), // 35900 K
        // new Color(2, 184, 255), // 36000 K
        // new Color(2, 184, 255), // 36100 K
        // new Color(2, 184, 255), // 36200 K
        // new Color(2, 184, 255), // 36300 K
        // new Color(2, 184, 255), // 36400 K
        // new Color(2, 184, 255), // 36500 K
        // new Color(2, 184, 255), // 36600 K
        // new Color(2, 184, 255), // 36700 K
        // new Color(2, 184, 255), // 36800 K
        // new Color(2, 184, 255), // 36900 K
        // new Color(2, 184, 255), // 37000 K
        // new Color(2, 184, 255), // 37100 K
        // new Color(2, 184, 255), // 37200 K
        // new Color(2, 184, 255), // 37300 K
        // new Color(2, 184, 255), // 37400 K
        // new Color(2, 184, 255), // 37500 K
        // new Color(2, 184, 255), // 37600 K
        // new Color(2, 184, 255), // 37700 K
        // new Color(2, 184, 255), // 37800 K
        // new Color(2, 184, 255), // 37900 K
        // new Color(2, 184, 255), // 38000 K
        // new Color(2, 184, 255), // 38100 K
        // new Color(2, 184, 255), // 38200 K
        // new Color(1, 184, 255), // 38300 K
        // new Color(1, 184, 255), // 38400 K
        // new Color(1, 184, 255), // 38500 K
        // new Color(1, 183, 255), // 38600 K
        // new Color(1, 183, 255), // 38700 K
        // new Color(1, 183, 255), // 38800 K
        // new Color(1, 183, 255), // 38900 K
        // new Color(1, 183, 255), // 39000 K
        // new Color(1, 183, 255), // 39100 K
        // new Color(1, 183, 255), // 39200 K
        // new Color(1, 183, 255), // 39300 K
        // new Color(1, 183, 255), // 39400 K
        // new Color(1, 183, 255), // 39500 K
        // new Color(1, 183, 255), // 39600 K
        // new Color(1, 183, 255), // 39700 K
        // new Color(1, 183, 255), // 39800 K
        // new Color(1, 183, 255), // 39900 K
        // new Color(1, 183, 255), // 40000 K
    };

    /**
     * Get the Color on the blackbody spectrum for a given value in a given range.
     */
    public static Color colorValue(int value, int min, int max) {
        assert min <= value && value <= max;
        int range = max - min;
        int per   = range / spectrum.length;
        return spectrum[(value - min) / per];
    }

}
