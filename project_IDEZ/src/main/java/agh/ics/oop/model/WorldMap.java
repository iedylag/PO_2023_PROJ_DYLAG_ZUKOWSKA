package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldMap implements MoveValidator {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Animal> deadAnimals = new HashMap<>();
    private final Set<MapChangeListener> observers = new HashSet<>(); //lista obserwatorów
    private final int height;
    private final int width;
    private final int energyGrass;
    private final int startingEnergyAnimal;
    private int deadAnimalsCounter = 0;
    private final int reproduceEnergyLevel;
    private final int genomeLength;

    //private boolean deadBodyFarmActivated;

    public WorldMap(int grassCount, int height, int width, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel, int genomeLength) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount, height, width, energyGrass);
        this.energyGrass = energyGrass;
        this.startingEnergyAnimal = startingEnergyAnimal ;
        this.reproduceEnergyLevel = reproduceEnergyLevel;
        this.height = height-1;
        this.width = width-1;
        this.genomeLength = genomeLength;
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

    public List<Animal> getDeadAnimals() {
        return List.copyOf(deadAnimals.values());
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
/*
    public List<Animal> getAnimals() {
        return List.copyOf(animals.values());
    }

 */public List<Animal> getAnimals() {
    return animals.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
}

    public void newGrassGenerator(int grassCount) {
        for (int i = 0; i < grassCount; i++) {
            if (Math.random() < 0.8) {
                generateFromPreferablePosition(height, width);
            } else {
                generateFromOtherPosition(height, width);
            }
        }
    }

    public void generateFromOtherPosition(int height, int width) {
        //na razie losuje ze wszystkich
        //int otherGrassPlaces = (int) (0.8 * width * height);
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(width, 0, height, 1);
        for (Vector2d grassPosition : positionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition, energyGrass));
        }
    }

    public void generateFromPreferablePosition(int height, int width) {
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
/*
chyba niepotrzebne

    private List<Vector2d> getMapEquator() { //metoda zwracajaca pozycje rownika (zawsze caly jeden srodkowy pasek)
        int equatorY = height / 2;

        for (int i = 0; i < getUpperRight().getX(); i++) {
            mapEquator.add(new Vector2d(i, equatorY));
        }
        return mapEquator;
    }

 */

    public int howManyAnimals() {
        return animals.size();
    }

    public int howManyAnimalsDied(){
        return deadAnimalsCounter;
    }
/*
    public OptionalDouble averageLifeTime() {
        return getAnimals().stream()
                .mapToInt(Animal::getLifetime)
                .average();
    }

    public OptionalDouble averageAnimalEnergy() {
        return getAnimals().stream()
                .mapToInt(Animal::getEnergy)
                .average();
    }

 */
/*
    public int howManyGrass() {
        return grasses.size();
    }

    public int emptyFields() {

        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Optional<WorldElement> object = this.objectAt(new Vector2d(x, y));
                if (object.isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

 */

    public void move(Animal animal, Rotation direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        Vector2d newPosition = animal.getPosition();

        if (!Objects.equals(oldPosition, newPosition)) {
            if (isOccupiedByAnimal(newPosition)) {
            List<Animal> animalsList = animals.get(newPosition);
            animalsList.add(animal);
        } else {
            animals.put(newPosition, new ArrayList<>());
            animals.get(newPosition).add(animal);
        }
            animals.get(oldPosition).remove(animal);
            animals.get(newPosition).add(animal);
            mapChanged("Animal moved to " + newPosition + " and is heading " + animal.getOrientation());
        } else {
            mapChanged("Animal remains in position, but heads " + animal.getOrientation());
        }

    }

    public void animalOnTheEdge(Animal animal, Vector2d position, MapDirection orientation) {
        if (position.getX() == LOWER_LEFT.getX() || position.getX() == upperRight.getX()) {
            animal.setPosition(position.opposite(LOWER_LEFT, upperRight));
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
        }
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

    public int getGrassesSize() {
        return grasses.size();
    }
    public int getGenomeLength() {
        return genomeLength;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows() && position.precedes(upperRight);
    }

    public void removeIfDead() {
        for (Vector2d position: animals.keySet() ) {
            List<Animal> animalsAtPosition = animals.get(position);
            for (Animal animal : animalsAtPosition) {
                if (animal.getEnergy() == 0) {
                    deadAnimalsCounter++;
                    deadAnimals.put(position, animal);
                    animals.get(position).remove(animal);
                }
            }
        }
    }

    public Collection<Grass> getGrass() {
        return grasses.values();
    }

    public void eatSomeGrass() {
        for (Vector2d position: animals.keySet() ) {
            if (isOccupiedByPlant(position)) {
                Animal animal = getStrongestAnimalAt(position);
                for (Grass grass : getGrass()) {
                    if (position.equals(grass.getPosition())) {
                        animal.eat(grass);
                        grasses.remove(position);
                    }
                }
            }
        }
    }

    private boolean canReproduce(Animal mom, Animal dad) {
        return mom.getEnergy() > reproduceEnergyLevel && dad.getEnergy() > reproduceEnergyLevel;
    }
    private List<Animal> getAlphaAnimal(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return List.of(animal1, animal2);
        }
        return List.of(animal2, animal1);
    }
    public Animal getStrongestAnimalAt(Vector2d position) {
        List<Animal> animalsAtPosition = animals.get(position);

        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
            return animalsAtPosition.stream()
                    .max(Comparator.comparingInt(Animal::getEnergy))
                    .orElse(null);
        }

        return null;
    }
    public Animal childOf(Animal mom, Animal dad) {

        if (canReproduce(mom, dad)) {
            int totalEnergy = mom.getEnergy() + dad.getEnergy();
            int genomeRatio = mom.getEnergy() / totalEnergy * genomeLength;
            Genome childGenome = mom.getGenome().crossover(genomeRatio, getAlphaAnimal(mom, dad));

            childGenome.mutate1(); //uzytkownik wybiera to lub mutate2
            mom.setEnergyLevel(mom.getEnergy() - reproduceEnergyLevel);
            dad.setEnergyLevel(dad.getEnergy() - reproduceEnergyLevel);

            return new Animal(mom, dad, childGenome);
        }
        return null;
    }

    public void animalsReproduction() {
        for (List<Animal> animalList : animals.values()) {
            if (animalList.size() >= 2) {
                Animal child = childOf(animalList.get(1), animalList.get(2));
                child.setEnergyLevel( 2 * reproduceEnergyLevel);
                animalList.add(child);
            }
        }
    }


    public boolean isOccupiedByAnimal(Vector2d position) {
        return this.animals.containsKey(position);
    }
    public boolean isOccupiedByAnimals(Vector2d position) {
        return animals.get(position).size() > 1;
    }

    public boolean isOccupiedByPlant(Vector2d position) {
        return this.grasses.containsKey(position);
    }
    public List<Animal> animalsAt(Vector2d position) {
        if (this.isOccupiedByAnimal(position)) {
            return this.animals.get(position);
        }
        return null;
    }
    public Optional<WorldElement> objectAt(Vector2d position) {
        if (this.isOccupiedByAnimal(position)) {
            List<Animal> animalsList = animals.get(position);
            return Optional.ofNullable(animalsList.get(0));
        } else if (this.isOccupiedByPlant(position)) {
            return Optional.ofNullable(grasses.get(position));
        } else {
            return Optional.empty();
        }
    }
    public int freePositionsNumber() {
        int number;
        int freePositions;

        Set<Vector2d> animalsKeys = animals.keySet();
        List<Vector2d> animalsVectors = new ArrayList<>(animalsKeys);

        Set<Vector2d> plantsKeys = grasses.keySet();
        List<Vector2d> plantsVectors = new ArrayList<>(plantsKeys);

        List<Vector2d> uniqVectors = Stream.concat(animalsVectors.stream(), plantsVectors.stream())
                .distinct()
                .toList();
        number = uniqVectors.size();

        freePositions = (this.width * this.height) - number;

        return freePositions;
    }

}
