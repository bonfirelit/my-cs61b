package byog.lab5;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Random;


import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 70;
    private static final Random RANDOM = new Random(114514);
    /** add hex on world in (x,y) which is the lower-left of hex */
    public static void addHexagon(TETile[][] world, int x, int y, int slen) {
        int row = slen * 2;
        int col = slen + (slen - 1) * 2;
        TETile[][] hex = new TETile[row][col];
        for (int j = 0; j < col; j += 1) {
            hex[slen][j] = Tileset.FLOWER;
            hex[slen - 1][j] = Tileset.FLOWER;
        }
        for (int i = slen + 1; i < row; i += 1) {
            copyAndDecrease(hex, i, i - 1, col);
        }
        for (int i = slen - 2; i >= 0; i -= 1) {
            copyAndDecrease(hex, i, i + 1, col);
        }
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < col; j += 1) {
                world[x + i][y + j] = hex[i][j];
            }
        }
    }

    private static void copyAndDecrease(TETile[][] hex, int row, int last, int col) {
        // copy
        for (int i = 0; i < col; i += 1) {
            hex[row][i] = hex[last][i];
        }
        // decrease
        if (hex[row][0] != Tileset.NOTHING && hex[row][col - 1] != Tileset.NOTHING) {
            hex[row][0] = Tileset.NOTHING;
            hex[row][col - 1] = Tileset.NOTHING;
            return;
        }
        int start = -1, end = -1;
        for (int i = 0; i < col - 1; i += 1) {
            if (hex[row][i] == Tileset.NOTHING && hex[row][i + 1] != Tileset.NOTHING) {
                start = i + 1;
            }
            if (hex[row][i] != Tileset.NOTHING && hex[row][i + 1] == Tileset.NOTHING) {
                end = i;
            }
        }
        hex[row][start] = Tileset.NOTHING;
        hex[row][end] = Tileset.NOTHING;
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        addHexagon(world, 20, 20, 4);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
