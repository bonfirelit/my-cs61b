package byog.Core;

import byog.Core.Basic.World;
import byog.Core.Basic.WorldGenerator;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        initialize();
        showMenu();
        while (true) {
            char c = 0;
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
            }
            c = Character.toUpperCase(c);
            switch (c) {
                case 'N':
                    startGame(makeWorld(getSeed()));
                    break;
                case 'L':
//                    load();
                    break;
                case 'Q':
                    // quit();
                    break;
            }
            if (c == 'N' || c == 'L' || c == 'Q') {
                break;
            }
        }

    }

    private void startGame(World world) {
        Font font = new Font("Dialog", Font.PLAIN, 15);
        StdDraw.setFont(font);
        while (true) {
            drawFrame(world);
        }
    }

    private void drawFrame(World world) {
        ter.renderFrame(world.getWorld());
    }

    private long getSeed() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "Enter Seed");
            char c = 0;
            if (StdDraw.hasNextKeyTyped()) {
                c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                } else if (c == 'S') {
                    if (sb.isEmpty()) {
                        continue;
                    }
                    else {
                        break;
                    }
                } else {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Enter Integer or S");
                    StdDraw.pause(500);
                }
            }
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, sb.toString());
            StdDraw.show();
        }
        return Long.parseLong(sb.toString());
    }

    private void initialize() {
        StdDraw.setCanvasSize(WIDTH * 15, HEIGHT * 15);
        Font font = new Font("Dialog", Font.PLAIN, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void showMenu() {
        int midX = WIDTH / 2;
        int midY = HEIGHT / 2;
        StdDraw.text(midX, midY + 10, "CS61B  THE GAME");
        StdDraw.text(midX, midY, "New Game(N)");
        StdDraw.text(midX, midY - 4, "Load Game(L)");
        StdDraw.text(midX, midY - 8, "Quit(Q)");
        StdDraw.show();
    }

    private void showTileInfo(int x, int y) {

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        Parser parser = new Parser();
        parser.parse(input);
        String option = parser.getOption();
        long seed = parser.getSeed();
        World world = null;
        // start game
        switch (option) {
            case "N":
                world = makeWorld(seed);
                break;
            case "L":
//                loadGame();
                break;
            case "Q":
//                quit();
                break;
            default:
                System.out.println("Please enter valid string");
                System.exit(1);
        }
        TETile[][] finalWorldFrame = world.getWorld();
        return finalWorldFrame;
    }

    private World makeWorld(long seed) {
        WorldGenerator wg = new WorldGenerator(seed, WIDTH, HEIGHT);
        return wg.getWorld();
    }
}
