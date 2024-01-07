package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrassField extends AbstractWorldMap {
    public static final Vector2d LOWER_LEFT = new Vector2d(0, 0);
    private final Vector2d upperRight;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(int grassCount,int height, int width) {
        upperRight = new Vector2d(width - 1, height - 1);
        grassFieldGenerate(grassCount);
    }

    private void grassFieldGenerate(int grassCount) {
        Vector2d grassBoundary = new Vector2d((int) Math.sqrt(grassCount * 10), (int) Math.sqrt(grassCount * 10));
        int maxWidth = grassBoundary.getX();
        int maxHeight = grassBoundary.getY();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxWidth, maxHeight, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(LOWER_LEFT, upperRight);
    }

    @Override
    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(super.getElements());
        elements.addAll(grasses.values());
        return elements;
    }

    public int getGrassesSize() {
        return grasses.size();
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement element = super.objectAt(position);
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
        return position.follows(LOWER_LEFT) && position.precedes(upperRight) && !isOccupied(position);
    }
}
