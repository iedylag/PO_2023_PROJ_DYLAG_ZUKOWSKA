package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class EquatorJungle extends WorldMap {
    private final List<Vector2d> mapEquator = new ArrayList<>(); //pozycje rownika

    public EquatorJungle(int grassCount, int height, int width) {
        super(grassCount, height, width);
        grassFieldEquatorGenerate(grassCount, height, width);
    }


    private List<Vector2d> getMapEquator() { //metoda zwracajaca pozycje rownika (zawsze caly jeden srodkowy pasek)
        int equatorY = getUpperRight().getY() / 2;

        for (int i = 0; i < getUpperRight().getX(); i++) {
            mapEquator.add(new Vector2d(i, equatorY));
        }
        return mapEquator;
    }

    private void grassFieldEquatorGenerate(int grassCount, int height, int width) {

        if (Math.random() < 0.8) {
            //wylosuj pozycje z rownika  (w sumie ma byc zawsze 80% wszystkich roslin)
        } else {
            //wylosuj pozycje spoza rownika (w sumie ma byc zawsze 20% wszystkich roslin)
        }

        //tutaj jest fragment tego starego generatora, moze sie przydac
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, 0, height, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }


}
