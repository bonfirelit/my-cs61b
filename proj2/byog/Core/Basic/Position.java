package byog.Core.Basic;


import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Position that) {
        if (this.x == that.x && this.y == that.y) {
            return true;
        }
        return false;
    }
}
