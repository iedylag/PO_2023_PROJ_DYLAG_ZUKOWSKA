package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.WorldMap;


public class StatisticsPresenter {

    private SimulationApp appInstance;
    private WorldMap worldMap;

    public void setAppInstance(SimulationApp app) {
        this.appInstance = app;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

}

