package agh.ics.oop.model;


public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny wschód";
            case EAST -> "Wschód";
            case SOUTHEAST -> "Południowy wschód";
            case SOUTH -> "Południe";
            case SOUTHWEST -> "Południowy zachód";
            case WEST -> "Zachód";
            case NORTHWEST -> "Północny zachód";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection  previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST ->SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
        };
    }
    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(0, 1);
            case SOUTHEAST -> new Vector2d(-1, 1);
            case SOUTH -> new Vector2d(-1, 0);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(0, -1);
            case NORTHWEST -> new Vector2d(1, -1);
        };

    }

    public MapDirection opposite(){
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case SOUTHEAST -> NORTHEAST;
            case NORTHEAST -> SOUTHEAST;
            case SOUTHWEST -> NORTHWEST;
            case NORTHWEST -> SOUTHWEST;
            default -> null;
        };
    }

    public static MapDirection getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

}
