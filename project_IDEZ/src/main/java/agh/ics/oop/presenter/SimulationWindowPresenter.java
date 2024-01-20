
package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;
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
import java.util.Collections;
import java.util.Optional;

public class SimulationWindowPresenter implements MapChangeListener {
    public static final int CELL_WIDTH = 20;
    public static final int CELL_HEIGHT = 20;
    private SimulationEngine engine;
    private SimulationApp appInstance;

    @FXML
    private PieChart pieChart;
    @FXML
    private PieChart lineChart;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        worldMap.subscribe(this);
    }

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    private WorldMap worldMap;

    @FXML
    private GridPane mapGrid;

    @FXML
    public void drawMap() {
        clearGrid();
        int width = worldMap.getUpperRight().getX();
        int height = worldMap.getUpperRight().getY();

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
                } else {
                    label.setStyle("-fx-background-color: " + toHexString(Color.rgb(182, 213, 118)) + ";");
                }
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

        //komórka (0,0)
        Label mainCell = new Label("y/x");
        mapGrid.add(mainCell, 0, 0);
        GridPane.setHalignment(mainCell, HPos.CENTER);

        //label wierszy
        for (int i = 0; i < width + 1; i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }

        //label kolumn
        for (int i = 0; i < height + 1; i++) {
            Label label = new Label(Integer.toString(height - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            //updateAnimalGrassRatioPlot();
            //updateAnimalLineChart();
        });
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void setEngine(SimulationEngine engine) {
        this.engine = engine;
    }

    @FXML
    public void onPauseButtonClicked(ActionEvent actionEvent) {
        try {
            engine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        openStatisticsWhenSimulationStopped();
    }

    public void openStatisticsWhenSimulationStopped(){
        System.out.println("jestem tutaj");
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
/*
    private void updateAnimalLineChart() {
        int animalCount = worldMap.getAnimalCount();

    }

 */

}

