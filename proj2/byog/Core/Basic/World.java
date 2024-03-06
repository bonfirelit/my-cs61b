package byog.Core.Basic;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class World {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int UP = 2;
    private final int DOWN = 3;


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
            if (room.getPosition().getX() == -1) { // 生成失败
                continue;
            }
            rooms.add(room);
        }
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
        // 对剩下的room按x坐标大小排序
        Collections.sort(rooms);
    }

    private void placeOneRoom(Room room) {
        int x = room.getPosition().getX();
        int y = room.getPosition().getY();
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

    public void addLockedDoor() {
        // 随机选择一个room
        Room r = rooms.get(rand.nextInt(0, rooms.size()));
        // 选择room的一条边
        int side = rand.nextInt(0, 4);
        // 选择一个点
        Position p = choose(r, side);
        // 改为lockedDoor
        world[p.getX()][p.getY()] = Tileset.LOCKED_DOOR;
    }

    public void connectAllRoom() {
        for (int i = 0; i < rooms.size() - 1; i += 1) {
            connectRoom(rooms.get(i), rooms.get(i + 1));
        }
    }

    // connect room1 and room2
    private void connectRoom(Room room1, Room room2) {
        // dir是room2相对于room1的位置
        int dir = direction(room1, room2);
        assert dir != -1: "direction error";
        Position p1 = room1.getPosition();
        Position p2 = room2.getPosition();
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        Position right = null;
        Position left = null;
        Position down = null;
        Position up = null;
        switch (dir) {
            case RIGHT: // room2 在 room1 右边
                right = choose(room1, RIGHT); // 在room1的右边框选一个点
                left = choose(room2, LEFT); // 在room2左边框选一个点
                connectRight2Left(right, left); // 将右边框的点连接到左边框的点
                break;
            case LEFT: // room2 在 room1 左边， 也即 room1 在 room2 右边
                right = choose(room2, RIGHT); // 在room2的右边框选一个点
                left = choose(room1, LEFT); // 在room1的左边框选一个点
                connectRight2Left(right, left); // 将右边框的点连接到左边框的点
                break;
            case DOWN: // room2 在 room1 下边
                down = choose(room1, DOWN);
                up = choose(room2, UP);
                connectDown2Up(down, up);
                break;
            case UP:
                down = choose(room2, DOWN);
                up = choose(room1, UP);
                connectDown2Up(down, up);
                break;
        }

    }

    // 随机选择点，保证不要选到顶点
    private Position choose(Room room, int side) {
        int width = room.getWidth();
        int height = room.getHeight();
        int x = room.getPosition().getX();
        int y = room.getPosition().getY();
        int X, Y;
        Position p;
        if (side == RIGHT) {
            do {
                Y = rand.nextInt(y + 1, y + height - 1);
                p = new Position(x + width - 1, Y);
            } while (!isValid(p, RIGHT));
            return p;
        }
        if (side == LEFT) {
            do {
                Y = rand.nextInt(y + 1, y + height - 1);
                p = new Position(x, Y);
            } while (!isValid(p, LEFT));
            return p;
        }
        if (side == UP) {
            do {
                X = rand.nextInt(x + 1, x + width - 1);
                p = new Position(X, y + height - 1);
            } while (!isValid(p, UP));
            return p;
        }
        if (side == DOWN) {
            do {
                X = rand.nextInt(x + 1, x + width - 1);
                p = new Position(X, y);
            } while (!isValid(p, DOWN));
            return p;
        }
        return null;
    }

    // 判断所选取的点是否合法，否则会导致路径相互覆盖
    private boolean isValid(Position p, int side) {
        return true; // 使用putWall函数后，似乎不需要判断了
//        int x = p.getX();
//        int y = p.getY();
//        if (side == LEFT || side == RIGHT) {
//            return world[x][y] == Tileset.WALL && world[x][y - 1] == Tileset.WALL && world[x][y + 1] == Tileset.WALL;
//        }
//        return world[x][y] == Tileset.WALL && world[x - 1][y] == Tileset.WALL && world[x + 1][y] == Tileset.WALL;
    }

    // down是room下边框的点，up是room上边框的点
    private void connectDown2Up(Position down, Position up) {
        assert down != null && up != null: "connectDown2Up error";
        int x1 = down.getX();
        int y1 = down.getY();
        int x2 = up.getX();
        int y2 = up.getY();
        int y3 = y1 - rand.nextInt(1, y1 - y2);
        for (int y = y1; y >= y3; y--) {
            world[x1][y] = Tileset.FLOOR;
            putWall(x1 - 1, y);
            putWall(x1 + 1, y);
        }
        if (x1 < x2) {
            putWall(x1 - 1, y3 - 1);
            putWall(x1, y3 - 1);
            for (int x = x1 + 1; x <= x2; x++) {
                world[x][y3] = Tileset.FLOOR;
                putWall(x, y3 + 1);
                putWall(x, y3 - 1);
            }
            putWall(x2 + 1, y3 + 1);
            putWall(x2 + 1, y3);
        }
        else {
            putWall(x1 + 1, y3 - 1);
            putWall(x1, y3 - 1);
            for (int x = x1 - 1; x >= x2; x--) {
                world[x][y3] = Tileset.FLOOR;
                putWall(x, y3 + 1);
                putWall(x, y3 - 1);
            }
            putWall(x2 - 1, y3 + 1);
            putWall(x2 - 1, y3);
        }
        for (int y = y3 - 1; y >= y2; y--) {
            world[x2][y] = Tileset.FLOOR;
            putWall(x2 - 1, y);
            putWall(x2 + 1, y);
        }
    }

    // right是room右边框上的点，left是room左边框上的点，right和left并*不是*指点的位置
    private void connectRight2Left(Position right, Position left) {
        assert right != null && left != null: "connectRight2Left Error";
        int x1 = right.getX();
        int y1 = right.getY();
        int x2 = left.getX();
        int y2 = left.getY();
        int x3 = x1 + rand.nextInt(1, x2 - x1);
        for (int x = x1; x <= x3; x++) {
            world[x][y1] = Tileset.FLOOR;
            putWall(x, y1 - 1);
            putWall(x, y1 + 1);
        }
        if (y2 > y1) {
            // 拐角
            putWall(x3 + 1, y1 - 1);
            putWall(x3 + 1, y1);
            for (int y = y1 + 1; y <= y2; y++) {
                world[x3][y] = Tileset.FLOOR;
                putWall(x3 + 1, y);
                putWall(x3 - 1, y);
            }
            // 拐角
            putWall(x3 - 1, y2 + 1);
            putWall(x3, y2 + 1);
        }
        else {
            putWall(x3 + 1, y1 + 1);
            putWall(x3 + 1, y1);
            for (int y = y1 - 1; y >= y2; y--) {
                world[x3][y] = Tileset.FLOOR;
                putWall(x3 + 1, y);
                putWall(x3 - 1, y);
            }
            putWall(x3 - 1, y2 - 1);
            putWall(x3, y2 - 1);
        }
        for (int x = x3 + 1; x <= x2; x++) {
            world[x][y2] = Tileset.FLOOR;
            putWall(x, y2 - 1);
            putWall(x, y2 + 1);
        }
    }

    // 用于在生成hallway时在world中放置WALL
    private void putWall(int x, int y) {
        if (world[x][y] == Tileset.FLOOR) { // 防止形成死路
            return;
        }
        world[x][y] = Tileset.WALL;
    }

    // 返回room2相对于room1的位置
    private int direction(Room r1, Room r2) {
        Position p1 = r1.getPosition();
        Position p2 = r2.getPosition();
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();
        if (x1 + r1.getWidth() < x2) {
            return RIGHT;
        }
        else if (x2 + r2.getWidth() < x1) {
            return LEFT;
        }
        else if (y1 + r1.getHeight() < y2) {
            return UP;
        }
        else if (y2 + r2.getHeight() < y1) {
            return DOWN;
        }
        return -1;
    }


    public  TETile[][] getWorld() {
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Random rand = new Random(4567);
        World world = new World(rand.nextInt());
        world.fillWithNothing();
        world.makeRooms(rand.nextInt(70, 100));
        world.placeRoom();
        world.connectAllRoom();
        world.addLockedDoor();
        ter.renderFrame(world.getWorld());
    }
}
