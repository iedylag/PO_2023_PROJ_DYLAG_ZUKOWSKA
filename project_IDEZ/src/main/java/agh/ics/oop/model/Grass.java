package agh.ics.oop.model;

import java.util.Objects;

import javafx.scene.paint.Color;

public record Grass(Vector2d position) implements WorldElement {

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

