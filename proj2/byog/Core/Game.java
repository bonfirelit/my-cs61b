package byog.Core;

import byog.Core.Basic.Player;
import byog.Core.Basic.World;
import byog.Core.Basic.WorldGenerator;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private World world;
    private Player player;

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
                    world = makeWorld(getSeed());
                    player = world.getPlayer();
                    startGame();
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

    private void startGame() {
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                switch (c) {
                    case 'W':
                        player.moveUp();
                        break;
                    case 'S':
                        player.moveDown();
                        break;
                    case 'D':
                        player.moveRight();
                        break;
                    case 'A':
                        player.moveLeft();
                        break;
                }
            }
            drawFrame();
            if (isWin()) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.YELLOW);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "YOU WIN");
                StdDraw.show();
                break;
            }
        }
    }

    private boolean isWin() {
        return world.reachDoor();
    }

    private void drawFrame() {
        drawWorld();
        drawUI();
        StdDraw.show();
    }

    private void drawWorld() {
        ter.renderFrame(world.getWorld());
    }

    private void drawUI() {
        StdDraw.setPenColor(Color.WHITE);
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return;
        }
        showTileInfo((int)x, (int)y);
    }

    private void showTileInfo(int x, int y) {
        TETile[][] tiles = world.getWorld();
        if (tiles[x][y] == Tileset.FLOOR) {
            StdDraw.text(3, HEIGHT - 1, "FLOOR");
        }
        else if (tiles[x][y] == Tileset.LOCKED_DOOR) {
            StdDraw.text(3, HEIGHT - 1, "LOCKED DOOR");
        }
        else if (tiles[x][y] == Tileset.WALL) {
            StdDraw.text(3, HEIGHT - 1, "WALL");
        }
        else if (tiles[x][y] == Tileset.PLAYER) {
            StdDraw.text(3, HEIGHT - 1, "YOU");
        }
        else {
            StdDraw.text(3, HEIGHT - 1, "NOTHING");
        }
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
                } else if (c == 'S' && !sb.isEmpty()) {
                    break;
                } else {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Enter Integer or S");
                }
            }
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, sb.toString());
            StdDraw.show();
        }
        return Long.parseLong(sb.toString());
    }

    private void initialize() {
        StdDraw.setCanvasSize(WIDTH * 15, HEIGHT * 15);
        Font font = new Font("Monaco", Font.PLAIN, 30);
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
        // start game
        switch (option) {
            case "N":
                world = makeWorld(seed);
                player = world.getPlayer();
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
