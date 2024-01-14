package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// NA POCZATKU NIE MA MARTWYCH ZWIERZAT WIEC TRAWY POWINNY SIE NORMALNIE WYGENEROWAC (PO TO JEST SUPER() W KONSTRUKTORZE
// METODA GRASSNEARBODYGENERATE MA SIE WYWOLYWAC PO KAZDYM DNIU, PONIEWAZ PO KAZDYM DNIU CHCEMY DODAWAC ROSLINY
// I CODZIENNIE PREFEROWANE POZYCJE DO GENEROWANIA ROSLIN BEDA SIE ZMIENIAC
// JESLI UZYTKOWNIK USTAWI, ZE CODZIENNIE MA SIE DODAWAC 10 ROSLIN TO 8 Z NICH POWINNO SIE WYLOSOWAC Z POZYCJI GETNEARBODYPOSITIONS, A 2 Z RESZTY POZYCJI
public class DeadBodyFarm extends WorldMap {
    List<Animal> deadAnimalList = new ArrayList<>(deadAnimals.values());

    public DeadBodyFarm(int grassCount, int height, int width) {
        super(grassCount, height, width);
    }
//mamy hash mape ze zwierzakami z ich pozycjami
    private List<Vector2d> getNearBodyPositions(List<Animal> deadAnimals) { //metoda zwracajaca pozycje wokol wszystkich martwych zwierzat
        return deadAnimals.stream()
                .flatMap(deadAnimal -> deadAnimal.getPosition().around().stream())
                .toList();
    }

    @Override
    public void generateFromPreferablePosition() {
        int preferableGrassPlaces = (int) (0.2 * width * height);
        List<Vector2d> possiblePositions = getNearBodyPositions(deadAnimalList);
        if (preferableGrassPlaces < possiblePositions.size()){
            Collections.shuffle(possiblePositions);
            possiblePositions = possiblePositions.subList(0, 1);
            for (Vector2d grassPosition : possiblePositions) {
                grasses.put(grassPosition, new Grass(grassPosition));
            }
        }
    }
}
