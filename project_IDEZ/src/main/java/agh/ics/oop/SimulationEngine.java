package agh.ics.oop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SimulationEngine {
    private final Simulation simulation;
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public SimulationEngine(Simulation simulation) {
        this.simulation = simulation;
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        simulation.stopSimulation();
        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

    public void runAsyncInThreadPool() {
        executorService.submit(simulation);
    }
}