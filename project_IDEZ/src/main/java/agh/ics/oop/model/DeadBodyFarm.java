package agh.ics.oop.model;

import java.util.List;


// NA POCZATKU NIE MA MARTWYCH ZWIERZAT WIEC TRAWY POWINNY SIE NORMALNIE WYGENEROWAC (PO TO JEST SUPER() W KONSTRUKTORZE
// METODA GRASSNEARBODYGENERATE MA SIE WYWOLYWAC PO KAZDYM DNIU, PONIEWAZ PO KAZDYM DNIU CHCEMY DODAWAC ROSLINY
// I CODZIENNIE PREFEROWANE POZYCJE DO GENEROWANIA ROSLIN BEDA SIE ZMIENIAC
// JESLI UZYTKOWNIK USTAWI, ZE CODZIENNIE MA SIE DODAWAC 10 ROSLIN TO 8 Z NICH POWINNO SIE WYLOSOWAC Z POZYCJI GETNEARBODYPOSITIONS, A 2 Z RESZTY POZYCJI
public class DeadBodyFarm extends WorldMap {

    public DeadBodyFarm(int grassCount, int height, int width) {
        super(grassCount, height, width);
        grassNearBodyGenerate(grassCount);
    }

    private List<Vector2d> getNearBodyPositions(List<Animal> deadAnimals) { //metoda zwracajaca pozycje wokol wszystkich martwych zwierzat
        return deadAnimals.stream()
                .flatMap(deadAnimal -> deadAnimal.getPosition().around().stream())
                .toList();
    }

    private void grassNearBodyGenerate(int newGrassCount) { //losowanie okreslonej liczby pozycji z getNearBodyPositions
        if (Math.random() < 0.8) {
            //wylosuj pozycje wokol truchla  (w sumie ma byc zawsze 80% wszystkich roslin)
        } else {
            //wylosuj pozycje spoza rownika (w sumie ma byc zawsze 20% wszystkich roslin)
        }
    }
}
