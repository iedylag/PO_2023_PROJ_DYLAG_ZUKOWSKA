package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        removeDeadObjects();
        moveEachAnimal();
        map.eatSomeGrass();
        animalsReproduction();
        growMoreGrass();
        currentDay++;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void animalsReproduction() {
    }

    private void growMoreGrass() {

    }

    public void moveEachAnimal() {
        List<Animal> animals = map.getAnimals();
        System.out.println(animals);
        System.out.println(map.getDeadAnimals());
        System.out.println(genomeLength);
        System.out.println(currentDay);
        System.out.println(map.getGrassesSize());

        //w pętli każde zwierze robi jeden krok w zależności od tego który mamy dzień
        for (Animal animal : animals) {
            System.out.println(animal.getGenome().getGenes());
            Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / genomeLength);
            map.move(animal, direction);
            System.out.println(animal.getChildrenNumber());
            System.out.println(animal.getPosition());
        }
    }
/*
    private void eatSomeGrass() {
        for (Animal currentAnimal : map.getAnimals()) {
            for (Grass grass : map.getGrass()) {
                if (currentAnimal.getPosition() == grass.getPosition()) {
                    currentAnimal.eat(grass);

                }
            }
        }
    }
 */
    private void removeDeadObjects() {
        map.removeIfDead();
    }
}

