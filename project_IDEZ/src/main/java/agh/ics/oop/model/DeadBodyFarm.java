package agh.ics.oop.model;

import java.util.Collections;
import java.util.List;


public class DeadBodyFarm extends WorldMap {
    private int energyGrass;

    public DeadBodyFarm(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength) {
        super(grassCount, height, width, energyGrass, startingEnergyAnimal, reproduceEnergyLevel, genomeLength);
    }

    private List<Vector2d> getNearBodyPositions(List<Animal> deadAnimals) { //metoda zwracajaca pozycje wokol wszystkich martwych zwierzat
        return deadAnimals.stream()
                .flatMap(deadAnimal -> deadAnimal.getPosition().around().stream())
                .toList();
    }

    @Override
    public void generateFromPreferablePosition(int width, int height) {
        int preferableGrassPlaces = (int) (0.2 * width * height);
        List<Vector2d> possiblePositions = getNearBodyPositions(getDeadAnimals());
        if (preferableGrassPlaces < possiblePositions.size()) {
            Collections.shuffle(possiblePositions);
            grasses.put(possiblePositions.getFirst(), new Grass(possiblePositions.getFirst(), energyGrass));
        }
    }
}
