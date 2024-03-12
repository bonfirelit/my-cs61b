package byog.Core.Basic;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private Position location;
    private TETile[][] world;

    public Player(int x, int y, World w) {
        location = new Position(x, y);
        world = w.getWorld();
    }

    public void moveUp() {
        int x = location.getX();
        int y = location.getY();
        if (isValid(x, y + 1)) {
            world[x][y] = Tileset.FLOOR;
            world[x][y + 1] = Tileset.PLAYER;
            location.setY(y + 1);
        }
    }
    public void moveDown() {
        int x = location.getX();
        int y = location.getY();
        if (isValid(x, y - 1)) {
            world[x][y] = Tileset.FLOOR;
            world[x][y - 1] = Tileset.PLAYER;
            location.setY(y - 1);
        }
    }
    public void moveLeft() {
        int x = location.getX();
        int y = location.getY();
        if (isValid(x - 1, y)) {
            world[x][y] = Tileset.FLOOR;
            world[x - 1][y] = Tileset.PLAYER;
            location.setX(x - 1);
        }
    }
    public void moveRight() {
        int x = location.getX();
        int y = location.getY();
        if (isValid(x + 1, y)) {
            world[x][y] = Tileset.FLOOR;
            world[x + 1][y] = Tileset.PLAYER;
            location.setX(x + 1);
        }
    }
    private boolean isValid(int x, int y) {
        return world[x][y].equals(Tileset.FLOOR) || world[x][y].equals(Tileset.LOCKED_DOOR);
    }
    public int getLocationX() {
        return location.getX();
    }
    public int getLocationY() {
        return location.getY();
    }
    public Position getLocation() {
        return location;
    }

    public static void main(String[] args) {

    }
}
