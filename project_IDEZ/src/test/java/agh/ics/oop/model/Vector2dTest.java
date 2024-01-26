package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void animalsAreGoingAroundTheGlobe() {
        //given
        Vector2d vector1 = new Vector2d(2, 5);
        Vector2d vector2 = new Vector2d(0, 0);
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(20, 20);

        //when
        Vector2d vector3 = vector1.oppositeX(lowerLeft, upperRight);
        Vector2d vector4 = vector2.oppositeX(lowerLeft, upperRight);

        // then
        assertEquals(18, vector3.x());
        assertEquals(5, vector3.y());
        assertEquals(20, vector4.x());
        assertEquals(0, vector4.y());
    }

    @Test
    void equalsWorksCorrectly() {
        //given
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(1, 2);
        Vector2d vector3 = new Vector2d(2, 2);
        Vector2d vector4 = new Vector2d(2, 3);

        //when-then
        assertTrue(vector1.equals(vector1)); //same vector
        assertTrue(vector1.equals(vector2)); //same coordinates
        assertFalse(vector1.equals(vector3)); //different x
        assertFalse(vector3.equals(vector4)); //different y
        assertFalse(vector1.equals(vector4)); //different x & y
    }

    @Test
    void toStringCorrectFormat() {
        //given
        Vector2d vector1 = new Vector2d(1, 2);

        //when
        String string1 = vector1.toString();

        //then
        assertEquals("(1,2)", string1);

    }

    @Test
    void precedesWorksCorrectly() {
        //given
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector1a = new Vector2d(1, 2); //same as vector1
        Vector2d vector2 = new Vector2d(3, 5); //x & y greater than in vector1
        Vector2d vector3 = new Vector2d(2, 2); //x greater than in vector1
        Vector2d vector4 = new Vector2d(1, 3); //y greater than in vector1

        //when-then
        assertTrue(vector1.precedes(vector1)); //same vector
        assertTrue(vector1a.precedes(vector1)); //same coordinates
        assertFalse(vector2.precedes(vector1)); //1 does not follow 2
        assertTrue(vector1.precedes(vector2)); //2 follows 1
        assertTrue(vector1.precedes(vector3)); //3 follows 1
        assertTrue(vector1.precedes(vector4)); //4 follows 1
    }

    @Test
    void vectorsCanBeAdded() {
        //given
        Vector2d vector1 = new Vector2d(2, 5);
        Vector2d vector2 = new Vector2d(0, -1);

        //when
        Vector2d vector3 = vector1.add(vector2);

        // then
        assertEquals(2, vector3.x());
        assertEquals(4, vector3.y());
    }

}