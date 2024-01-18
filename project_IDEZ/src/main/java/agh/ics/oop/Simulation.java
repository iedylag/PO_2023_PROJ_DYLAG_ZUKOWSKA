package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;

public class Simulation implements Runnable {
    private final int dailyGrowth;
    private final int grassVariant;
    private int currentDay = 0;

    //private int averageLifetime = 0;
    private final WorldMap map;

    public Simulation(int animalCount, WorldMap map, int dailyGrowth, int grassVariant) {

        this.map = map;
        this.dailyGrowth = dailyGrowth;
        this.grassVariant = grassVariant;
        map.place(animalCount);
    }


    @Override
    public void run() {
        moveEachAnimal();
        System.out.println("ruszyly sie ");
        map.removeIfDead();
        map.eatSomeGrass();
        map.animalsReproduction();
        growMoreGrass();
        currentDay++;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void growMoreGrass() {
        map.newGrassGenerator(dailyGrowth);
    }

    public void moveEachAnimal() {
        Map<Vector2d, List<Animal>> animals = map.getAnimals();
        List<Vector2d> positions = List.copyOf(animals.keySet());
        for (Vector2d position: positions) {
            List<Animal> animalsAtPosition = List.copyOf(map.animalsAt(position));
            for (Animal animal : animalsAtPosition) {
                Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / map.getGenomeLength());
                map.move(animal, direction);
            }
        }
    }

}

