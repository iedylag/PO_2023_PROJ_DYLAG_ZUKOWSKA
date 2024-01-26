package agh.ics.oop;

public class Configurations {
    private final int mapWidth;
    private final int mapHeight;
    private final int initialGrassCount;
    private final int initialAnimalCount;
    private final int grassEnergy;
    private final int animalStartingEnergy;
    private final int animalReproductionEnergy;
    private final int genomeLength;
    private final int minMutation;
    private final int maxMutation;
    private final int mutationVariant;
    private final int dailyGrassGrowth;
    private final int grassGrowthVariant;

    public Configurations(Integer widthSpinner, Integer heightSpinner, Integer initialGrassSpinner, Integer initialAnimalsSpinner, Integer energyGrassSpinner, Integer startingEnergyAnimalSpinner, Integer reproductionEnergySpinner, Integer genomeLengthSpinner, Integer minMutationsSpinner, Integer maxMutationsSpinner, Integer mutationVariantSpinner, Integer dailyGrowthSpinner, Integer grassVariantSpinner) {
        this.mapWidth = widthSpinner;
        this.mapHeight = heightSpinner;
        this.initialGrassCount = initialGrassSpinner;
        this.initialAnimalCount = initialAnimalsSpinner;
        this.grassEnergy = energyGrassSpinner;
        this.animalStartingEnergy = startingEnergyAnimalSpinner;
        this.animalReproductionEnergy = reproductionEnergySpinner;
        this.genomeLength = genomeLengthSpinner;
        this.minMutation = minMutationsSpinner;
        this.maxMutation = maxMutationsSpinner;
        this.mutationVariant = mutationVariantSpinner;
        this.dailyGrassGrowth = dailyGrowthSpinner;
        this.grassGrowthVariant = grassVariantSpinner;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialGrassCount() {
        return initialGrassCount;
    }

    public int getInitialAnimalCount() {
        return initialAnimalCount;
    }

    public int getGrassEnergy() {
        return grassEnergy;
    }

    public int getAnimalStartingEnergy() {
        return animalStartingEnergy;
    }

    public int getAnimalReproductionEnergy() {
        return animalReproductionEnergy;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public int getMinMutation() {
        return minMutation;
    }

    public int getMaxMutation() {
        return maxMutation;
    }

    public int getMutationVariant() {
        return mutationVariant;
    }

    public int getDailyGrassGrowth() {
        return dailyGrassGrowth;
    }

    public int getGrassGrowthVariant() {
        return grassGrowthVariant;
    }

}