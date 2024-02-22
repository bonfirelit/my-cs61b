package byog.Core.Basic;

import byog.Core.Generator.RoomGenerator;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    static final int WIDTH = 100;
    static final int HEIGHT = 100;
    private TETile[][] world;
    private final Random rand;
    public World(Random rand) {
        world = new TETile[WIDTH][HEIGHT];
        this.rand = rand;
    }
    public TETile[][] getWorld() {
        return world;
    }
    /** place room on position p
     * @param p position
     * @param room room
     * */
    public void placeRoom(Room room, Position p) {
        int roomHeight = room.getHeight();
        int roomWidth = room.getWidth();
        room.setLowerLeft(roomHeight - 1 + p.getX(), p.getY());
        room.setUpperRight(p.getX(), roomWidth - 1 + p.getY());
        for (int i = 0; i < roomHeight; i++) {
            for (int j = 0; j < roomWidth; j++) {
                world[p.getX() + i][p.getY() + j] = room.getRoom()[i][j];
            }
        }
    }

    public void fillWorld() {
    }
    public void resetToNothing() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }
}
