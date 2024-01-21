package agh.ics.oop.model;

import java.util.Objects;

import javafx.scene.paint.Color;

public class Grass implements WorldElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
    }


    public Color toColor(int energyLevel) {
        return Color.rgb(30, 100, 10);
    }
}

