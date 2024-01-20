package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;

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
    private Spinner<Integer> initialGrassSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;
    @FXML
    private Spinner<Integer> widthSpinner;

    private SimulationApp appInstance;

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }


    @FXML
    private void initialize() {
        startButton.setOnAction(actionEvent -> onSimulationStartClicked());
    }

    @FXML
    private void onSimulationStartClicked() {
        int grassCount = initialGrassSpinner.getValue();
        int width = widthSpinner.getValue();
        int height = heightSpinner.getValue();
        int grassEnergy = energyGrassSpinner.getValue();
        int animalEnergy = startingEnergyAnimalSpinner.getValue();
        int reproductionEnergy = reproductionEnergySpinner.getValue();
        int animalCount = initialAnimalsSpinner.getValue();
        int genomeLength = genomeLengthSpinner.getValue();
        int dailyGrassGrowth = dailyGrowthSpinner.getValue();
        int mutationVariant = mutationVariantSpinner.getValue();
        int mapVariant = grassVariantSpinner.getValue();

        if (grassCount <= width * height) {
            ConsoleMapDisplay display = new ConsoleMapDisplay();

            WorldMap worldMap = switch (mapVariant) {
                case 1 -> new WorldMap(grassCount, height, width, grassEnergy, animalEnergy, reproductionEnergy, genomeLength);
                case 2 -> new DeadBodyFarm(grassCount, height, width, grassEnergy, animalEnergy, reproductionEnergy, genomeLength);
                default -> throw new IllegalStateException("Unexpected value: " + mapVariant);
            };

            if(mutationVariant == 2){
                worldMap.setMutationVariantActivated(true);
            }
            worldMap.subscribe(display);
            System.out.println("dziala");
            SimulationEngine engine = new SimulationEngine(new Simulation(animalCount, worldMap, dailyGrassGrowth, appInstance));

            try {
                appInstance.openSimulationWindow(engine, worldMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            engine.runAsyncInThreadPool();
        } else {
            showAlert("Błąd", "Próbujesz wygenerować za dużo trawy.");

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