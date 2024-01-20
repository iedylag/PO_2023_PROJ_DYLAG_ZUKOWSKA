package agh.ics.oop.model;

import java.awt.*;

public interface WorldElement {
    /**
     * @param position
     * @return True if WorldElement is on this position
     */
    boolean isAt(Vector2d position);

    Vector2d getPosition();

    int getEnergy();

    javafx.scene.paint.Color toColor(int startEnergy);
}
