package byog.Core.Basic;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Room {
    private TETile[][] room;
    private int width;
    private int height;
    private Position lowerLeft;
    private Position upperRight;
    private Random rand;
    public Room(long seed) {
        rand = new Random(seed);
        setHeight(rand);
        setWidth(rand);
        room = new TETile[this.height][this.width];
        setRoomPosition();
        roundByWall();
        fillRoom();
    }
    /** for intersection room */
    public Room(Position lowerLeft, Position upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }
    private void setHeight(Random rand) {
        this.height = rand.nextInt(3, 20);
    }
    private void setRoomPosition() {
        Position p = new Position();
        p.setX(rand.nextInt(World.HEIGHT / 2));
        p.setY(rand.nextInt(World.WIDTH / 2));
        setLowerLeft(height - 1 + p.getX(), p.getY());
        setUpperRight(p.getX(), width - 1 + p.getY());
    }
    private void setWidth(Random rand) {
        this.width = rand.nextInt(3,20);
    }
    private void roundByWall() {
        int i;
        // row
        for (i = 0; i < upperRight.getX() - lowerLeft.getX(); i++) {
            room[upperRight.getY()][lowerLeft.getX() + i] = Tileset.WALL;
            room[lowerLeft.getY()][lowerLeft.getX() + i] = Tileset.WALL;
        }
        // col
        for (i = 0; i < upperRight.getY() - lowerLeft.getY(); i++) {
            room[upperRight.getX() + i][lowerLeft.getY()] = Tileset.WALL;
            room[upperRight.getX() + i][upperRight.getY()] = Tileset.WALL;
        }
    }

    public void roundByFloor() {
        int i;
        // row
        for (i = 0; i < upperRight.getX() - lowerLeft.getX(); i++) {
            room[upperRight.getY()][lowerLeft.getX() + i] = Tileset.FLOOR;
            room[lowerLeft.getY()][lowerLeft.getX() + i] = Tileset.FLOOR;
        }
        // col
        for (i = 0; i < upperRight.getY() - lowerLeft.getY(); i++) {
            room[upperRight.getX() + i][lowerLeft.getY()] = Tileset.FLOOR;
            room[upperRight.getX() + i][upperRight.getY()] = Tileset.FLOOR;
        }
    }

    private void fillRoom() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                room[i][j] = Tileset.FLOOR;
            }
        }
    }
    public boolean intersectWith(Room that) {
        int x1 = this.lowerLeft.getX();
        int y1 = this.lowerLeft.getY();
        int x2 = this.upperRight.getX();
        int y2 = this.upperRight.getY();
        int x3 = that.lowerLeft.getX();
        int y3 = that.lowerLeft.getY();
        int x4 = that.upperRight.getX();
        int y4 = that.upperRight.getY();
        return Math.max(x1, x3) <= Math.max(x2, x4) && Math.max(y1, y3) <= Math.max(y2, y4);
    }

    public Position getLowerLeft() {
        return lowerLeft;
    }
    public Position getUpperRight() {
        return upperRight;
    }

    /** the lowerleft conner in world */
    public void setLowerLeft(int x, int y) {
        lowerLeft = new Position(x, y);
    }
    /** the upperright conner in world */
    public void setUpperRight(int x, int y) {
        upperRight = new Position(x, y);
    }

    public TETile[][] getRoom() {
        return room;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
