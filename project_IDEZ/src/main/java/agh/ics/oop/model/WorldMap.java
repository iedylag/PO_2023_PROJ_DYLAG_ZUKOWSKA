package agh.ics.oop.model;

import java.util.*;

public class WorldMap implements MoveValidator {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Set<MapChangeListener> observers = new HashSet<>(); //lista obserwatorów
    private final UUID mapId = UUID.randomUUID();

    public WorldMap(int grassCount, int height, int width) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount);
    }

    /*
    potrzebujemy jeszczez tutaj metody:
    1. zliczającej zwierzaki na mapie
    2. zliczającej rośliny na mapie
    3. zliczającej wolne pola na mapie
    4. wybieranie najpopularniejszego genomu
    5. liczenie średniej energii
    6. liczenie średniej dł życia
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

    public void animalOnTheEdge(Vector2d newPosition, MapDirection orientation){
        if (newPosition.getX() < LOWER_LEFT.getX() || newPosition.getX() > upperRight.getX()){
            newPosition.opposite(LOWER_LEFT, upperRight);
        }
        if (newPosition.getY() < LOWER_LEFT.getY() || newPosition.getY() > upperRight.getY()){
            orientation.opposite();
        }
    }

    @Override
    public String toString() {
        MapVisualizer visualizer = new MapVisualizer(this);
        return visualizer.draw(LOWER_LEFT,upperRight);
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


    public WorldElement objectAt(Vector2d position) {
        WorldElement element = animals.get(position);
        if (element != null) {
            return element;
        }
        return grasses.get(position);
    }

    //TO DO POPRAWY
    //kula ziemska - lewa i prawa krawędź mapy zapętlają się (jeżeli zwierzak wyjdzie za lewą krawędź,
    //to pojawi się po prawej stronie - a jeżeli za prawą, to po lewej); górna i dolna krawędź mapy to bieguny -
    // nie można tam wejść (jeżeli zwierzak próbuje wyjść poza te krawędzie mapy, to pozostaje na polu na którym był,
    // a jego kierunek zmienia się na odwrotny (FUNKCJA OPPOSITE Z VECTOR2D));

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(LOWER_LEFT) && position.precedes(upperRight);
    }

    public void removeIfDead() {
        Collection<WorldElement> elements = getElements();
        elements.removeIf(element -> element.getEnergy() == 0);
    }

    public Collection<Grass> getGrass() {
        return grasses.values();
    }
}
