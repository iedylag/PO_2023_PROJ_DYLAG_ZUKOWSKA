package agh.ics.oop.presenter;

import agh.ics.oop.model.WorldMap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StatisticsExporter {
    public void exportToCSV(String filePath, WorldMap worldMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {

            String sb = "ID mapy" +
                    ',' +
                    "Liczba zwierząt" +
                    ',' +
                    "Liczba traw" +
                    ',' +
                    "Liczba wolnych pól" +
                    ',' +
                    "Najpopularniejszy genotyp" +
                    ',' +
                    "Średnia energia zwierząt" +
                    ',' +
                    "Średnia długość życia" +
                    ',' +
                    "Średnia liczba dzieci" +
                    '\n' +
                    worldMap.getId() +
                    ',' +
                    worldMap.getAnimalCount() +
                    ',' +
                    worldMap.getGrassCount() +
                    ',' +
                    worldMap.emptyPositionsNumber() +
                    ',' +
                    worldMap.getTheMostFrequentGenotype() +
                    ',' +
                    worldMap.averageAnimalEnergy().orElse(0) +
                    ',' +
                    worldMap.averageLifetime().orElse(0) +
                    ',' +
                    worldMap.averageAnimalChildren().orElse(0) +
                    '\n';

            writer.write(sb);
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu do pliku CSV: " + e.getMessage());
        }
    }
}

