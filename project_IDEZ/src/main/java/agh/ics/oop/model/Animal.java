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
            case STRAIGHT -> orientation;
            case DEGREE45 -> orientation.next();
            case DEGREE90 -> orientation.next().next();
            case DEGREE135 -> orientation.next().next().next();
            case DEGREE180 -> orientation.next().next().next().next();
            case DEGREE225 -> orientation.next().next().next().next().next();
            case DEGREE270 -> orientation.next().next().next().next().next().next();
            case DEGREE315 -> orientation.next().next().next().next().next().next().next();
        };

        Vector2d newPosition = position.add(orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        }

    }

    @Override //to jest nieważne bo tutaj bedzie sie wyswietlac kropka albo obrazek
    public String toString() {
        return orientation.toString().substring(0, 1);
    }
}