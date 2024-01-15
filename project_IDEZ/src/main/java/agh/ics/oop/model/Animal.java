package agh.ics.oop.model;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Animal implements WorldElement {

    //to chyba bardziej cecha symulacji niż zwierzęcia ale na razie dam tu
    private static final int REPRODUCE_ENERGY_LEVEL = 5; //ustawia uzytkownik

    private static final int INITIAL_ENERGY_LEVEL = 10;
    private MapDirection orientation;
    private Vector2d position;
    private int energyLevel;

    private int birthDay;

    private Genome genome;
    private int lifeTime = 0;
    private int childrenNumber = 0;


    //dla poczatkowych zwierzat
    public Animal(Vector2d position) {
        this.position = position;
        energyLevel = INITIAL_ENERGY_LEVEL;
        orientation = MapDirection.getRandom();
        genome = new Genome();
        //this.birthDay = birthDay;

    }

    //dla dziecka
    public Animal(Animal mom, Animal dad, Genome childGenome) {
        position = dad.getPosition();
        genome = childGenome;
        energyLevel = 2 * REPRODUCE_ENERGY_LEVEL;
        orientation = MapDirection.getRandom();
        //this.birthDay = birthDay;
        dad.childrenNumber++;
        mom.childrenNumber++;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public int getEnergy() {
        return energyLevel;
    }

    public int getLifetime() {
        return lifeTime;
    }

    public int getChildrenNumber() {
        return childrenNumber;
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
            validator.animalOnTheEdge(position, orientation);
        }

        energyLevel--;
        lifeTime++;
    }

    //metoda na zjedzenie rosliny
    public void eat(Grass grass) {
        int energyFromFood = grass.getEnergy();
        energyLevel += energyFromFood;
        grass.wasConsumed();
    }

    //metody na rozmażanie

    //1 czy może się rozmnażać z drugim zwierzęciem?
    private boolean canReproduceWith(Animal partner) {
        return energyLevel > REPRODUCE_ENERGY_LEVEL && partner.energyLevel > REPRODUCE_ENERGY_LEVEL;
    }

    public Animal reproduceWith(Animal partner) {

        if (canReproduceWith(partner)) {
            int totalEnergy = partner.energyLevel + this.energyLevel;
            int genomeRatio = this.energyLevel / totalEnergy * Genome.GENOME_LENGTH;
            Genome childGenome = genome.crossover(genomeRatio, getAlphaAnimal(this, partner));

            childGenome.mutate1(); //uzytkownik wybiera to lub mutate2
            energyLevel -= REPRODUCE_ENERGY_LEVEL;
            partner.energyLevel -= REPRODUCE_ENERGY_LEVEL;

            return new Animal(this, partner, childGenome);
        }
        return null;
    }

    private List<Animal> getAlphaAnimal(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return List.of(animal1, animal2);
        }
        return List.of(animal2, animal1);
    }

    @Override
    public String toString() {
        return String.valueOf(getEnergy());
    }

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
