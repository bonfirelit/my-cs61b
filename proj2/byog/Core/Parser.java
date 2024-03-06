package byog.Core;

import byog.Core.Basic.World;

public class Parser {
    private String option;
    private long seed;

    public Parser() {
        option = null;
        seed = 0;
    }

    public void parse(String input) {
        option = Character.toString(input.charAt(0));
        option = option.toUpperCase();
        input = input.substring(1);
        seed = getSeed(input);
    }

    public String getOption() {
        return option;
    }

    public long getSeed() {
        return seed;
    }

    private long getSeed(String input) {
        long seed = 0;
        int i;
        for (i = 0; isDigit(input.charAt(i)); i++) {
            int t = input.charAt(i) - '0';
            seed = seed * 10 + t;
        }
        return seed;
    }

    private boolean isDigit(char c) {
        return c - '0' >= 0 && c - '0' <= 9;
    }
    public static void main(String[] args) {

    }
}
