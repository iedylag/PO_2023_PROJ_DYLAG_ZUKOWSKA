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
    private int eatenGrassCount = 0;

    private List<AnimalChangeListener> listeners = new ArrayList<>();

    //private int birthDay;

    //dla poczatkowych zwierzat
    public Animal(Vector2d position, int energyLevel, int genomeLength) {
        this.position = position;
        orientation = MapDirection.getRandom();
        genome = new Genome(genomeLength);
        //this.birthDay = birthDay;
        this.energyLevel = energyLevel;

    }

    //dla dziecka
    public Animal(Animal mom, Animal dad, Genome childGenome) {
        position = dad.position();
        genome = childGenome;
        orientation = MapDirection.getRandom();
        //this.birthDay = birthDay;
        dad.childrenNumber++;
        mom.childrenNumber++;
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
        orientation = switch (direction) {
            case STRAIGHT -> orientation;
            case DEGREE45 -> orientation.next();
            case DEGREE90 -> orientation.next().next();
            case DEGREE135 -> orientation.next().next().next();
            case DEGREE180 -> orientation.next().next().next().next();
            case DEGREE225 -> orientation.previous().previous().previous();
            case DEGREE270 -> orientation.previous().previous();
            case DEGREE315 -> orientation.previous();
        };

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

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
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

}
