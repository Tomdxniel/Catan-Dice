package comp1140.ass2;

public enum PieceType {
    CITY, SETTLEMENT,KNIGHT,USEDKNIGHT,ROAD,CASTLE;
 //FIXME what is the data size of this piece type?
 //FIXME would having null as the representation for empty be adequate
    public static PieceType fromChar(char c) {
        return switch (c) {
            case 'T' -> CITY;
            case 'S' -> SETTLEMENT;
            case 'K' -> KNIGHT;
            case 'J' -> USEDKNIGHT;
            case 'R' -> ROAD;
            case 'C' -> CASTLE;
            default -> null;
        };
    }
}
