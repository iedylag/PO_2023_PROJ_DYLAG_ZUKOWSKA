package agh.ics.oop;

import agh.ics.oop.model.WorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.SimulationWindowPresenter;
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
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
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

        stage.setScene(new Scene(root));
        stage.setTitle("Mapa");
        stage.show();
        stage.toFront();

    }

}
