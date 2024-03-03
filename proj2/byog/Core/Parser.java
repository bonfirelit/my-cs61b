package byog.Core;

import byog.Core.Basic.World;

public class Parser {
    private int idx;
    public Parser() {
        idx = 0;
    }
    public void parse(String input) {
        String option = Character.toString(input.charAt(idx));
        idx++;
        option = option.toUpperCase();
        long seed = getSeed(input);
        if (idx == input.length()) {
            // Error: must enter S
            System.exit(1);
        }
        if (input.charAt(idx) == 's' || input.charAt(idx) == 'S') {
            // start game
            switch (option) {
                case "N":
//                newGame(seed);
                    break;
                case "L":
//                loadGame();
                    break;
                case "Q":
//                quit();
                    break;
            }
        }
    }
    private long getSeed(String input) {
        long seed = 0;
        for (; idx < input.length(); idx++) {
            char digit = input.charAt(idx);
            int num = digit - '0';
            if (num < 0 || num > 9) {
                break;
            }
            seed = seed * 10 + num;
        }
        return seed;
    }
    public static void main(String[] args) {

    }
}
