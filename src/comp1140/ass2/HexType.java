package comp1140.ass2;

public enum HexType {
    LUMBER, WOOL, GRAIN, ORE, BRICK, WILD
    ;

    public static HexType fromChar(char c) {
        return switch (c) {
            case 'b' -> BRICK;
            case 'g' -> GRAIN;
            case 'l' -> LUMBER;
            case 'o' -> ORE;
            case 'w' -> WOOL;
            case 'z' -> WILD;
            default -> null;
        };
    }
}