package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class DeadBodyFarm extends WorldMap {

    List<Vector2d> preferablePositions = new ArrayList<>();

    public DeadBodyFarm(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength, int minMutation, int maxMutation) {
        super(grassCount, height, width, energyGrass, startingEnergyAnimal, reproduceEnergyLevel, genomeLength, minMutation, maxMutation);
    }

    public void getPreferablePositions() {
        preferablePositions = getGraves().stream()
                .flatMap(bodyPosition -> bodyPosition.around().stream())
                .filter(bodyPosition -> bodyPosition.precedes(getUpperRight()))
                .filter(Vector2d::follows)
                .distinct()
                .filter(position -> !isOccupiedByPlant(position))
                .collect(Collectors.toList());
    }

    @Override
    public void generateFromOtherPositions(int count) {
        List<Vector2d> otherPositions;
        if (!getGraves().isEmpty()) {
            otherPositions = allPositions.stream()
                    .filter(pos -> !isOccupiedByPlant(pos))
                    .filter(pos -> !preferablePositions.contains(pos))
                    .collect(Collectors.toList());
        } else {
            otherPositions = allPositions.stream()
                    .filter(pos -> !isOccupiedByPlant(pos))
                    .collect(Collectors.toList());
        }

        if(emptyPositionsNumber() < count){
            count = emptyPositionsNumber();
        }

        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(otherPositions, count);
        for (Vector2d grassPosition : positionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public void generateFromPreferablePositions(int count) {
        getPreferablePositions();
        if (!getGraves().isEmpty()) {
            if (!preferablePositions.isEmpty()) {
                int howManyIsPossible = preferablePositions.size();
                if (howManyIsPossible > count) {
                    RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(preferablePositions, count);
                    for (Vector2d position : randomPositionGenerator) {
                        grasses.put(position, new Grass(position));
                    }
                } else {
                    for (Vector2d position : preferablePositions) {
                        grasses.put(position, new Grass(position));
                    }
                }


            }
        }
    }
}
