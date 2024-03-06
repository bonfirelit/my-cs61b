package byog.Core.Basic;

import byog.Core.RandomUtils;

import java.util.Random;

public class WorldGenerator {
    private World world;
    private Random rand;

    public WorldGenerator(long seed) {
        rand = new Random(seed);
        world = new World(seed);
        world.fillWithNothing();
        world.makeRooms(rand.nextInt(70, 100));
        world.placeRoom();
        world.connectAllRoom();
        world.addLockedDoor();
    }

    public World getWorld() {
        return world;
    }
    public static void main(String[] args) {

    }
}
