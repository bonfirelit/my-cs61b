package byog.Core.Generator;

import byog.Core.Basic.Position;
import byog.Core.Basic.Room;
import byog.Core.Basic.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Generate world randomly */
public class WorldGenerator {
    private World world;
    private final Random rand;
    private final long seed;
    public WorldGenerator(long seed) {
        this.seed = seed;
        rand = new Random(this.seed);
        world = new World(this.rand);
    }

    public World generateWorld() {
        world.resetToNothing();
        world.fillWorld();
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        WorldGenerator wg = new WorldGenerator(1233);
        World world = wg.generateWorld();
        ter.initialize(world.getHeight(), world.getWidth());

        ter.renderFrame(world.getWorld());
    }
}
