package agh.ics.oop.model;

import java.util.Objects;

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

        return new Vector2d(this.x + other.x,this.y + other.y);
    }
    public Vector2d subtract(Vector2d other) {

        return new Vector2d(this.x - other.x,this.y - other.y);
    }
    /*
    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }
    boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
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

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }
*/
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