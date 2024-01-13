package agh.ics.oop.model;

import java.util.*;

public class WorldMap implements MoveValidator {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Set<MapChangeListener> observers = new HashSet<>(); //lista obserwatorów
    private final UUID mapId = UUID.randomUUID();

    private final List<Vector2d> mapEquator = new ArrayList<>(); //pozycje rownika

    public WorldMap(int grassCount, int height, int width) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount);
    }

    /*public WorldMap(int grassCount, Vector2d upperRight) { //nie wiem czy to tak zadziala, czy sie trawa nie utworzy dwa razy
        super();
        this.upperRight = upperRight;
        grassFieldEquatorGenerate(grassCount);
    }*/

    /*
    potrzebujemy jeszczez tutaj metody:
    4. wybieranie najpopularniejszego genomu
    7. liczenie średniej liczby dzieci -> potrzbujemy jakiejś metody getChildren w Animal
     */

    public UUID getId() {
        return mapId;
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

    public int howManyAnimals() {
        return animals.size();
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
        int width = upperRight.getX();
        int height = upperRight.getY();

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
        if (position.getX() == LOWER_LEFT.getX() || position.getX() == upperRight.getX()) {
            position.opposite(LOWER_LEFT, upperRight);
        }
        if (position.getY() == LOWER_LEFT.getY() || position.getY() == upperRight.getY()) {
            orientation.opposite();
        }
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(LOWER_LEFT, upperRight);
    }

    //GENEROWANIE TRAWY CZESCIEJ PRZY ROWNIKU
    private void grassFieldGenerate(int grassCount) {
        int width = upperRight.getX();
        int height = upperRight.getY();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    private List<Vector2d> getMapEquator() { //metoda zwracajaca pozycje rownika
        int equatorY = upperRight.getY() / 2;

        for (int i = 0; i < upperRight.getX(); i++) {
            mapEquator.add(new Vector2d(i, equatorY));
        }
        return mapEquator;
    }

    private void grassFieldEquatorGenerate(int grassCount) {
        //metoda do generowania trawy na rowniku
    }

    public void place(int animalCount) {
        int width = upperRight.getX();
        int height = upperRight.getY();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, animalCount);
        for (Vector2d animalPosition : randomPositionGenerator) {
            animals.put(animalPosition, new Animal(animalPosition));
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
    public boolean canMoveTo(Vector2d position) {
        return position.follows() && position.precedes(upperRight);
    }

    public void removeIfDead() {
        Collection<WorldElement> elements = getElements();
        elements.removeIf(element -> element.getEnergy() == 0);
    }

    public Collection<Grass> getGrass() {
        return grasses.values();
    }
}
