package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.WorldMap;
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

    private SimulationApp appInstance;
    private WorldMap worldMap;

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @FXML
    private void initialize() {
        // Inicjalizacja interfejsu użytkownika

        // Dodajemy aktualizację statystyk
        updateStatistics();
    }

    private void updateStatistics() {
        if (worldMap != null) {
            int animalCount = worldMap.getAnimalCount();
            int grassCount = worldMap.getGrassCount();

            // Aktualizacja statystyk
            animalsCountLabel.setText("Liczba zwierząt: " + animalCount);
            grassCountLabel.setText("Liczba traw: " + grassCount);

            // Aktualizacja wykresu proporcji zwierząt i traw
            updateAnimalGrassRatioPlot(animalCount, grassCount);
        }
    }

    private void updateAnimalGrassRatioPlot(int animalCount, int grassCount) {
        // Aktualizacja wykresu
        animalGrassRatioPlot.getData().clear();

        // Dodawanie danych do wykresu
        animalGrassRatioPlot.getData().add(new PieChart.Data("Zwierzęta", animalCount));
        animalGrassRatioPlot.getData().add(new PieChart.Data("Trawy", grassCount));
    }

}
