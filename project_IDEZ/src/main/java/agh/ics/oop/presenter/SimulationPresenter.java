package agh.ics.oop.presenter;

import agh.ics.oop.GenParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;


public class SimulationPresenter implements MapChangeListener {

    public static final int CELL_WIDTH = 40;
    public static final int CELL_HEIGHT = 40;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label moveDescription;

    @FXML
    private TextField textField;

    @FXML
    private Label infoLabel;
    private WorldMap worldMap;  //MODEL

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        worldMap.subscribe(this);
    }

    @FXML
    private void onSimulationStartClicked() {
            List<Rotation> directions = GenParser.parse((textField.getText()).split("\\s+"));

        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        ConsoleMapDisplay display = new ConsoleMapDisplay();

        List<Simulation> simulations = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            WorldMap grassField = new GrassField(10, 10, 10);
            setWorldMap(grassField);
            grassField.subscribe(display);
            simulations.add(new Simulation(directions, positions, grassField));
        }

        SimulationEngine engine = new SimulationEngine(simulations);
        engine.runAsyncInThreadPool();
    }

    @FXML
    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        int width = Math.abs(boundary.lowLeftCorner().getY() - boundary.upRightCorner().getY()) + 1;
        int height = Math.abs(boundary.lowLeftCorner().getX() - boundary.upRightCorner().getX()) + 1;

        createFrame(height, width, boundary);

        for (int y = boundary.lowLeftCorner().getY(); y <= boundary.upRightCorner().getY(); y++) {
            for (int x = boundary.lowLeftCorner().getX(); x <= boundary.upRightCorner().getX(); x++) {
                Vector2d position = new Vector2d(x, y);
                WorldElement element = worldMap.objectAt(position);
                Label label = new Label();
                if (worldMap.isOccupied(position)) {
                    label.setText(element.toString());
                } else {
                    label.setText(" ");
                }
                mapGrid.add(label, x - boundary.lowLeftCorner().getX() + 1, boundary.upRightCorner().getY() - y + 1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }

    private void createFrame(int height, int width, Boundary boundary) {
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
            Label label = new Label(Integer.toString(boundary.lowLeftCorner().getX() + i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i + 1, 0);
        }

        //label kolumn
        for (int i = 0; i < width; i++) {
            Label label = new Label(Integer.toString(boundary.upRightCorner().getY() - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i + 1);
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            moveDescription.setText(message);
            infoLabel.setVisible(false);
        });
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}
