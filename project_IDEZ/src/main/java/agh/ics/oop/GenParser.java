package agh.ics.oop;

import agh.ics.oop.model.Rotation;

import java.util.ArrayList;
import java.util.List;

public class GenParser {
    public static List<Rotation> parse(List<Integer> args) {
        List<Rotation> directions = new ArrayList<>();

        for (int arg : args) {
            Rotation direction = switch (arg) {
                case 0 -> Rotation.STRAIGHT;
                case 1 -> Rotation.DEGREE45;
                case 2 -> Rotation.DEGREE90;
                case 3 -> Rotation.DEGREE135;
                case 4 -> Rotation.DEGREE180;
                case 5 -> Rotation.DEGREE225;
                case 6 -> Rotation.DEGREE270;
                case 7 -> Rotation.DEGREE315;
                default -> throw new IllegalArgumentException(arg + " is not legal move specification"); //unchecked
            };
            directions.add(direction);
        }
        return directions;
    }
}
