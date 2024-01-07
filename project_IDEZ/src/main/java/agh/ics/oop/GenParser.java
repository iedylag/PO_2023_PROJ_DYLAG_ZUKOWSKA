package agh.ics.oop;

import agh.ics.oop.model.Rotation;

import java.util.ArrayList;
import java.util.List;


//  Zwierzak jak już się obróci to może się ruszać jedynie do przodu,
//  więc ta klasa ma zmieniac jego orientacje a nie kierunek ---> Zamiana MoveDIrection na MapDirection


public class GenParser {
    public static List<Rotation> parse(String[] args) {
        List<Rotation> directions = new ArrayList<>();

        for (String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException(arg + " is not legal move specification");
            }
            Rotation direction = switch (arg) {
                case "0" -> Rotation.STRAIGHT;
                case "1" -> Rotation.DEGREE45;
                case "2" -> Rotation.DEGREE90;
                case "3" -> Rotation.DEGREE135;
                case "4" -> Rotation.DEGREE180;
                case "5" -> Rotation.DEGREE225;
                case "6" -> Rotation.DEGREE270;
                case "7" -> Rotation.DEGREE315;
                default -> throw new IllegalArgumentException(arg + " is not legal move specification"); //unchecked
            };
            directions.add(direction);
        }
        return directions;
    }
}
