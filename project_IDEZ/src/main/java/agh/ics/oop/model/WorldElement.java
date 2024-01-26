package agh.ics.oop.model;


public interface WorldElement {
    Vector2d position();

    javafx.scene.paint.Color toColor(int startEnergy);
}
