package project_IDEZ.src.main.java.agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Animal implements WorldElement {

    //to chyba bardziej cecha symulacji niż zwierzęcia ale na razie dam tu
    private static final int REPRODUCE_ENERGY_LEVEL = 5; //ustawia uzytkownik

    private static final int INITIAL_ENERGY_LEVEL = 10;
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private int energyLevel;
    private final int mutationPoint = (new Random()).nextInt(GENOM_LENGTH);
    private static final int GENOM_LENGTH = 10; //ustawia uzytkownik

    //idk czy się przyda
    private List<Integer> genom = new ArrayList<>();

    //dla poczatkowych zwierzat
    public Animal(Vector2d position) {
        this.position = position;
        energyLevel = INITIAL_ENERGY_LEVEL;
        orientation = MapDirection.getRandom();
        genom = generateGenom();
    }

    //dla dziecka
    public Animal(List<Integer> childGenom, Vector2d position) {
        this.position = position;
        genom = childGenom;
        energyLevel = 2 * REPRODUCE_ENERGY_LEVEL;
        orientation = MapDirection.getRandom();
    }

    private List<Integer> generateGenom() {
        for (int i = 0; i < GENOM_LENGTH; i++) {
            Random number = new Random();
            genom.add(number.nextInt(8));
        }
        return genom;
    }

    public List<Integer> getGenom() {
        return genom;
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
        }

        energyLevel--;
    }

    //metoda na zmiane aktywnego kierunku (codziennie zwierze wykonuje jeden ruch wg swojego genomu)


    //metoda na spadek poziomu energii
    public int getEnergy() {
        return energyLevel;
    }

    //metoda na zjedzenie rosliny
    public void eat(Grass grass) {
        int energyFromFood = grass.getEnergy();
        energyLevel += energyFromFood;
        grass.wasConsumed();
    }
    //metoda na rozmażanie

    //1 czy może się rozmnażać z drugim zwierzęciem?

    private boolean canReproduceWith(Animal partner) {
        return energyLevel > REPRODUCE_ENERGY_LEVEL && partner.energyLevel > REPRODUCE_ENERGY_LEVEL;
    }

    public Animal reproduceWith(Animal partner) {

        if (canReproduceWith(partner)) {
            List<Integer> childGenom = crossover(partner);
            mutate(childGenom); //uzytkownik wybiera to lub mutate2
            energyLevel -= REPRODUCE_ENERGY_LEVEL;
            partner.energyLevel -= REPRODUCE_ENERGY_LEVEL;

            return new Animal(childGenom, partner.getPosition());
        }
        return null;
    }

    //crossover genomu z genomem innego zwierzęcia
    public List<Integer> crossover(Animal partner) {
        int totalEnergy = partner.energyLevel + this.energyLevel;
        int genomRatio = this.energyLevel / totalEnergy * GENOM_LENGTH;
        int sideIndex = (int) Math.round(Math.random());
        int otherIndex = Math.abs(sideIndex - 1);

        List<Integer> childGenom = getAlphaAnimal(this, partner).get(sideIndex).getGenom();
        for (int i = genomRatio; i < GENOM_LENGTH; i++) {
            childGenom.remove(i);
            childGenom.add(getAlphaAnimal(this, partner).get(otherIndex).getGenom().get(i));
        }
        return childGenom;
    }

    private List<Animal> getAlphaAnimal(Animal animal1, Animal animal2) {
        if (animal1.getEnergy() > animal2.getEnergy()) {
            return List.of(animal1, animal2);
        }
        return List.of(animal2, animal1);
    }

    //mutacje genomu
    private void mutate(List<Integer> childGenom) {
        //int howMany = (new Random()).nextInt(GENOM_LENGTH);
        childGenom.remove(mutationPoint);
        childGenom.add(mutationPoint, new Random().nextInt(8));
    }

    /*
        MUTACJE

        [obowiązkowo dla wszystkich] pełna losowość - mutacja zmienia gen na dowolny inny gen;
        [1] lekka korekta - mutacja zmienia gen o 1 w górę lub w dół (np. gen 3 może zostać zamieniony na 2 lub 4, a gen 0 na 1 lub 7);
         */
    private void mutate2(List<Integer> childGenom) {
        if (Math.random() < 0.5) {
            childGenom.add(mutationPoint, (childGenom.get(mutationPoint) + 1) / 8);
        } else {
            childGenom.add(mutationPoint, (childGenom.get(mutationPoint) - 1) / 8);
        }
    }
}




