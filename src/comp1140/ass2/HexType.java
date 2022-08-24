package comp1140.ass2;

public enum HexType {
    FOREST, PASTURE, FIELD, HILL, MOUNTAIN;


    public static HexType fromChar(char c) {
        return switch (c) {
            case 'F' -> FOREST;
            case 'P' -> PASTURE;
            case 'H' -> HILL;
            case 'M' -> MOUNTAIN;
            case 'I' -> FIELD;
            default -> null;
        };
    }
