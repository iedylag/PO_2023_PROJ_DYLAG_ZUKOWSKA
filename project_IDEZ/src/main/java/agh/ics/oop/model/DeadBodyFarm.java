package agh.ics.oop.model;

import java.util.Collections;
import java.util.List;


public class DeadBodyFarm extends WorldMap {
    private int energyGrass;

    public DeadBodyFarm(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength) {
        super(grassCount, height, width, energyGrass, startingEnergyAnimal, reproduceEnergyLevel, genomeLength);
    }

    private List<Vector2d> getNearBodyPositions() {
        return getDeadAnimals().keySet().stream()
                .flatMap(deadAnimalPosition -> deadAnimalPosition.around().stream())
                .distinct()
                .filter(position -> !isOccupied(position))
                .toList();
    }

    @Override
    public void generateFromPreferablePosition(int width, int height) {
        int preferableGrassPlaces = (int) (0.2 * width * height);

        List<Vector2d> possiblePositions = getNearBodyPositions();
        if (preferableGrassPlaces < possiblePositions.size()) {
            if (!possiblePositions.isEmpty()) {
                Collections.shuffle(possiblePositions);
                Vector2d position = possiblePositions.getFirst();
                grasses.put(position, new Grass(position, energyGrass));
            }
        }
    }
}
