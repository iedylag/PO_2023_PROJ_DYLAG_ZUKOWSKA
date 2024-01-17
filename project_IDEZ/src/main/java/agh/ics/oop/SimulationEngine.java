package agh.ics.oop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;

public class SimulationEngine {
    private final Simulation simulation;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public SimulationEngine(Simulation simulation) {
        this.simulation = simulation;
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(20, TimeUnit.SECONDS);
    }

    public void runAsyncInThreadPool() {
        for(int i=0; i<15; i++) {
            executorService.submit(simulation);
        }
    }

}
