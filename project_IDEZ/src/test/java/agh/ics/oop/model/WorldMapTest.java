package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    @Test
    void isAnimalEatingGrass() {
        //given
        WorldMap worldMap = new WorldMap(0, 5, 5, 2, 5, 5, 5, 3, 10);
        Vector2d position = new Vector2d(2, 2);
        Animal owieczka = new Animal(position, 5, 5);
        worldMap.animals.put(position, Collections.singletonList(owieczka));
        worldMap.grasses.put(position, new Grass(position));

        //when
        worldMap.eatSomeGrass();

        //then
        assertFalse(worldMap.isOccupiedByPlant(position));
        assertTrue(worldMap.isOccupiedByAnimal(position));
        assertEquals(7, owieczka.getEnergy());
    }

    @Test
    void areAnimalsRomanticEnoughToMakeABaby() {
        //given
        WorldMap worldMap = new WorldMap(0, 5, 5, 5, 15, 5, 5, 2, 5);
        Vector2d position = new Vector2d(2, 2);
        Animal owieczka = new Animal(position, 15, 5);
        Animal leniwiec = new Animal(position, 30, 10);
        worldMap.animals.put(position, new ArrayList<>());
        worldMap.animals.get(position).add(owieczka);
        worldMap.animals.get(position).add(leniwiec);

        //when
        worldMap.animalsReproduction();

        //then
        assertEquals(3, worldMap.animals.get(position).size());
    }

    @Test
    void numberOfEmptyPositionsIsCountedCorrectly() {
        //given
        int height = 10;
        int width = 10;
        int grassCount = 0;
        int animalCount = 15;
        WorldMap worldMap = new WorldMap(grassCount, height, width, 5, animalCount, 5, 5, 2, 5);
        worldMap.place(15);

        //when
        int expectedEmptyPositions = (width * height) - (grassCount + animalCount);
        int actualEmptyPositions = worldMap.emptyPositionsNumber();

        //then
        assertEquals(expectedEmptyPositions, actualEmptyPositions);
    }

    @Test
    void testRemoveDeadAnimals() {
        int height = 10;
        int width = 10;
        int grassCount = 0;
        int animalCount = 15;
        WorldMap worldMap = new WorldMap(grassCount, height, width, 5, animalCount, 5, 5, 2, 5);
        SimulationApp simApp = new SimulationApp();
        Simulation simulation = new Simulation(5, worldMap, 10, simApp);
        Vector2d position = new Vector2d(2, 2);
        worldMap.animals.put(position, new ArrayList<>());
        worldMap.animals.get(position).add(new Animal(position, 0, 6));

        simulation.removeDeadAnimals();

        assertEquals(1, worldMap.getDeadAnimals().size());
    }
}