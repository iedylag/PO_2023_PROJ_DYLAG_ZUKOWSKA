package agh.ics.oop;

import agh.ics.oop.model.WorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.SimulationWindowPresenter;
import agh.ics.oop.presenter.StatisticsPresenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class SimulationApp extends Application {  //dziedziczymy po Application
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();            //ładujemy drzewo kontrolek z FXML (WIDOK)
        configureStage(primaryStage, viewRoot);

        SimulationPresenter presenter = loader.getController();
        presenter.setAppInstance(this);

        primaryStage.show();  // wyswietlamy okno

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);   //tworzymy scene w oknie
        primaryStage.setTitle("Simulation app");   //konfigurujemy okno
        primaryStage.minWidthProperty().bind(viewRoot.widthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.heightProperty());
    }

    public void openStatisticsWindow(WorldMap worldMap) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("statistics.fxml"));
        BorderPane root = loader.load();

        StatisticsPresenter statisticspresenter = loader.getController();
        System.out.println("przekazuje");
        statisticspresenter.setWorldMap(worldMap);

        configureStatisticsStage(stage, root);
        stage.show();

    }

    private void configureStatisticsStage(Stage stage, BorderPane root) {
        var scene = new Scene(root);
        stage.setScene(scene);   //tworzymy scene w oknie
        stage.setTitle("Statystyki");
        stage.setMinHeight(200);
        stage.setMinWidth(200);
        stage.setX(1000); // obok pierwszego okna
    }

    public void openSimulationWindow(SimulationEngine engine, WorldMap map) throws IOException {
        System.out.println("otworz okno");

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane root = loader.load();

        SimulationWindowPresenter presenter = loader.getController();
        presenter.setAppInstance(this);
        presenter.setWorldMap(map);
        presenter.setEngine(engine);

        configureSimulationStage(stage, root);
        stage.show();

    }

    private void configureSimulationStage(Stage stage, BorderPane root) {
        var scene = new Scene(root);
        stage.setScene(scene);   //tworzymy scene w oknie
        stage.setTitle("Mapa");
        stage.minWidthProperty().bind(root.widthProperty());
        stage.minHeightProperty().bind(root.heightProperty());
        stage.setX(100);
    }

}
