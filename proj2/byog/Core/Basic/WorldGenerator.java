package byog.Core.Basic;


import java.util.Random;

public class WorldGenerator {
    private World world;
    private Random rand;

    public WorldGenerator(long seed, int width, int height) {
        rand = new Random(seed);
        world = new World(seed, width, height);
        world.fillWithNothing();
        world.makeRooms(rand.nextInt(100 - 70) + 70);
        world.placeRoom();
        world.connectAllRoom();
        world.addLockedDoor();
        world.createPlayer();
    }

    public World getWorld() {
        return world;
    }
    public static void main(String[] args) {

    }
}
