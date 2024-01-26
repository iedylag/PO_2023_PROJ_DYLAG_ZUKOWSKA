package agh.ics.oop;

import agh.ics.oop.model.WorldMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    @Test
    void pauseAndResumeSimulation() {
        int height = 10;
        int width = 10;
        int grassCount = 0;
        int animalCount = 15;
        WorldMap worldMap = new WorldMap(grassCount, height, width, 5, animalCount, 5, 5, 2, 5);
        SimulationApp simApp = new SimulationApp();
        Simulation simulation = new Simulation(5, worldMap, 10, simApp);
        simulation.pauseSimulation();
        assertTrue(simulation.isPaused());
        simulation.resumeSimulation();
        assertFalse(simulation.isPaused());
    }
}