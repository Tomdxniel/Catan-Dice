package comp1140.ass2;

public enum PieceType {
    ROAD, KNIGHT, CITY, SETTLEMENT;

    public static PieceType fromChar(char c) {
        return switch (c) {
            case 'R' -> ROAD;
            case 'K' -> KNIGHT;
            case 'C' -> CITY;
            case 'S' -> SETTLEMENT;
            default -> null;
        };
    }
}
