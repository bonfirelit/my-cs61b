package byog.Core.Basic;

import byog.Core.Generator.RoomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rooms {
    private List<Room> rooms;
    private int size;
    private Random rand;
    public Rooms(int size, Random rand, World world) {
        rooms = new ArrayList<>();
        this.size = size;
        this.rand = rand;
        for (int i = 0; i < size; i++) {
            RoomGenerator rg = new RoomGenerator(rand.nextInt());
            rooms.add(rg.generateRoom());
        }
    }
    public void handleOverlap() {
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                if (rooms.get(i).intersectWith(rooms.get(j))) {
                    int x1 = rooms.get(i).getLowerLeft().getX();
                    int y1 = rooms.get(i).getLowerLeft().getY();
                    int x2 = rooms.get(i).getUpperRight().getX();
                    int y2 = rooms.get(i).getUpperRight().getY();
                    int x3 = rooms.get(j).getLowerLeft().getX();
                    int y3 = rooms.get(j).getLowerLeft().getY();
                    int x4 = rooms.get(j).getUpperRight().getX();
                    int y4 = rooms.get(j).getUpperRight().getY();
                    Position ll = new Position(Math.max(x1, x3), Math.max(y1, y3));
                    Position ur = new Position(Math.max(x2, x4), Math.max(y2, y4));
                    Room intersection = new Room(ll, ur);
                    intersection.roundByFloor();
                }
            }
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public int getSize()  {
        return size;
    }
}
