package agh.ics.oop.model;

import java.util.*;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows() {
        return this.x >= WorldMap.LOWER_LEFT.x && this.y >= WorldMap.LOWER_LEFT.y;
    }

    public Vector2d opposite(Vector2d lowerLeft, Vector2d upperRight) {
        int x1 = upperRight.getX();
        int x2 = lowerLeft.getX();

        if (this.getX() == x1) {
            return new Vector2d(x2, this.getY());
        }
        return new Vector2d(x1, this.getY());
    }

    public Set<Vector2d> around() {  //metoda na szybko na generowanie pozycji wokol truchla
        Set<Vector2d> positionsAround = new HashSet<>();
        positionsAround.add(new Vector2d(x, y + 1));
        positionsAround.add(new Vector2d(x + 1, y + 1));
        positionsAround.add(new Vector2d(x + 1, y));
        positionsAround.add(new Vector2d(x + 1, y - 1));
        positionsAround.add(new Vector2d(x, y - 1));
        positionsAround.add(new Vector2d(x - 1, y - 1));
        positionsAround.add(new Vector2d(x - 1, y));
        positionsAround.add(new Vector2d(x - 1, y + 1));
        return positionsAround;
    }

    public Vector2d upperRight(Vector2d other) {
        int maxX = Math.max(this.x, other.x);
        int maxY = Math.max(this.y, other.y);
        return new Vector2d(maxX, maxY);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int minX = Math.min(this.x, other.x);
        int minY = Math.min(this.y, other.y);
        return new Vector2d(minX, minY);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2d vector2d = (Vector2d) other;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}