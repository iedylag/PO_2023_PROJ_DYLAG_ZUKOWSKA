package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private List<Vector2d> possiblePositions = new ArrayList<>();

    public RandomPositionGenerator(int maxWidth, int minHeight, int maxHeight, int count) {
        for (int x = 0; x < maxWidth; x++) {
            for (int y = minHeight; y < maxHeight; y++) {
                possiblePositions.add(new Vector2d(x, y));
            }
        }
        Collections.shuffle(possiblePositions);

        possiblePositions = possiblePositions.subList(0, count);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return possiblePositions.iterator();
    }
}
