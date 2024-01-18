package agh.ics.oop.model;

import java.util.*;

public class WorldMap implements MoveValidator {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Map<Vector2d, Animal> deadAnimals = new HashMap<>();
    private final Set<MapChangeListener> observers = new HashSet<>(); //lista obserwatorów
    private final int height;
    private final int width;
    private final int energyGrass;

    public int getStartingEnergyAnimal() {
        return startingEnergyAnimal;
    }

    private final int startingEnergyAnimal;
    private int deadAnimalsCounter = 0;
    private final int reproduceEnergyLevel;

    //private boolean deadBodyFarmActivated;

    private final UUID mapId = UUID.randomUUID();

    public WorldMap(int grassCount, int width, int height, int energyGrass, int startingEnergyAnimal, int reproduceEnergyLevel) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount, width, height);
        this.energyGrass = energyGrass;
        this.startingEnergyAnimal = startingEnergyAnimal ;
        this.reproduceEnergyLevel = reproduceEnergyLevel;
        this.width = width-1;
        this.height = height-1;
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

    private void grassFieldGenerate(int grassCount, int width, int height) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, 0, height, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
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

    public List<Animal> getAnimals() {
        return List.copyOf(animals.values());
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
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void generateFromPreferablePosition(int width, int height) {
        int preferableGrassPlaces = (int) (0.2 * width * height);
        int equatorHeight = 1;
        while (preferableGrassPlaces > width * equatorHeight) {
            RandomPositionGenerator positionGenerator = new RandomPositionGenerator(width, height / 2 - equatorHeight, height / 2 + equatorHeight, 1);
            for (Vector2d grassPosition : positionGenerator) {
                grasses.put(grassPosition, new Grass(grassPosition));
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

    public void move(Animal animal, Rotation direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        Vector2d newPosition = animal.getPosition();

        if (!Objects.equals(oldPosition, newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged("Animal moved to " + newPosition + " and is heading " + animal.getOrientation());
        } else {
            mapChanged("Animal remains in position, but heads " + animal.getOrientation());
        }
    }

    public void animalOnTheEdge(Vector2d position, MapDirection orientation) {
        Animal animal = animals.get(position);
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
            animals.put(animalPosition, new Animal(animalPosition, startingEnergyAnimal));
        }
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(animals.values());
        elements.addAll(grasses.values());
        return elements;
    }

    public int getGrassesSize() {
        return grasses.size();
    }

    public Optional<WorldElement> objectAt(Vector2d position) {
        Optional<WorldElement> animal = Optional.ofNullable(animals.get(position));
        return animal.or(() -> Optional.ofNullable(grasses.get(position)));
    }

    @Override
    public UUID getId() {
        return mapId;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows() && position.precedes(upperRight);
    }

    public void removeIfDead() {
        for (Animal animal : getAnimals()) {
            if (animal.getEnergy() == 0) {
                Vector2d position = animal.getPosition();
                deadAnimalsCounter++;
                deadAnimals.put(position, animal);
                animals.remove(position, animal);
            }
        }
    }

    public Collection<Grass> getGrass() {
        return grasses.values();
    }

    public void eatSomeGrass() {
        for (Animal currentAnimal : getAnimals()) {
            for (Grass grass : getGrass()) {

                if (currentAnimal.getPosition().equals(grass.getPosition())) {
                    currentAnimal.eat(grass);
                    grasses.remove(grass.getPosition());
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

    public Animal childOf(Animal mom, Animal dad) {

        if (canReproduce(mom, dad)) {
            int totalEnergy = mom.getEnergy() + dad.getEnergy();
            int genomeRatio = mom.getEnergy() / totalEnergy * Genome.GENOME_LENGTH;
            Genome childGenome = mom.getGenome().crossover(genomeRatio, getAlphaAnimal(mom, dad));

            childGenome.mutate1(); //uzytkownik wybiera to lub mutate2
            mom.setEnergyLevel(mom.getEnergy() - reproduceEnergyLevel);
            dad.setEnergyLevel(dad.getEnergy() - reproduceEnergyLevel);

            return new Animal(mom, dad, childGenome);
        }
        return null;
    }

    public void animalsReproduction() {
        Map<Vector2d, List<Animal>> animalsByPosition = new HashMap<>();

        // Grupowanie zwierząt po pozycji
        for (Animal animal : animals.values()) {
            animalsByPosition.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        }

        // Sprawdzenie, czy są dwie lub więcej zwierzęta na tej samej pozycji
        for (List<Animal> animalsAtPosition : animalsByPosition.values()) {
            if (animalsAtPosition.size() >= 2) {
                Animal child = childOf(animalsAtPosition.get(1), animalsAtPosition.get(2));
                child.setEnergyLevel( 2 * reproduceEnergyLevel);
                animals.put(child.getPosition(), child);
            }
        }
    }

}
