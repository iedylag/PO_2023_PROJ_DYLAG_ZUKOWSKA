package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.WorldMap;

public class World {
    public static void main(String[] args) {
        System.out.println("Start");

        ConsoleMapDisplay display = new ConsoleMapDisplay();
        try {
            WorldMap map = new WorldMap(4, 3, 3, 1, 20, 2, 5);
            map.subscribe(display);
            /* map.subscribe((worldMap, message) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                LocalDateTime dateTime = LocalDateTime.now();
                System.out.println(formatter.format(dateTime) + " " + message);
            }); */
            Simulation simulation = new Simulation(4, map, 2, 1);
            System.out.println(map);
            SimulationEngine engine = new SimulationEngine(simulation);
            engine.runAsyncInThreadPool();
            engine.awaitSimulationsEnd();
        } catch (IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stop");
    }
}