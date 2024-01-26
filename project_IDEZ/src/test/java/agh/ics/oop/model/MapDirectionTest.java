package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MapDirectionTest {

    @Test
    void isOurMapAGlobe() {
        //given
        MapDirection dirSouth = MapDirection.SOUTH;
        MapDirection dirEast = MapDirection.EAST;
        MapDirection dirSouthWest = MapDirection.SOUTHWEST;
        MapDirection dirNorth = MapDirection.NORTH;

        //when
        MapDirection dirSouth2 = dirSouth.opposite();
        MapDirection dirEast2 = dirEast.opposite();
        MapDirection dirSouthWest2 = dirSouthWest.opposite();
        MapDirection dirNorth2 = dirNorth.opposite();

        //then
        assertSame(MapDirection.NORTH, dirSouth2);
        assertSame(MapDirection.EAST, dirEast2);
        assertSame(MapDirection.NORTHWEST, dirSouthWest2);
        assertSame(MapDirection.SOUTH, dirNorth2);
    }

    @Test
    void doesAnimalRotateInCorrectWay() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.rotate(Rotation.STRAIGHT));
        assertEquals(MapDirection.EAST, MapDirection.NORTH.rotate(Rotation.DEGREE90));
        assertEquals(MapDirection.NORTH, MapDirection.SOUTH.rotate(Rotation.DEGREE180));
        assertEquals(MapDirection.NORTH, MapDirection.EAST.rotate(Rotation.DEGREE270));
        assertEquals(MapDirection.SOUTHEAST, MapDirection.EAST.rotate(Rotation.DEGREE45));
        assertEquals(MapDirection.EAST, MapDirection.NORTHWEST.rotate(Rotation.DEGREE135));
    }

}