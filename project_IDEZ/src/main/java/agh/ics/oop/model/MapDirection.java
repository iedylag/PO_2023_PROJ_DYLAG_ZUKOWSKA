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

    public MapDirection opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> EAST;
            case SOUTHEAST -> NORTHEAST;
            case NORTHEAST -> SOUTHEAST;
            case SOUTHWEST -> NORTHWEST;
            case WEST -> WEST;
            case NORTHWEST -> SOUTHWEST;
        };
    }

    public MapDirection rotate(Rotation rotation) {
        int directions = MapDirection.values().length;
        int currentOrdinal = this.ordinal();
        int rotationValue = rotation.ordinal();

        int newOrdinal = (currentOrdinal + rotationValue + directions) % directions;
        return MapDirection.values()[newOrdinal];
    }

    public static MapDirection getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }

}
