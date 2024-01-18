package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;

public class SimulationEngine {
    private final Simulation simulation;

    private List<Simulation> simulations = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public SimulationEngine(Simulation simulation) {
        this.simulation = simulation;
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.SECONDS);
    }

    public void runAsyncInThreadPool() {
        executorService.submit(simulation);

       /* for(int i = 0; i< 10; i++){
            executorService.submit(simulation);
        }*/
        simulations.add(simulation);
    }

    public void pauseSimulation(){
        simulation.setPauseButton(TRUE);
    }

}
