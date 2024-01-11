package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) {
        System.out.println("Start");
        /*

        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        ConsoleMapDisplay display = new ConsoleMapDisplay();

        try {
            List<Rotation> directions = GenParser.parse(args);
            List<Simulation> simulations = new ArrayList<>();

            for (int i = 0; i < 1; i++) {
                GrassField grassField = new GrassField(10, 10, 10); //to ma byc ustawiane przez użytkownika
                grassField.subscribe(display);
                simulations.add(new Simulation(positions, grassField, initialGrass, energyGrass, dailyGrowth, grassVariant));
            }

            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsyncInThreadPool();
            engine.awaitSimulationsEnd();
        } catch (IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stop");
    }

         */
    }
}