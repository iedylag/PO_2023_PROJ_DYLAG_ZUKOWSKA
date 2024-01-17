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
        System.out.println(map);
        map.removeIfDead();
        map.eatSomeGrass();
        map.animalsReproduction();
        growMoreGrass();
        currentDay++;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void growMoreGrass() {
        map.newGrassGenerator(dailyGrowth);
    }

    public void moveEachAnimal() {

        Map<Vector2d, List<Animal>> animals = map.getAnimals();
        System.out.println(animals);

        for (Vector2d position: animals.keySet()) {
            System.out.println(position);
            List<Animal> animalsAtPosition = map.animalsAt(position);
            for (Animal animal : animalsAtPosition) {
                Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / map.getGenomeLength());
                map.move(animal, direction);
                System.out.println("liczba dzieci:");
                System.out.println(animal.getChildrenNumber());
            }
        }

    }

}

