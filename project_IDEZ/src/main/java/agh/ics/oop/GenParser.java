package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

//  Zwierzak jak już się obróci to może się ruszać jedynie do przodu,
//  więc ta klasa ma zmieniac jego orientacje a nie kierunek ---> Zamiana MoveDIrection na MapDirection


public class GenParser {
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> directions = new ArrayList<>();

        for (String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException(arg + " is not legal move specification");
            }
            MoveDirection direction = switch (arg) {
                case "0" -> MoveDirection.STRAIGHT;
                case "1" -> MoveDirection.DEGREE45;
                case "2" -> MoveDirection.DEGREE90;
                case "3" -> MoveDirection.DEGREE135;
                case "4" -> MoveDirection.DEGREE180;
                case "5" -> MoveDirection.DEGREE225;
                case "6" -> MoveDirection.DEGREE270;
                case "7" -> MoveDirection.DEGREE315;
                default -> throw new IllegalArgumentException(arg + " is not legal move specification"); //unchecked
            };
            directions.add(direction);
        }
        return directions;
    }
}
