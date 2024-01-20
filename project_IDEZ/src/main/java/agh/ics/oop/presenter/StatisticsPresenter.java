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
    private Label averageChildrenCount;
    @FXML
    private Label averageLifeTime;
    @FXML
    private Label averageEnergy;
    @FXML
    private Label emptyFields;
    @FXML
    private Label mostPopularGenome;
    @FXML
    private PieChart animalGrassRatioPlot;

    @FXML
    private Label animalsCountLabel;

    @FXML
    private Label grassCountLabel;
    private WorldMap worldMap;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        updateStatistics();
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
        // Aktualizacja statystyk
        animalsCountLabel.setText("Liczba zwierząt: " + worldMap.getAnimalCount());
        grassCountLabel.setText("Liczba traw: " + worldMap.getGrassCount());
        emptyFields.setText("Liczba wolnych pól: brakuje tego");
        mostPopularGenome.setText("Najpopularniejszy genotyp: [i,n,i,a]");
        averageEnergy.setText("Średnia energia zwierzaków: nie ma?");
        averageLifeTime.setText("Średnia długość życia: nie ma?");
        averageChildrenCount.setText("Średnia liczba dziecu: nie ma?");

        // Aktualizacja wykresu proporcji zwierząt i traw
        updateAnimalGrassRatioPlot(worldMap.getAnimalCount(),  worldMap.getGrassCount());

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
