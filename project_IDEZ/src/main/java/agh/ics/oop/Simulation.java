package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Rotation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation implements Runnable {
    private final int dailyGrowth;
    private final int grassVariant;
    private int currentDay = 0;

    //private int averageLifetime = 0;
    private final WorldMap map;

    private boolean running = true;

    public Simulation(int animalCount, WorldMap map, int dailyGrowth, int grassVariant) {

        this.map = map;
        this.dailyGrowth = dailyGrowth;
        this.grassVariant = grassVariant;
        map.place(animalCount);
    }


    public void stopSimulation() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            moveEachAnimal();
            System.out.println("ruszyly sie ");
            map.removeIfDead();
            System.out.println("usuniete");
            map.eatSomeGrass();
            System.out.println("pojedzone");
            System.out.println(map.getGrassCount());
            map.animalsReproduction();
            growMoreGrass();
            currentDay++;

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                running = false;
                e.printStackTrace();
            }

        }

    }

    private void growMoreGrass() {
        map.newGrassGenerator(dailyGrowth);
    }

    public void moveEachAnimal() {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>(map.getAnimals());
        for (Map.Entry<Vector2d, List<Animal>> entry : animalsCopy.entrySet()) {
            for (Animal animal : new ArrayList<>(entry.getValue())) {
                Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / map.getGenomeLength());
                map.move(animal, direction);
                System.out.println(animal + "is moving");
            }
        }
        map.removeEmptyPositions();
    }

}

