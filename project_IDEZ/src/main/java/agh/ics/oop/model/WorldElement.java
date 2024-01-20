package agh.ics.oop.model;


public interface WorldElement {
    /**
     * @param position
     * @return True if WorldElement is on this position
     */
    boolean isAt(Vector2d position);

    Vector2d getPosition();

    javafx.scene.paint.Color toColor(int startEnergy);
}
