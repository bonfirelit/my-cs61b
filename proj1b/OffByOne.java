public class OffByOne implements CharacterComparator{
    private int One;

    public OffByOne() {
        One = 1;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == One;
    }
}