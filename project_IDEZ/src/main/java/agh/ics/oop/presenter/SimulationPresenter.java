package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import java.io.IOException;


public class SimulationPresenter{

    public static final int CELL_WIDTH = 40;
    public static final int CELL_HEIGHT = 40;

    @FXML
    private Spinner<Integer> energyGrassSpinner;

    @FXML
    private Spinner<Integer> dailyGrowthSpinner;

    @FXML
    private Spinner<Integer> grassVariantSpinner;

    @FXML
    private Spinner<Integer> initialAnimalsSpinner;

    @FXML
    private Spinner<Integer> startingEnergyAnimalSpinner;

    @FXML
    private Spinner<Integer> reproductionEnergySpinner;

    @FXML
    private Spinner<Integer> offspringEnergySpinner;

    @FXML
    private Spinner<Integer> genomeLengthSpinner;

    @FXML
    private Spinner<Integer> mutationVariantSpinner;
    @FXML
    private Spinner<Integer> minMutationsSpinner;

    @FXML
    private Spinner<Integer> maxMutationsSpinner;
    @FXML
    private Button startButton;

    @FXML
    private Spinner initialGrassSpinner;
    @FXML
    private Spinner heightSpinner;
    @FXML
    private Spinner widthSpinner;

    @FXML
    private GridPane mapGrid;

    private WorldMap worldMap;  //MODEL


    private SimulationApp appInstance;

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
    }

    private SimulationEngine engine;

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @FXML
    private void initialize(){
        startButton.setOnAction(actionEvent -> onSimulationStartClicked());
    }
    @FXML
    private void onSimulationStartClicked() {
        ConsoleMapDisplay display = new ConsoleMapDisplay();
        WorldMap map = new WorldMap((Integer) initialGrassSpinner.getValue(), (Integer) heightSpinner.getValue(), (Integer) widthSpinner.getValue(), energyGrassSpinner.getValue(), startingEnergyAnimalSpinner.getValue(), reproductionEnergySpinner.getValue(), genomeLengthSpinner.getValue());
        setWorldMap(map);
        map.subscribe(display);
        System.out.println("dziala");
        SimulationEngine engine = new SimulationEngine(new Simulation(initialAnimalsSpinner.getValue(), worldMap, dailyGrowthSpinner.getValue(), grassVariantSpinner.getValue()));
        try {
            appInstance.openSimulationWindow(engine, map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        engine.runAsyncInThreadPool();
    }

    /* @FXML
    private void onSimulationStartClicked() {
        try {
            appInstance.openSimulationWindow(engine, worldMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        engine.runAsyncInThreadPool();
    }
    @FXML
    void initialize(){
        System.out.println("uruchamiam");
        ConsoleMapDisplay display = new ConsoleMapDisplay();
        WorldMap map = new WorldMap((Integer) initialGrassSpinner.getValue(), (Integer) heightSpinner.getValue(), (Integer) widthSpinner.getValue(), energyGrassSpinner.getValue(), startingEnergyAnimalSpinner.getValue(), reproductionEnergySpinner.getValue());
        setWorldMap(map);
        map.subscribe(display);
        System.out.println("dziala");
        SimulationEngine engine = new SimulationEngine(new Simulation(initialAnimalsSpinner.getValue(), worldMap, genomeLengthSpinner.getValue(), dailyGrowthSpinner.getValue(), grassVariantSpinner.getValue()));
        setEngine(engine);
        startButton.setOnAction(actionEvent -> onSimulationStartClicked());
    }
*/

}
