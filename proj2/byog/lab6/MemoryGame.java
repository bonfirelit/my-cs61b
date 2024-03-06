package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        round = 1;
        playerTurn = false;
        gameOver = false;
        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[rand.nextInt(0, 26)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.line(0, height - 3, width, height - 3);
        StdDraw.text(5, height - 2, "Round:" + round);
        StdDraw.text(width - 9, height - 2, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length - 1)]);
        if (playerTurn) {
            StdDraw.text(width / 2 - 3, height - 2, "TYPE!");
        }
        else {
            StdDraw.text(width / 2 - 3, height - 2, "WATCH!");
        }
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters){
        //TODO: Display each character in letters, making sure to blank the screen between letters
        int len = letters.length();
        for (int i = 0; i < len; i++) {
            StdDraw.pause(500);
            drawFrame(Character.toString(letters.charAt(i)));
            StdDraw.pause(1000);
        }
        playerTurn = true;
        drawFrame(" ");
    }

    public String solicitNCharsInput(int n){
        //TODO: Read n letters of player input
        StringBuilder sb = new StringBuilder(n);
        while (sb.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                sb.append(StdDraw.nextKeyTyped());
                drawFrame(sb.toString());
            }
        }
        StdDraw.pause(500);
        return sb.toString();
    }

    public void startGame(){
        //TODO: Set any relevant variables before the game starts
        String target = null;
        String input = null;
        //TODO: Establish Game loop
        do {
            drawFrame("Round:" + round);
            StdDraw.pause(1000);
            target = generateRandomString(round);
            playerTurn = false;
            flashSequence(target);
            input = solicitNCharsInput(target.length());
            round++;
            gameOver = !input.equals(target);
        } while (!gameOver);
        round--;
        drawFrame("Game Over!You made it to round:" + round);
    }

}
