package agh.ics.oop.model;

import agh.ics.oop.presenter.AnimalChangeListener;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energyLevel;
    private final Genome genome;
    private int lifeTime = 0;
    private int childrenNumber = 0;
    private int descendantsNumber = 0;
    private int eatenGrassCount = 0;
    private Animal mom;
    private Animal dad;
    private final List<AnimalChangeListener> listeners = new ArrayList<>();


    //dla poczatkowych zwierzat
    public Animal(Vector2d position, int energyLevel, int genomeLength) {
        this.position = position;
        orientation = MapDirection.getRandom();
        genome = new Genome(genomeLength);
        this.energyLevel = energyLevel;
    }

    //dla dziecka
    public Animal(Animal mom, Animal dad, Genome childGenome) {
        position = dad.position();
        genome = childGenome;
        orientation = MapDirection.getRandom();
        this.mom = mom;
        this.dad = dad;
        dad.childrenNumber++;
        mom.childrenNumber++;
        dad.descendantsNumber++;
        mom.descendantsNumber++;
    }

    public void subscribe(AnimalChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(AnimalChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyChange() {
        listeners.forEach(listener -> listener.onAnimalChanged(this));
    }

    public void move(Rotation direction, MoveValidator validator) {
        orientation = orientation.rotate(direction);

        Vector2d newPosition = position.add(orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        } else {
            validator.animalOnTheEdge(this, position, orientation);
        }
        energyLevel--;
        lifeTime++;
        notifyChange();
    }

    public void eat(int energyGrass) {
        this.energyLevel += energyGrass;
        eatenGrassCount++;
        notifyChange();
    }

    @Override
    public Vector2d position() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public Genome getGenome() {
        return genome;
    }

    public int getEnergy() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getLifetime() {
        return lifeTime;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getEatenGrassCount() {
        return eatenGrassCount;
    }

    @Override
    public String toString() {
        return String.valueOf(getEnergy());
    }

    @Override
    public Color toColor(int startEnergy) {
        if (energyLevel == 0) return Color.rgb(222, 221, 224);
        if (energyLevel < 0.2 * startEnergy) return Color.rgb(224, 179, 173);
        if (energyLevel < 0.4 * startEnergy) return Color.rgb(224, 142, 127);
        if (energyLevel < 0.6 * startEnergy) return Color.rgb(201, 124, 110);
        if (energyLevel < 0.8 * startEnergy) return Color.rgb(182, 105, 91);
        if (energyLevel < startEnergy) return Color.rgb(164, 92, 82);
        if (energyLevel < 2 * startEnergy) return Color.rgb(146, 82, 73);
        if (energyLevel < 4 * startEnergy) return Color.rgb(128, 72, 64);
        if (energyLevel < 6 * startEnergy) return Color.rgb(119, 67, 59);
        if (energyLevel < 8 * startEnergy) return Color.rgb(88, 50, 44);
        if (energyLevel < 10 * startEnergy) return Color.rgb(74, 42, 37);
        return Color.rgb(55, 31, 27);
    }

    public int getDescendantsNumber() {
        return descendantsNumber;
    }

    public void updateDescendentsNumber() {
        if (mom != null & dad != null) {
            mom.descendantsNumber++;
            dad.descendantsNumber++;
        }
    }

}
