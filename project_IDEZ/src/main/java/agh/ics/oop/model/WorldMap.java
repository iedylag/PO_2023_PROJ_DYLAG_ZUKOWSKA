package agh.ics.oop.model;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldMap implements MoveValidator {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, List<Animal>> deadAnimals = new HashMap<>();
    private final BlockingDeque<MapChangeListener> observers = new LinkedBlockingDeque<>(); //lista obserwatorów
    private final int height;
    private final int width;
    private final int energyGrass;
    private final UUID mapId = UUID.randomUUID();
    private final Map<List<Integer>, Integer> genotypeOccurrences = new HashMap<>();
    private final int startingEnergyAnimal;
    private int deadAnimalsCounter = 0;
    private final int reproduceEnergyLevel;
    private final int genomeLength;
    private boolean mutationVariantActivated = false;
    private final int minMutation;
    private final int maxMutation;
    protected final Set<Vector2d> allPositions = new HashSet<>();
    private final Set<Vector2d> equatorPositions = new HashSet<>();
    private Set<Vector2d> notEquatorPositions = new HashSet<>();

    public WorldMap(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength, int minMutation, int maxMutation) {
        upperRight = new Vector2d(width - 1, height - 1);
        this.energyGrass = energyGrass;
        this.startingEnergyAnimal = startingEnergyAnimal;
        this.reproduceEnergyLevel = reproduceEnergyLevel;
        this.width = width;
        this.genomeLength = genomeLength;
        this.height = height;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        grassFieldGenerate(grassCount, height, width);
        generateAllPositions();
        generateEquator();
    }

    private void grassFieldGenerate(int grassCount, int height, int width) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void subscribe(MapChangeListener observer) {  //rejestrowanie obserwatora
        observers.add(observer);
    }

    public void unsubscribe(MapChangeListener observer) {  //wyrejestrowanie obserwatora
        observers.remove(observer);
    }

    private void mapChanged(String message) {
        observers.forEach(observer -> observer.mapChanged(this, message));
    }


    public void newGrassGenerator(int grassCount) {
        int preferCount = 0;
        int otherCount = 0;
        for (int i = 0; i < grassCount; i++) {
            if (Math.random() < 0.8) {
                preferCount++;
            } else {
                otherCount++;
            }
        }

        generateFromPreferablePositions(preferCount);
        generateFromOtherPositions(otherCount);
    }

    public void generateFromPreferablePositions(int count) {
        List<Vector2d> preferablePositions = equatorPositions.stream()
                .filter(pos -> !isOccupiedByPlant(pos))
                .collect(Collectors.toList());

        if (preferablePositions.size() < count) {
            count = preferablePositions.size();
        }

        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(preferablePositions, count);
        for (Vector2d grassPosition : positionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void generateFromOtherPositions(int count) {
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(notEquatorPositions.stream()
                .filter(pos -> !isOccupiedByPlant(pos))
                .collect(Collectors.toList()), count);
        for (Vector2d grassPosition : positionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void generateAllPositions() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }
    }

    private void generateEquator() {
        int equatorLength = (int) (0.2 * (width * height));
        int startX = width / 2;
        int startY = height / 2;

        if (width >= equatorLength) {
            int startXStart = startX - equatorLength / 2;
            int startXEnd = startX + equatorLength / 2;

            for (int x = startXStart; x < startXEnd; x++) {
                equatorPositions.add(new Vector2d(x, startY));
            }
        } else {
            int numberOfPasses = equatorLength / width;

            int startYStart = startY - (numberOfPasses / 2);
            int startYEnd = startY + (numberOfPasses / 2) + 1;

            for (int x = 0; x < width; x++) {
                for (int y = startYStart; y < startYEnd; y++) {
                    equatorPositions.add(new Vector2d(x, y));
                }
            }
        }

        notEquatorPositions = allPositions.stream()
                .filter(pos -> !equatorPositions.contains(pos))
                .collect(Collectors.toSet());
    }

    public void move(Animal animal, Rotation direction) {
        Vector2d oldPosition = animal.position();
        animal.move(direction, this);
        Vector2d newPosition = animal.position();

        if (!Objects.equals(oldPosition, newPosition)) {
            if (!isOccupiedByAnimal(newPosition)) {
                animals.put(newPosition, new ArrayList<>());
            }
            animals.get(newPosition).add(animal);
            animals.get(oldPosition).remove(animal);

            mapChanged("Animal moved to " + newPosition + " and is heading " + animal.getOrientation());
        } else {
            mapChanged("Animal remains in position, but heads " + animal.getOrientation());
        }
    }

    public void animalOnTheEdge(Animal animal, Vector2d position, MapDirection orientation) {
        if (position.x() == LOWER_LEFT.x() || position.x() == upperRight.x()) {
            animal.setPosition(position.oppositeX(LOWER_LEFT, upperRight));
            mapChanged("Animal moved");
        }
        if (position.y() == LOWER_LEFT.y() || position.y() == upperRight.y()) {
            animal.setOrientation(orientation.opposite());
        }
    }

    public void place(int animalCount) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, animalCount);
        for (Vector2d animalPosition : randomPositionGenerator) {
            Animal newAnimal = new Animal(animalPosition, startingEnergyAnimal, genomeLength);
            if (isOccupiedByAnimal(animalPosition)) {
                List<Animal> animalsList = animals.get(animalPosition);
                animalsList.add(newAnimal);
            } else {
                animals.put(animalPosition, new ArrayList<>());
                animals.get(animalPosition).add(newAnimal);
            }
            addToGenotypeMap(newAnimal.getGenome().getGenes());
        }
        mapChanged("Animals were placed");
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows() && position.precedes(upperRight);
    }

    public void removeIfDead() {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>(getAnimals());
        for (Map.Entry<Vector2d, List<Animal>> entry : animalsCopy.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animalsAtPosition = recentlyDeadAnimals(entry, position);

            if (animalsAtPosition.isEmpty()) {
                animals.remove(position);
            } else {
                animals.put(position, animalsAtPosition);
            }
        }
    }

    private List<Animal> recentlyDeadAnimals(Map.Entry<Vector2d, List<Animal>> entry, Vector2d position) {
        List<Animal> animalsAtPosition = new ArrayList<>(entry.getValue());

        animalsAtPosition.removeIf(animal -> {
            if (animal.getEnergy() <= 0) {
                deadAnimalsCounter++;

                removeFromGenotypeMap(animal.getGenome().getGenes());

                if (!deadAnimals.containsKey(position)) {
                    deadAnimals.put(position, new ArrayList<>());
                }
                deadAnimals.get(position).add(animal);

                return true;
            }
            return false;
        });
        return animalsAtPosition;
    }

    public void eatSomeGrass() {
        for (Vector2d position : animals.keySet()) {
            if (isOccupiedByPlant(position)) {
                Optional<Animal> animal = getStrongestAnimalAt(position);
                animal.get().eat(energyGrass);
                grasses.remove(position);
                mapChanged("Grass had been eaten");
            }
        }
    }

    private boolean canReproduce(Animal mom, Animal dad) {
        return mom.getEnergy() >= reproduceEnergyLevel && dad.getEnergy() >= reproduceEnergyLevel;
    }

    private List<Animal> getAlphaAnimal(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return List.of(animal1, animal2);
        }
        return List.of(animal2, animal1);
    }

    public Optional<Animal> getStrongestAnimalAt(Vector2d position) {
        List<Animal> animalsAtPosition = animals.get(position);

        return animalsAtPosition.stream()
                .max(Comparator.comparingInt(Animal::getEnergy));
    }

    public Optional<Animal> childOf(Animal mom, Animal dad) {
        if (canReproduce(mom, dad)) {
            int totalEnergy = mom.getEnergy() + dad.getEnergy();
            int genomeRatio = mom.getEnergy() / totalEnergy * genomeLength;
            Genome childGenome = mom.getGenome().crossover(genomeRatio, getAlphaAnimal(mom, dad));

            if (mutationVariantActivated) {
                childGenome.mutate2(minMutation, maxMutation); //lekka korekta
            } else {
                childGenome.mutate1(minMutation, maxMutation); //obowiazkowy wariant
            }

            mom.setEnergyLevel(mom.getEnergy() - reproduceEnergyLevel);
            dad.setEnergyLevel(dad.getEnergy() - reproduceEnergyLevel);
            addToGenotypeMap(childGenome.getGenes());
            return Optional.of(new Animal(mom, dad, childGenome));
        }
        return Optional.empty();
    }

    public void animalsReproduction() {
        for (Vector2d position : animals.keySet()) {
            if (isOccupiedByAnimals(position)) {
                List<Animal> animalsAtPosition = animals.get(position);
                childOf(animalsAtPosition.get(0), animalsAtPosition.get(1)).ifPresent(child -> {
                    child.setEnergyLevel(reproduceEnergyLevel * 2);
                    animals.get(position).add(child);
                    mapChanged("Animals made a baby");
                });
            }
        }
    }

    public boolean isOccupiedByAnimal(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean isOccupiedByAnimals(Vector2d position) {
        return animals.get(position).size() > 1;
    }

    public boolean isOccupiedByPlant(Vector2d position) {
        return this.grasses.containsKey(position);
    }


    public Optional<WorldElement> objectAt(Vector2d position) {
        List<Animal> animalList = animals.get(position);
        if (animalList != null && !animalList.isEmpty()) {
            return Optional.of(animalList.getFirst());
        }
        return Optional.ofNullable(grasses.get(position));
    }

    public int emptyPositionsNumber() {
        int number;
        int freePositions;

        List<Vector2d> animalsVectors = new ArrayList<>(animals.keySet());
        List<Vector2d> plantsVectors = new ArrayList<>(grasses.keySet());

        List<Vector2d> uniqVectors = Stream.concat(animalsVectors.stream(), plantsVectors.stream())
                .distinct()
                .toList();

        number = uniqVectors.size();

        freePositions = (width * height) - number;

        return freePositions;
    }

    public int howManyAnimalsDied() {
        return deadAnimalsCounter;
    }

    public OptionalDouble averageAnimalChildren() {
        return allAnimalsThatHaveEverLivedOnThisMap().stream()
                .mapToInt(Animal::getChildrenNumber)
                .average();
    }

    public List<Animal> allAnimalsThatHaveEverLivedOnThisMap() {
        Map<Vector2d, List<Animal>> aliveAnimals = new HashMap<>(getAnimals());
        Map<Vector2d, List<Animal>> deadAnimals = new HashMap<>(getDeadAnimals());
        return Stream.concat(aliveAnimals.values().stream().flatMap(List::stream),
                deadAnimals.values().stream().flatMap(List::stream)).toList();
    }

    public OptionalDouble averageLifetime() {
        return allAnimalsThatHaveEverLivedOnThisMap().stream()
                .mapToInt(Animal::getLifetime)
                .average();
    }

    public OptionalDouble averageAnimalEnergy() {
        return getAnimals().values().stream()
                .flatMap(List::stream)
                .mapToInt(Animal::getEnergy)
                .average();
    }

    public void addToGenotypeMap(List<Integer> genes) {
        if (genotypeOccurrences.containsKey(genes))
            genotypeOccurrences.replace(genes, genotypeOccurrences.get(genes) + 1);
        else
            genotypeOccurrences.put(genes, 1);
    }

    public void removeFromGenotypeMap(List<Integer> genes) {
        genotypeOccurrences.replace(genes, genotypeOccurrences.get(genes) - 1);
        if (genotypeOccurrences.get(genes) == 0)
            genotypeOccurrences.remove(genes);
    }

    public List<Integer> getTheMostFrequentGenotype() {
        List<Integer> popularGenotype = new ArrayList<>();
        int maxOccurrences = 0;

        for (List<Integer> genotype : List.copyOf(genotypeOccurrences.keySet())) {
            if (genotypeOccurrences.get(genotype) > maxOccurrences) {
                maxOccurrences = genotypeOccurrences.get(genotype);
                popularGenotype = genotype;
            }
        }
        return popularGenotype;
    }

    @Override
    public UUID getId() {
        return mapId;
    }

    public Map<Vector2d, List<Animal>> getAnimals() {
        return Map.copyOf(animals);
    }

    public void setAnimals(Map<Vector2d, List<Animal>> updatedAnimals) {
        this.animals = animals;
    }

    public Map<Vector2d, List<Animal>> getDeadAnimals() {
        return Map.copyOf(deadAnimals);
    }

    public void setMutationVariantActivated(boolean mutationVariantActivated) {
        this.mutationVariantActivated = mutationVariantActivated;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public int getGrassCount() {
        return grasses.size();
    }

    public int getAnimalCount() {
        return animals.size();
    }

    public int getStartingEnergyAnimal() {
        return startingEnergyAnimal;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(LOWER_LEFT, upperRight);
    }
}
