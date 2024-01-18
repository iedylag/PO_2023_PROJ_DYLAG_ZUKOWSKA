package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    @Test
    void isAnimalEatingGrass() {
        //given
            WorldMap worldMap = new WorldMap(0, 5 , 5, 5, 5, 5, 5);
            Vector2d position = new Vector2d(2,2);
            Animal owieczka = new Animal(position, 5, 5);
            worldMap.animals.put(position, Collections.singletonList(owieczka));
            worldMap.grasses.put(position, new Grass(position, 2));

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
        WorldMap worldMap = new WorldMap(0, 5 , 5, 5, 15, 5, 5);
        Vector2d position = new Vector2d(2,2);
        Animal owieczka = new Animal(position, 15, 5);
        Animal leniwiec = new Animal(position, 30, 10);
        worldMap.animals.put(position, new ArrayList<>());
        worldMap.animals.values().add(List.of(owieczka, leniwiec));

        //when
        worldMap.animalsReproduction();

        //then
        assertEquals(3, worldMap.animals.get(position).size());
    }
}