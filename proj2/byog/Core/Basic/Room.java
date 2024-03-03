package byog.Core.Basic;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

import static byog.Core.Basic.World.world;

public class Room implements Comparable<Room>{
    private int height;
    private int width;
    private Position position;
    private TETile[][] room;
    private final Random rand;


    public Room(long seed) {
        rand = new Random(seed);
        setPosition(); // 确定room的左下角在world中的位置坐标(x, y)
        int x = position.getX();
        int y = position.getY();
        int retry = 10; // 随机生成的room可能超出地图范围，故设置retry
        // while循环尝试在world的(x, y)处创建room
        do {
            setHeight();
            setWidth();
            retry--;
            if (retry == 0) break;
        } while (x + width >= World.WIDTH || y + height >= World.HEIGHT);
        if (retry == 0) {
            position = new Position(-1, -1);
            return;
        }
        room = new TETile[height][width];
        roundByWall();
        fillWithFloor();
        // 以上代码构造了一个room，并且在world中的坐标也已经确定，但还未将其放置到world中
    }

    public boolean isOverlap() {
        int x = position.getX();
        int y = position.getY();
        for (int X = x; X <=  x + width - 1; X++) {
            for (int Y = y; Y <= y + height - 1; Y++) {
                if (world[X][Y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPosition() {
        int x = rand.nextInt(World.WIDTH);
        int y = rand.nextInt(World.HEIGHT);
        position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setHeight() {
        height = rand.nextInt(10, 20);
    }
    public int getHeight() {
        return height;
    }
    public void setWidth() {
        width = rand.nextInt(10, 20);
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

    @Override
    public int compareTo(Room that) {
        return Integer.compare(this.getPosition().getX(), that.getPosition().getX());
    }

    public static void main(String[] args) {

    }
}
