package byog.Core.Generator;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.Core.Basic.Room;
import java.util.Random;

public class RoomGenerator {
    private Random random;
    public RoomGenerator(long seed) {
        random = new Random(seed);
    }
    public Room generateRoom() {
        return new Room(random.nextInt());
    }

}

