package comp1140.ass2;

public enum PieceType {
    ROAD, KNIGHT, CITY, SETTLEMENT, CASTLE;

    public static PieceType fromChar(char c) {
        return switch (c) {
            case 'R' -> ROAD;
            case 'K' -> KNIGHT;
            case 'C' -> CITY;
            case 'S' -> SETTLEMENT;
            case 'A' -> CASTLE;
            default -> null;
        };
    }
}
