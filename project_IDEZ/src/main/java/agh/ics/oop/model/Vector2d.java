package agh.ics.oop.model;

import java.util.*;

public record Vector2d(int x, int y) {
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
        int x1 = upperRight.x();
        int x2 = lowerLeft.x();

        if (this.x() == x1) {
            return new Vector2d(x2, this.y());
        }
        return new Vector2d(x1, this.y());
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

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}