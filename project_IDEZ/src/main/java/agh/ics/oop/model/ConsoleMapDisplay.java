package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {

    private int updateCount = 0;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        update();
        System.out.println("Update: " + message);
        System.out.println(worldMap);
        System.out.printf("Number of updates so far: %d\n%n", howManyUpdates());
    }

    private void update() {
        updateCount++;
    }

    private int howManyUpdates() {
        return updateCount;
    }
}
