package project_IDEZ.src.main.java.agh.ics.oop.model;

//zwierzak moze iść tylko do przodu! to orientacja się zmienia

public enum Rotation {
    STRAIGHT,
    DEGREE45,
    DEGREE90,
    DEGREE135,
    DEGREE180,
    DEGREE225,
    DEGREE270,
    DEGREE315;

    public Rotation next() {
        return switch (this) {
            case DEGREE315 -> Rotation.STRAIGHT;
            case STRAIGHT -> Rotation.DEGREE45;
            case DEGREE45 -> Rotation.DEGREE90;
            case DEGREE90 -> Rotation.DEGREE135;
            case DEGREE135 -> Rotation.DEGREE180;
            case DEGREE180 -> Rotation.DEGREE225;
            case DEGREE225 -> Rotation.DEGREE270;
            case DEGREE270 -> Rotation.DEGREE315;
        };
    }

    public Rotation previous() {
        return switch (this) {
            case DEGREE45 -> Rotation.STRAIGHT;
            case DEGREE90 -> Rotation.DEGREE45;
            case DEGREE135 -> Rotation.DEGREE90;
            case DEGREE180 -> Rotation.DEGREE135;
            case DEGREE225 -> Rotation.DEGREE180;
            case DEGREE270 -> Rotation.DEGREE225;
            case DEGREE315 -> Rotation.DEGREE270;
            case STRAIGHT -> Rotation.DEGREE315;
        };
    }

}
