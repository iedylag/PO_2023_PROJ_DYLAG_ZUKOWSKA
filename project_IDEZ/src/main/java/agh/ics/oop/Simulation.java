package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.List;

public class Simulation implements Runnable {
    private final List<Rotation> directions;
    private final WorldMap map;

    public Simulation(List<Rotation> directions, int animalCount, WorldMap map) {
        this.directions = directions;
        this.map = map;

        map.place(animalCount);
    }

    @Override
    public void run() {
        List<Animal> animals = map.getAnimals();

        for (int i = 0; i < directions.size(); i++) {
            Animal animal = animals.get(i % animals.size());
            map.move(animal, directions.get(i));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}