package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Rotation;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation implements Runnable {
    private final int dailyGrowth;
    private final SimulationApp appInstance;
    private int currentDay = 0;
    private final WorldMap map;
    private final Object pauseLock = new Object();
    private boolean paused = false;
    private boolean running = true;
    private int index;

    public Simulation(int animalCount, WorldMap map, int dailyGrowth, SimulationApp appInstance) {
        this.appInstance = appInstance;
        this.map = map;
        this.dailyGrowth = dailyGrowth;
        map.place(animalCount);
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (pauseLock) {
                    if (paused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }
                index = currentDay % map.getGenomeLength();
                moveEachAnimal();
                removeDeadAnimals();
                map.eatSomeGrass();
                map.animalsReproduction();
                growMoreGrass();
                currentDay++;

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    stopSimulation();
                    e.printStackTrace();
                }

            }
        } catch (Throwable e) {
            e.printStackTrace(); // albo lepiej, jakieś Platform.runLater(() -> showError()) żeby pokazac blad w okienku

        }

    }

    public void pauseSimulation() {
        synchronized (pauseLock) {
            paused = true;
        }
    }

    public void resumeSimulation() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Wznawia wątek
        }
    }

    public void stopSimulation() {
        running = false;
        map.removeFromGenotypeMap();
    }

    private void growMoreGrass() {
        map.newGrassGenerator(dailyGrowth);
    }

    public void moveEachAnimal() {
        Map<Vector2d, List<Animal>> animalsCopy = new HashMap<>(map.getAnimals());
        for (Map.Entry<Vector2d, List<Animal>> entry : animalsCopy.entrySet()) {
            List<Animal> animalsAtPosition = new ArrayList<>(entry.getValue());
            animalsAtPosition.forEach(animal -> {
                Rotation direction = GenParser.parse(animal.getGenome().getGenes()).get(index);
                map.move(animal, direction);
                animal.notifyChange();
            });
        }
    }

    public void removeDeadAnimals() {
        map.removeEmptyPositions();
        if (map.getAnimals().isEmpty()) {
            stopSimulation();
            Platform.runLater(() -> {
                try {
                    appInstance.openStatisticsWindow(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getIndex() {
        return index;
    }
}

