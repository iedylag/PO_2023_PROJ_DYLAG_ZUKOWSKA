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
    private int deadAnimalsCounter = 0;
    private int averageLifetime = 0;
    private Set<Animal> deadAnimals = new HashSet<>();
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
        //w pętli każde zwierze robi jeden krok w zależności od tego który mamy dzień
        for (Animal currentAnimal : map.getAnimals()) {
            Rotation direction = GenParser.parse(new String[]{currentAnimal.getGenome().toString()}).get(currentDay / genomeLength);
            map.move(currentAnimal, direction);
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
        for (WorldElement element: map.getElements()) {
            if (element.getEnergy() == 0) {
                map.getElements().remove(element);
                deadAnimalsCounter++;
            }
        }
    }
}