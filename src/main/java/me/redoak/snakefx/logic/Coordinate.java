package me.redoak.snakefx.logic;

public class Coordinate {

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate random(int maxX, int maxY) {
        return new Coordinate((int) (Math.random() * maxX), (int) (Math.random()*maxY));
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object other) {
        if(other.getClass().equals(Coordinate.class)) {
            Coordinate otherCoord = (Coordinate) other;

            return otherCoord.x == this.x && otherCoord.y == this.y;
        }

        return false;
    }

    @Override
    public String toString() {
        return x + "-" + y;
    }
}
