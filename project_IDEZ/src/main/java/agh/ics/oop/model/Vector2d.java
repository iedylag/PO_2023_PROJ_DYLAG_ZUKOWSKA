package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Vector2d(int x, int y) {

    private static final Vector2d[] AROUND_SET = {new Vector2d(0, 1), new Vector2d(1, 1), new Vector2d(1, 0), new Vector2d(1, -1),
            new Vector2d(0, -1), new Vector2d(-1, -1), new Vector2d(-1, 0), new Vector2d(-1, 1)
    };
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows() {
        return this.x >= WorldMap.LOWER_LEFT.x && this.y >= WorldMap.LOWER_LEFT.y;
    }

    public Vector2d oppositeX(Vector2d lowerLeft, Vector2d upperRight) {
        int newX = upperRight.x - (this.x - lowerLeft.x);
        return new Vector2d(newX, this.y());
    }

    public Set<Vector2d> around() {
        return Stream.of(AROUND_SET)
                .map(posAround -> add(this.add(posAround)))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}