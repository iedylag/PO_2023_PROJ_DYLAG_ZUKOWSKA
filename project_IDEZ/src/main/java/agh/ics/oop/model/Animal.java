package agh.ics.oop.model;

import java.util.Objects;

public class Animal implements WorldElement {
    private static final MapDirection DEFAULT_MAP_DIRECTION = MapDirection.NORTH;
    private MapDirection orientation = DEFAULT_MAP_DIRECTION;
    private Vector2d position;

    private int energyLevel;

    //domyslny
    public Animal() {
        this(new Vector2d(2, 2));

    }

    //ustawiajacy pozycje
    public Animal(Vector2d position) {
        this.position = position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        orientation = switch (direction) {
            case RIGHT -> orientation.next();
            case LEFT -> orientation.previous();
            case FORWARD, BACKWARD -> orientation;
        };

        Vector2d newPosition = switch (direction) {
            case FORWARD -> position.add(orientation.toUnitVector());
            case BACKWARD -> position.subtract(orientation.toUnitVector());
            case RIGHT, LEFT -> position;
        };

        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        }

    }

    @Override
    public String toString() {
        return orientation.toString().substring(0, 1);
    }
}