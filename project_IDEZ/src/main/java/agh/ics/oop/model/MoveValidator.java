package agh.ics.oop.model;

import java.util.Optional;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    void animalOnTheEdge(Vector2d newPosition, MapDirection direction);

    default boolean isOccupied(Vector2d position) {
        return objectAt(position).isPresent();
    }

    Optional<WorldElement> objectAt(Vector2d position);
}
