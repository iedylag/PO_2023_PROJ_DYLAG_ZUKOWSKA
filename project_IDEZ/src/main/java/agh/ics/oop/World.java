package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.WorldMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) {
        System.out.println("Start");

        ConsoleMapDisplay display = new ConsoleMapDisplay();
        try {

            //List<Simulation> simulations = new ArrayList<>();

            WorldMap map = new WorldMap(10, 10, 10);
            map.subscribe(display);
            map.subscribe((worldMap, message) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                LocalDateTime dateTime = LocalDateTime.now();
                System.out.println(formatter.format(dateTime) + " " + message);
            });
            Simulation simulation= new Simulation(2, map, 20,5,1,2,1);
            simulation.run();
            /*SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsyncInThreadPool();
            engine.awaitSimulationsE*/
        } catch (IllegalArgumentException /*| InterruptedException */e) {
            e.printStackTrace();
        }

        System.out.println("Stop");
    }
}