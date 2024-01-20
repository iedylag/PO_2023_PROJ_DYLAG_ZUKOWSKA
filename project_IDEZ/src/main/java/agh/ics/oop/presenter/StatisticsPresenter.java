package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;


public class StatisticsPresenter {

    @FXML
    private PieChart animalGrassRatioPlot;

    @FXML
    private Label animalsCountLabel;

    @FXML
    private Label grassCountLabel;
    private WorldMap worldMap;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /*
    public void updateStatistics(int animalCount, int grassCount) {
        Platform.runLater(() -> {
            animalsCountLabel.setText("Liczba zwierząt: " + animalCount);
            grassCountLabel.setText("Liczba traw: " + grassCount);
            // Dodaj inne aktualizacje statystyk, jeśli są potrzebne
        });
    }

     */

    public void updateStatistics() {
        System.out.println("statystykli");
        System.out.println(worldMap);
        System.out.println("mapa");
        setWorldMap(worldMap);
        // Aktualizacja statystyk
        animalsCountLabel.setText("Liczba zwierząt: " + 1);
        grassCountLabel.setText("Liczba traw: " + 1);

        // Aktualizacja wykresu proporcji zwierząt i traw
        updateAnimalGrassRatioPlot(1, 1);

    }


    public void updateAnimalGrassRatioPlot(int animalCount, int grassCount) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Animals", animalCount),
                new PieChart.Data("Grass", grassCount)
        );

        animalGrassRatioPlot.setData(pieChartData);

        animalsCountLabel.setText("Liczba zwierząt: " + animalCount);
        grassCountLabel.setText("Liczba roślin: " + grassCount);
    }


}
