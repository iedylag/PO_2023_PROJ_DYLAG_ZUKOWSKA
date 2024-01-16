package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.*;

public class Simulation implements Runnable {
    private final int dailyGrowth;
    private final int grassVariant;
    private int currentDay = 0;
    private final int genomeLength;

    //private int averageLifetime = 0;
    private final WorldMap map;

    public Simulation(int animalCount, WorldMap map, int genomeLength, int dailyGrowth, int grassVariant) {

        this.map = map;
        this.genomeLength = genomeLength;
        this.dailyGrowth = dailyGrowth;
        this.grassVariant = grassVariant;
        map.place(animalCount);
    }


    @Override
    public void run() {
        map.removeIfDead();
        moveEachAnimal();
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
        List<Animal> animals = map.getAnimals();

        //w pętli każde zwierze robi jeden krok w zależności od tego który mamy dzień
        for (Animal animal : animals) {
            Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / genomeLength);
            map.move(animal, direction);
        }
    }

}

