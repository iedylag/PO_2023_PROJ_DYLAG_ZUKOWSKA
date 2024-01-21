package agh.ics.oop.model;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
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

    public int getStartingEnergyAnimal() {
        return startingEnergyAnimal;
    }

    private final int startingEnergyAnimal;
    private int deadAnimalsCounter = 0;
    private final int reproduceEnergyLevel;
    private final int genomeLength;

    //private boolean deadBodyFarmActivated;


    public WorldMap(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount, height, width, energyGrass);
        this.energyGrass = energyGrass;
        this.startingEnergyAnimal = startingEnergyAnimal;
        this.reproduceEnergyLevel = reproduceEnergyLevel;
        this.width = width - 1;
        this.genomeLength = genomeLength;
        this.height = height - 1;

    }

    @Override
    public UUID getId() {
        return mapId;
    }

    /*
    public void setParameters(int energyGrass, int startingEnergyAnimal) {
        for (Grass grass: grasses.values()) {
            grass.setEnergyLevel(energyGrass);
        }
        for (Animal animal: animals.values()) {
            animal.setEnergyLevel(startingEnergyAnimal);
        }
    }

     */

    private void grassFieldGenerate(int grassCount, int height, int width, int energyGrass) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, 0, height, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition, energyGrass));
        }
    }

    /*
    potrzebujemy jeszczez tutaj metody:
    4. wybieranie najpopularniejszego genomu
    7. liczenie średniej liczby dzieci -> potrzbujemy jakiejś metody getChildren w Animal
     */

    public Map<Vector2d, List<Animal>> getDeadAnimals() {
        return Map.copyOf(deadAnimals);
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

    public Map<Vector2d, List<Animal>> getAnimals() {
        return Map.copyOf(animals);
    }

    public void newGrassGenerator(int grassCount) {
        for (int i = 0; i < grassCount; i++) {
            if (Math.random() < 0.8) {
                generateFromPreferablePosition(width, height);
            } else {
                generateFromOtherPosition(width, height);
            }
        }
    }

    public void generateFromOtherPosition(int width, int height) {
        //na razie losuje ze wszystkich
        //int otherGrassPlaces = (int) (0.8 * width * height);
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(width, 0, height, 1);
        for (Vector2d grassPosition : positionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition, energyGrass));
        }
    }

    public void generateFromPreferablePosition(int width, int height) {
        int preferableGrassPlaces = (int) (0.2 * width * height);
        int equatorHeight = 1;
        while (preferableGrassPlaces > width * equatorHeight) {
            RandomPositionGenerator positionGenerator = new RandomPositionGenerator(width, height / 2 - equatorHeight, height / 2 + equatorHeight, 1);
            for (Vector2d grassPosition : positionGenerator) {
                grasses.put(grassPosition, new Grass(grassPosition, energyGrass));
            }
            equatorHeight++;
        }
    }

    public int howManyAnimalsDied() {
        return deadAnimalsCounter;
    }

    public OptionalDouble averageAnimalChildren() {
        return getAnimals().values().stream()
                .flatMap(List::stream)
                .mapToInt(Animal::getChildrenNumber)
                .average();
    }

    public List<Animal> allAnimalsThatHaveEverLivedOnThisMap(){
        Map<Vector2d, List<Animal>> aliveAnimals = new HashMap<>(getAnimals());
        Map<Vector2d, List<Animal>> deadAnimals = new HashMap<>(getDeadAnimals());
        return Stream.concat(aliveAnimals.values().stream().flatMap(List::stream),
                             deadAnimals.values().stream().flatMap(List::stream)).toList();
    }
    public OptionalDouble averageLifeTime() {
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

    public void move(Animal animal, Rotation direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        Vector2d newPosition = animal.getPosition();

        if (!Objects.equals(oldPosition, newPosition)) {
            if (isOccupiedByAnimal(newPosition)) {
                animals.get(newPosition).add(animal);
            } else {
                animals.put(newPosition, new ArrayList<>());
                animals.get(newPosition).add(animal);
            }
            animals.get(oldPosition).remove(animal);

            mapChanged("Animal moved to " + newPosition + " and is heading " + animal.getOrientation());
        } else {
            mapChanged("Animal remains in position, but heads " + animal.getOrientation());
        }
    }

    public void removeEmptyPositions(){
        List<Vector2d> positionsToRemove = new ArrayList<>();

        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            if (entry.getValue().isEmpty()) {
                positionsToRemove.add(entry.getKey());
            }
        }
        for (Vector2d position : positionsToRemove) {
            animals.remove(position);
        }
    }

    public void animalOnTheEdge(Animal animal, Vector2d position, MapDirection orientation) {
        if (position.getX() == LOWER_LEFT.getX() || position.getX() == upperRight.getX()) {
            animal.setPosition(position.opposite(LOWER_LEFT, upperRight));
            mapChanged("Animal moved");
        }
        if (position.getY() == LOWER_LEFT.getY() || position.getY() == upperRight.getY()) {
            animal.setOrientation(orientation.opposite());
        }
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(LOWER_LEFT, upperRight);
    }


    public void place(int animalCount) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, 0, height, animalCount);
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


    public Vector2d getUpperRight() {
        return upperRight;
    }
/*
    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(animals.values());
        elements.addAll(grasses.values());
        return elements;
    }

 */

    public int getGrassCount() {
        return grasses.size();
    }

    public int getAnimalCount() {
        return animals.size();
    }

    public int getGenomeLength() {
        return genomeLength;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows() && position.precedes(upperRight);
    }

    public void removeIfDead() {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>(getAnimals());
        for (Map.Entry<Vector2d, List<Animal>> entry : animalsCopy.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animalsAtPosition = new ArrayList<>(entry.getValue());

            animalsAtPosition.removeIf(animal -> {
                if (animal.getEnergy() <= 0) {
                    deadAnimalsCounter++;

                    removeFromGenotypeMap(animal.getGenome().getGenes());
                    deadAnimals.put(position, animal);

                    /*if(!deadAnimals.containsKey(position)) {
                        deadAnimals.put(position, new ArrayList<>());
                    }
                    deadAnimals.get(position).add(animal);*/
  
                    return true;
                }
                return false;
            });

            if (animalsAtPosition.isEmpty()) {
                animals.remove(position);
            } else {
                animals.put(position, animalsAtPosition);
            }
        }
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

            childGenome.mutate2(); //uzytkownik wybiera to lub mutate2

            mom.setEnergyLevel(mom.getEnergy() - reproduceEnergyLevel);
            dad.setEnergyLevel(dad.getEnergy() - reproduceEnergyLevel);
            System.out.println("mamy dziecko");
            System.out.println(new Animal(mom, dad, childGenome));
            addToGenotypeMap(childGenome.getGenes());
            return Optional.of(new Animal(mom, dad, childGenome));
        }
        return Optional.empty();
    }

    public void animalsReproduction() {
        for (Vector2d position : animals.keySet()) {
            if (isOccupiedByAnimals(position)) {
                List<Animal> animalsAtPosition = animals.get(position);
                childOf(animalsAtPosition.get(0), animalsAtPosition.get(1)).ifPresent(child -> {child.setEnergyLevel(reproduceEnergyLevel * 2);
                    animals.get(position).add(child);
                    mapChanged("Animals made a baby");});
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
        // Sprawdzenie, czy na danej pozycji znajduje się lista zwierząt
        List<Animal> animalList = animals.get(position);
        if (animalList != null && !animalList.isEmpty()) {
            // Zwrócenie pierwszego zwierzęcia z listy, jeśli lista nie jest pusta
            return Optional.of(animalList.get(0));
        }
        // Jeśli na pozycji nie ma zwierząt, sprawdzenie, czy jest tam trawa
        return Optional.ofNullable(grasses.get(position));
    }

    public void addToGenotypeMap(List<Integer> genes){
        if (genotypeOccurrences.containsKey(genes))
            genotypeOccurrences.replace(genes, genotypeOccurrences.get(genes) + 1);
        else
            genotypeOccurrences.put(genes, 1);
    }

    public void removeFromGenotypeMap(List<Integer> genes){
        genotypeOccurrences.replace(genes, genotypeOccurrences.get(genes) - 1);
        if (genotypeOccurrences.get(genes) == 0)
            genotypeOccurrences.remove(genes);
    }

    public List<Integer> getTheMostFrequentGenotype(){
        List<Integer> popularGenotype = new ArrayList<>();
        int maxOccurrences = 0;

        for (List<Integer> genotype: List.copyOf(genotypeOccurrences.keySet())){
            if (genotypeOccurrences.get(genotype) > maxOccurrences){
                maxOccurrences = genotypeOccurrences.get(genotype);
                popularGenotype = genotype;
            }
        }
        return popularGenotype;
    }
}
