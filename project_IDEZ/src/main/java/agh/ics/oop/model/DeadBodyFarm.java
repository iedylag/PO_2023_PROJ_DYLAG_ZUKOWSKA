package agh.ics.oop.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class DeadBodyFarm extends WorldMap {

    public DeadBodyFarm(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength, int minMutation, int maxMutation) {
        super(grassCount, height, width, energyGrass, startingEnergyAnimal, reproduceEnergyLevel, genomeLength, minMutation, maxMutation);
    }

    private List<Vector2d> getNearBodyPositions() {
        return getDeadAnimals().keySet().stream()
                .flatMap(deadAnimalPosition -> deadAnimalPosition.around().stream())
                .distinct()
                .filter(position -> !isOccupied(position))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Vector2d> generateFromPreferablePositions(int count) {
        Set<Vector2d> preferablePositions = new HashSet<>();
        if (!getDeadAnimals().isEmpty()) {
            List<Vector2d> possiblePositions = getNearBodyPositions();
            if (!possiblePositions.isEmpty()) {
                Collections.shuffle(possiblePositions);
                int howManyIsPossible = possiblePositions.size();
                if (howManyIsPossible > count) {
                    howManyIsPossible = count;
                }
                for (Vector2d position : possiblePositions.subList(0, howManyIsPossible)) {
                    grasses.put(position, new Grass(position));
                    preferablePositions.add(position);
                }
            }
        }
        return preferablePositions;
    }
}
