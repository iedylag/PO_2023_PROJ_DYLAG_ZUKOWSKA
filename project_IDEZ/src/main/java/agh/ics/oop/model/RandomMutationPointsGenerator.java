package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomMutationPointsGenerator implements Iterable<Integer> {
    private List<Integer> possiblePositions = new ArrayList<>();

    public RandomMutationPointsGenerator(int min, int max, int genomeLength) {
        drawPoints(min, max);
        int mutationCount = possiblePositions.getFirst();

        drawPoints(0, genomeLength);
        possiblePositions = possiblePositions.subList(0, mutationCount);
    }

    private void drawPoints(int min, int max) {
        for (int i = min; i < max; i++) {
            possiblePositions.add(i);
        }
        Collections.shuffle(possiblePositions);
    }

    @Override
    public Iterator<Integer> iterator() {
        return possiblePositions.iterator();
    }
}
