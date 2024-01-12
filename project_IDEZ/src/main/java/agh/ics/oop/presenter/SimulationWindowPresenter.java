/*
package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Collections;

public class SimulationWindowPresenter implements MapChangeListener {

    public static final int CELL_WIDTH = 40;
    public static final int CELL_HEIGHT = 40;

    private SimulationApp appInstance;
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
    private void onSimulationStartClicked() {
        appInstance.openSimulationWindow();
    }
    @FXML
    public void drawMap() {
        clearGrid();
        int width = worldMap.getUpperRight().getX();
        int height = worldMap.getUpperRight().getY();

        createFrame(height, width);

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                Vector2d position = new Vector2d(x, y);
                WorldElement element = worldMap.objectAt(position);
                Label label = new Label();
                if (worldMap.isOccupied(position)) {
                    label.setText(element.toString());
                } else {
                    label.setText(" ");
                }
                mapGrid.add(label, x + 1, height - y + 1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }

    private void createFrame(int height, int width) {
        for (int i = 0; i < height + 1; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i < width + 1; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        //komórka (0,0)
        Label mainCell = new Label("y/x");
        mapGrid.add(mainCell, 0, 0);
        GridPane.setHalignment(mainCell, HPos.CENTER);

        //label wierszy
        for (int i = 0; i < height; i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }

        //label kolumn
        for (int i = 0; i < width; i++) {
            Label label = new Label(Integer.toString(width - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}
*/
