package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Rotation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                running = false;
            }

        }

    }

    private void growMoreGrass() {
        map.newGrassGenerator(dailyGrowth);
    }

    public void moveEachAnimal() {  //ta metoda cos pierdoli
        Map<Vector2d, List<Animal>> animals = map.getAnimals();
        List<Vector2d> positions = List.copyOf(animals.keySet());
        for (Vector2d position: positions) {
            List<Animal> animalsAtPosition = List.copyOf(map.getAnimals().get(position));
            for (Animal animal : animalsAtPosition) {
                Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(currentDay / map.getGenomeLength());
                map.move(animal, direction);
            }
        }
        map.removeEmptyPositions();
    }

}

