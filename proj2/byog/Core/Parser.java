package byog.Core;


public class Parser {
    private String option;
    private long seed;
    private String move;
    private int idx;

    public Parser(String input) {
        option = null;
        seed = 0;
        move = null;
        idx = 0;
        parse(input);
    }

    public String getMove() {
        return move;
    }

    private void parse(String input) {
        option = Character.toString(input.charAt(idx)).toUpperCase();
        idx++;
        if (option.equals("L")) {
            move = input.substring(idx);
            return;
        }
        seed = parseSeed(input);
        char c = input.charAt(idx);
        if (c != 'S' && c != 's') {
            System.out.println("Invalid input");
            System.exit(1);
        }
        idx++;
        move = input.substring(idx);
    }

    public String getOption() {
        return option;
    }

    public long getSeed() {
        return seed;
    }

    private long parseSeed(String input) {
        long seed = 0;
        for (; isDigit(input.charAt(idx)); idx++) {
            int t = input.charAt(idx) - '0';
            seed = seed * 10 + t;
        }
        return seed;
    }

    private boolean isDigit(char c) {
        return c - '0' >= 0 && c - '0' <= 9;
    }

    public static void main(String[] args) {
        String input = "LWWSDS:Q";
        Parser parser = new Parser(input);
        String option = parser.getOption();
        long seed = parser.getSeed();
        String move = parser.getMove();
        System.out.println(seed);
        System.out.println(move);
        System.out.println(option);
    }
}
