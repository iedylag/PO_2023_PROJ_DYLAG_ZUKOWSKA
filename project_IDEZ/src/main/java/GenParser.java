import model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class GenParser {
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> directions = new ArrayList<>();

        for (String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException(arg + " is not legal move specification");
            }
            MoveDirection direction = switch (arg) {
                case "0" -> MoveDirection.FORWARD;
                case "1" -> MoveDirection.BACKWARD;
                case "2" -> MoveDirection.LEFT;
                case "3" -> MoveDirection.RIGHT;
                case "4" -> MoveDirection.FORWARD;
                case "5" -> MoveDirection.BACKWARD;
                case "6" -> MoveDirection.LEFT;
                case "7" -> MoveDirection.RIGHT;
                default -> throw new IllegalArgumentException(arg + " is not legal move specification"); //unchecked
            };
            directions.add(direction);
        }
        return directions;
    }
}
