package agh.ics.oop.presenter;

import agh.ics.oop.Configurations;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
    @FXML
    private TextField fileNameField;


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
        int minMutation = minMutationsSpinner.getValue();
        int maxMutation = maxMutationsSpinner.getValue();

        if (grassCount <= width * height) {
            if (maxMutation <= genomeLength) {
                ConsoleMapDisplay display = new ConsoleMapDisplay();

                WorldMap worldMap = switch (mapVariant) {
                    case 1 ->
                            new WorldMap(grassCount, height, width, grassEnergy, animalEnergy, reproductionEnergy, genomeLength, minMutation, maxMutation);
                    case 2 ->
                            new DeadBodyFarm(grassCount, height, width, grassEnergy, animalEnergy, reproductionEnergy, genomeLength, minMutation, maxMutation);
                    default -> throw new IllegalStateException("Unexpected value: " + mapVariant);
                };

                if (mutationVariant == 2) {
                    worldMap.setMutationVariantActivated(true);
                }
                worldMap.subscribe(display);
                Simulation simulation = new Simulation(animalCount, worldMap, dailyGrassGrowth, appInstance);
                SimulationEngine engine = new SimulationEngine(simulation);

                try {
                    appInstance.openSimulationWindow(engine, worldMap, simulation);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                engine.runAsyncInThreadPool();
            } else {
                showAlert("Błąd", "Liczba mutacji nie może być większa niż długość genomu.");
            }
        } else {
            showAlert("Błąd", "Próbujesz wygenerować za dużo trawy.");
        }
    }

    @FXML
    private void onLoadButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("configurations"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            loadConfigurationsFromFile(file);
        }
    }

    public void loadConfigurationsFromFile(File file) {
        // Wczytanie konfiguracji z pliku
        Gson gson = new Gson();
        try {
            Configurations configurations = gson.fromJson(new FileReader(file), Configurations.class);

            energyGrassSpinner.getValueFactory().setValue(configurations.getGrassEnergy());
            dailyGrowthSpinner.getValueFactory().setValue(configurations.getDailyGrassGrowth());
            grassVariantSpinner.getValueFactory().setValue(configurations.getGrassGrowthVariant());
            initialAnimalsSpinner.getValueFactory().setValue(configurations.getInitialAnimalCount());
            startingEnergyAnimalSpinner.getValueFactory().setValue(configurations.getAnimalStartingEnergy());
            reproductionEnergySpinner.getValueFactory().setValue(configurations.getAnimalReproductionEnergy());
            genomeLengthSpinner.getValueFactory().setValue(configurations.getGenomeLength());
            mutationVariantSpinner.getValueFactory().setValue(configurations.getMutationVariant());
            minMutationsSpinner.getValueFactory().setValue(configurations.getMinMutation());
            maxMutationsSpinner.getValueFactory().setValue(configurations.getMaxMutation());
            initialGrassSpinner.getValueFactory().setValue(configurations.getInitialGrassCount());
            heightSpinner.getValueFactory().setValue(configurations.getMapHeight());
            widthSpinner.getValueFactory().setValue(configurations.getMapWidth());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveButtonClicked() {
        String fileName = fileNameField.getText();
        if (fileName.isEmpty()) {
            showAlert("Błąd", "Proszę podać nazwę pliku.");
            return;
        }

        Configurations configurations = collectCurrentConfigurations();
        try {
            saveConfigurationsToJson(configurations, fileName);
        } catch (IOException e) {
            showAlert("Błąd zapisu", "Nie udało się zapisać pliku: " + e.getMessage());
        }
    }

    private Configurations collectCurrentConfigurations() {

        return new Configurations(
                widthSpinner.getValue(),
                heightSpinner.getValue(),
                initialGrassSpinner.getValue(),
                initialAnimalsSpinner.getValue(),
                energyGrassSpinner.getValue(),
                startingEnergyAnimalSpinner.getValue(),
                reproductionEnergySpinner.getValue(),
                genomeLengthSpinner.getValue(),
                minMutationsSpinner.getValue(),
                maxMutationsSpinner.getValue(),
                mutationVariantSpinner.getValue(),
                dailyGrowthSpinner.getValue(),
                grassVariantSpinner.getValue()
        );
    }

    private void saveConfigurationsToJson(Configurations configurations, String fileName) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(configurations);

        Path path = Paths.get("configurations/" + fileName + ".json");
        Files.createDirectories(path.getParent());
        Files.writeString(path, json);
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}