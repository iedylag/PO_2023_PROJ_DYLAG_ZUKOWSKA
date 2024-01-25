
package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SimulationWindowPresenter implements MapChangeListener, AnimalChangeListener {
    public static final int CELL_WIDTH = 20;
    public static final int CELL_HEIGHT = 20;
    @FXML
    private Label dzien;
    @FXML
    private Label s8;
    @FXML
    private Label s7;
    @FXML
    private Label s6;
    @FXML
    private Label s5;
    @FXML
    private Label s4;
    @FXML
    private Label s3;
    @FXML
    private Label s2;
    @FXML
    private Label s1;
    @FXML
    private Label infoLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private PieChart lineChart;
    @FXML
    private GridPane mapGrid;
    private SimulationEngine engine;
    private SimulationApp appInstance;
    private Simulation simulation;
    private WorldMap worldMap;
    private Animal selectedAnimal;

    @FXML
    public void drawMap() {
        clearGrid();
        int width = worldMap.getUpperRight().x();
        int height = worldMap.getUpperRight().y();

        createFrame(width, height);

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                Vector2d position = new Vector2d(x, y);
                Optional<WorldElement> element = worldMap.objectAt(position);
                Label label = new Label();
                label.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                if (element.isPresent()) {
                    javafx.scene.paint.Color color = element.get().toColor(worldMap.getStartingEnergyAnimal());
                    label.setStyle("-fx-background-color: " + toHexString(color) + ";");
                    if (element.get() == selectedAnimal) {
                        label.setStyle("-fx-background-color: " + toHexString(Color.rgb(10, 50, 230)) + ";");
                    }
                } else {
                    label.setStyle("-fx-background-color: " + toHexString(Color.rgb(182, 213, 118)) + ";");
                }

                label.setOnMouseClicked(event -> showAnimalStatsAt(position, label));

                mapGrid.add(label, x + 1, height - y + 1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void createFrame(int width, int height) {
        for (int i = 0; i < width + 2; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i < height + 2; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        Label mainCell = new Label("y/x");
        mapGrid.add(mainCell, 0, 0);
        GridPane.setHalignment(mainCell, HPos.CENTER);

        for (int i = 0; i < width + 1; i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }

        for (int i = 0; i < height + 1; i++) {
            Label label = new Label(Integer.toString(height - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            infoLabel.setText("Map ID: " + worldMap.getId());
            dzien.setText("Dzień: " + simulation.getCurrentDay());
        });
    }

    private void showAnimalStatsAt(Vector2d position, Label label) {
        List<Animal> animalsAtPosition = worldMap.getAnimals().get(position);
        if (animalsAtPosition != null && !animalsAtPosition.isEmpty()) {
            setSelectedAnimal(worldMap.getStrongestAnimalAt(position).get());
            selectedAnimal.subscribe(this);
            label.setStyle("-fx-background-color: " + toHexString(Color.rgb(10, 50, 230)) + ";");
            updateAnimalStats(selectedAnimal);
            s1.setText("Liczba zwierzaków na pozycji:" + animalsAtPosition.size());
        }
    }

    @Override
    public void onAnimalChanged(Animal animal) {
        Platform.runLater(() -> {
            if (animal == selectedAnimal) {
                updateAnimalStats(animal);
            }
        });
    }

    private void updateAnimalStats(Animal animal) {
        List<Integer> animalGenome = animal.getGenome().getGenes();
        s1.setText("Statystyki Twojego zwierzaka:");
        s2.setText("Genome:" + animalGenome);
        s3.setText("Aktywny gen:" + animalGenome.get(simulation.getIndex()));
        s4.setText("Energia:" + animal.getEnergy());
        s5.setText("Liczba zjedzonych roślin:" + animal.getEatenGrassCount());
        s6.setText("Liczba dzieci:" + animal.getChildrenNumber());
        s7.setText("Liczba potomków: BRAK METODY");
        s8.setText("Liczba przeżytych dni:" + animal.getLifetime());
    }

    @FXML
    public void onPauseButtonClicked(ActionEvent actionEvent) {
        if (simulation.isPaused()) {
            simulation.resumeSimulation();
        } else {
            simulation.pauseSimulation();
        }
    }

    @FXML
    public void onStopButtonClicked(ActionEvent actionEvent) {
        try {
            engine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        openStatisticsWhenSimulationStopped();
    }

    public void openStatisticsWhenSimulationStopped() {
        try {
            appInstance.openStatisticsWindow(worldMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAnimalGrassRatioPlot() {
        int animalCount = worldMap.getAnimalCount();
        int grassCount = worldMap.getGrassCount();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Animals", animalCount),
                new PieChart.Data("Grass", grassCount)
        );

        pieChart.setData(pieChartData);

    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        worldMap.subscribe(this);
    }

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
    }

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    public void setSelectedAnimal(Animal selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
    }
/*
    private void updateAnimalLineChart() {
        int animalCount = worldMap.getAnimalCount();

    }
*/
}

