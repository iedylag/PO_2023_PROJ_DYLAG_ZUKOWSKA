package agh.ics.oop.model;


import javafx.scene.paint.Color;

public record Grass(Vector2d position) implements WorldElement {

    @Override
    public String toString() {
        return "*";
    }

    public Color toColor(int energyLevel) {
        return Color.rgb(30, 100, 10);
    }
}

