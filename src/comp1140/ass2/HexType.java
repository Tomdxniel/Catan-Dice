package comp1140.ass2;

public enum HexType {
    FOREST, FIELD, MOUNTAIN, HILL, PASTURE
    ;

    public static HexType fromChar(char c) {
        return switch (c) {
            case 'F' -> FOREST;
            case 'M' -> MOUNTAIN;
            case 'H' -> HILL;
            case 'I' -> FIELD;
            case 'P' -> PASTURE;
            default -> null;
        };
    }
}