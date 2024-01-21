package agh.ics.oop.presenter;

import agh.ics.oop.model.WorldMap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StatisticsExporter {
    public void exportToCSV(String filePath, WorldMap worldMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID mapy");
            sb.append(',');
            sb.append("Liczba zwierząt");
            sb.append(',');
            sb.append("Liczba traw");
            sb.append(',');
            sb.append("Liczba wolnych pól");
            sb.append(',');
            sb.append("Najpopularniejszy genotyp");
            sb.append(',');
            sb.append("Średnia energia zwierząt");
            sb.append(',');
            sb.append("Średnia długość życia");
            sb.append(',');
            sb.append("Średnia liczba dzieci");
            sb.append('\n');

            sb.append(worldMap.getId());
            sb.append(',');
            sb.append(worldMap.getAnimalCount());
            sb.append(',');
            sb.append(worldMap.getGrassCount());
            sb.append(',');
            sb.append(worldMap.emptyPositionsNumber());
            sb.append(',');
            sb.append("[i,n,i,a]");
            sb.append(',');
            sb.append(worldMap.averageAnimalEnergy().orElse(0.0));
            sb.append(',');
            sb.append(worldMap.averageLifetime().orElse(0.0));
            sb.append(',');
            sb.append("Średnia liczba dziecu: nie ma?");
            sb.append('\n');

            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu do pliku CSV: " + e.getMessage());
        }
    }
}

