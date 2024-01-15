package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation implements Runnable {
    private final int startingEnergyAnimal;
    private final int energyGrass;
    private final int dailyGrowth;
    private final int grassVariant;
    private int currentDay = 0;
    private final int genomeLength;

    //private int averageLifetime = 0;
    private final WorldMap map;

    public Simulation(int animalCount, WorldMap map, int startingEnergyAnimal, int genomeLength, int energyGrass, int dailyGrowth, int grassVariant) {

        this.map = map;
        this.startingEnergyAnimal = startingEnergyAnimal;
        this.genomeLength = genomeLength;
        this.energyGrass = energyGrass;
        this.dailyGrowth = dailyGrowth;
        this.grassVariant = grassVariant;

        map.place(animalCount);
    }

    @Override
    public void run() {
        removeDeadObjects();
        moveEachAnimal();
        eatSomeGrass();
        animalsReproduction();
        growMoreGrass();
        currentDay++;

        try {
            Thread.sleep(500);
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
        //w pętli każde zwierze robi jeden krok w zależności od tego który mamy dzień
        for (Animal animal : animals) {
            Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / genomeLength);
            map.move(animal, direction);
        }
    }

    private void eatSomeGrass() {
        for (Animal currentAnimal : map.getAnimals()) {
            for (Grass grass : map.getGrass()) {
                if (currentAnimal.getPosition() == grass.getPosition()) {
                    currentAnimal.eat(grass);
                }
            }
        }
    }

    private void removeDeadObjects() {
        map.removeIfDead();
    }
}

