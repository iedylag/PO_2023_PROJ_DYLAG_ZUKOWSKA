package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private List<Vector2d> possibleGrassFieldsPositions = new ArrayList<>();

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        for (int x = 0; x < maxHeight; x++) {
            for (int y = 0; y < maxWidth; y++) {
                possibleGrassFieldsPositions.add(new Vector2d(x, y));
            }
        }
        Collections.shuffle(possibleGrassFieldsPositions);

        possibleGrassFieldsPositions = possibleGrassFieldsPositions.subList(0, grassCount);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return possibleGrassFieldsPositions.iterator();
    }
}
