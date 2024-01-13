package agh.ics.oop.model;

import java.util.Objects;

public class Grass implements WorldElement {
    private final Vector2d position;
    private int energyLevel;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energyLevel;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
    }

    public void wasConsumed() {
        energyLevel = 0;
    }
}

