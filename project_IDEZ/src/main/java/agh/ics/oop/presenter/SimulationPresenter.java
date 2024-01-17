package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import java.io.IOException;


public class SimulationPresenter {

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
    private Spinner<Integer> offspringEnergySpinner; //ustawic to ENERGIA KONIECZNA DO ROZMNAZANIA

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

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @FXML
    private void initialize() {
        startButton.setOnAction(actionEvent -> onSimulationStartClicked());
    }

    @FXML
    private void onSimulationStartClicked() {
        int grassCount = (int) initialGrassSpinner.getValue();
        int width = (int) widthSpinner.getValue();
        int height = (int) heightSpinner.getValue();
        int grassEnergy = energyGrassSpinner.getValue();
        int animalEnergy = startingEnergyAnimalSpinner.getValue();
        int reproductionEnergy = reproductionEnergySpinner.getValue();
        int animalCount = initialAnimalsSpinner.getValue();
        int genomeLength = genomeLengthSpinner.getValue();
        int dailyGrassGrowth = dailyGrowthSpinner.getValue();

        if (grassCount <= width * height) {
            ConsoleMapDisplay display = new ConsoleMapDisplay();
            WorldMap map = new WorldMap(grassCount, width, height, grassEnergy, animalEnergy, reproductionEnergy);
            setWorldMap(map);
            map.subscribe(display);
            System.out.println("dziala");
            SimulationEngine engine = new SimulationEngine(new Simulation(animalCount, worldMap, genomeLength, dailyGrassGrowth, grassVariantSpinner.getValue()));
            try {
                appInstance.openSimulationWindow(engine, map);
                appInstance.openStatisticsWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            engine.runAsyncInThreadPool();
        } else {
            showAlert("Błąd", "Nie ma tyle miejsca na trawę..");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}