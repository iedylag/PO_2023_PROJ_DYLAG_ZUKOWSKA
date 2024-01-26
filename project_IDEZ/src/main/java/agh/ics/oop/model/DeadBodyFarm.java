package agh.ics.oop.model;

import java.util.List;
import java.util.stream.Collectors;


public class DeadBodyFarm extends WorldMap {

    public DeadBodyFarm(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength, int minMutation, int maxMutation) {
        super(grassCount, height, width, energyGrass, startingEnergyAnimal, reproduceEnergyLevel, genomeLength, minMutation, maxMutation);
    }

    public List<Vector2d> getPreferablePositions() {
        return getDeadAnimals().keySet().stream()
                .flatMap(deadAnimalPosition -> deadAnimalPosition.around().stream())
                .distinct()
                .filter(position -> !isOccupiedByPlant(position))
                .collect(Collectors.toList());
    }

    @Override
    public void generateFromOtherPositions(int count) {
        List<Vector2d> otherPositions = allPositions.stream()
                .filter(pos -> !isOccupiedByPlant(pos))
                .filter(pos -> !getPreferablePositions().contains(pos))
                .collect(Collectors.toList());

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
        if (!getDeadAnimals().isEmpty()) {
            List<Vector2d> possiblePositions = getPreferablePositions();
            if (!possiblePositions.isEmpty()) {
                int howManyIsPossible = possiblePositions.size();
                if (howManyIsPossible > count) {
                    howManyIsPossible = count;
                }

                RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(possiblePositions, howManyIsPossible);
                for (Vector2d position : randomPositionGenerator) {
                    grasses.put(position, new Grass(position));
                }
            }
        }
    }
}
