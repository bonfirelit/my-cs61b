package byog.Core.Basic;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

import static byog.Core.Basic.World.world;

public class Room {
    private int height;
    private int width;
    // bottom left conner position(x, y)
    private int x;
    private int y;
    private TETile[][] room;
    private final Random rand;


    public Room(long seed) {
        rand = new Random(seed);
        setPosition(); // 确定room的左下角在world中的位置坐标(x, y)
        int retry = 8;
        // while循环尝试在world的(x, y)处创建room
        do {
            setHeight();
            setWidth();
            retry--;
            if (retry == 0) break;
        } while (x + width >= World.WIDTH || y + height >= World.HEIGHT);
        if (retry == 0) {
            x = -1;
            y = -1;
            return;
        }
        room = new TETile[height][width];
        roundByWall();
        fillWithFloor();
        // 以上代码构造了一个room，并且在world中的坐标也已经确定，但还未将其放置到world中
    }

    public boolean isOverlap() {
        for (int X = x; X <=  x + width - 1; X++) {
            for (int Y = y; Y <= y + height - 1; Y++) {
                if (world[X][Y] == Tileset.WALL) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPosition() {
        x = rand.nextInt(World.WIDTH);
        y = rand.nextInt(World.HEIGHT);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setHeight() {
        height = rand.nextInt(3, World.HEIGHT / 2);
    }
    public int getHeight() {
        return height;
    }
    public void setWidth() {
        width = rand.nextInt(3, World.WIDTH / 2);
    }
    public int getWidth() {
        return width;
    }
    public void roundByWall() {
        for (int row = 0; row < height; row++) {
            room[row][0] = room[row][width - 1] = Tileset.WALL;
        }
        for (int col = 0; col < width; col++) {
            room[0][col] = room[height - 1][col] = Tileset.WALL;
        }
    }

    public void fillWithFloor() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (room[row][col] == Tileset.WALL) continue;
                room[row][col] = Tileset.FLOOR;
            }
        }
    }

    public TETile[][] getRoom() {
        return room;
    }

    public static void main(String[] args) {

    }
}
