package byog.Core.Basic;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    protected static TETile[][] world;
    private List<Room> rooms;
    private Random rand;

    public World(long SEED) {
        rand = new Random(SEED);
        world = new TETile[HEIGHT][WIDTH];
        rooms = new ArrayList<>();
    }

    public void fillWithNothing() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void makeRooms(int numOfRoom) {
        for (int i = 0; i < numOfRoom; i++) {
            int seed = rand.nextInt();
            Room room = new Room(seed);
            if (room.getX() == -1) { // 生成失败
                continue;
            }
            rooms.add(room);
        }
        System.out.println("room size = " + rooms.size());
    }

    public void placeRoom() {
        Iterator<Room> iterator = rooms.iterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.isOverlap()) { // 去除重叠
                iterator.remove();
                continue;
            }
            placeOneRoom(room);
        }
        System.out.println("room size = " + rooms.size());
    }

    private void placeOneRoom(Room room) {
        int x = room.getX();
        int y = room.getY();
        int height = room.getHeight();
        int width = room.getWidth();
        TETile[][] r = room.getRoom();
        int row = height, col = 0;
        for (int Y = y; Y < y + height; Y++) {
            row--;
            col = 0;
            for (int X = x; X < x + width; X++) {
                world[X][Y] = r[row][col];
                col++;
            }
        }
    }

    

    public  TETile[][] getWorld() {
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(100, 100);
        Random rand = new Random(11454);
        World world = new World(rand.nextInt());
        world.fillWithNothing();
        world.makeRooms(rand.nextInt(50, 100));
        world.placeRoom();
        ter.renderFrame(world.getWorld());
    }
}
