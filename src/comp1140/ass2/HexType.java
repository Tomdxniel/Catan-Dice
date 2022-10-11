package comp1140.ass2;

import com.sun.source.tree.CaseTree;
import javafx.scene.paint.Color;


// HexType.java created by Sam Liersch u7448311
public enum HexType {
    LUMBER, WOOL, GRAIN, ORE, BRICK, WILD;

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
    public static Color toColor(HexType type)
    {
        return switch (type)
            {
                case BRICK -> Color.BROWN;
                case GRAIN -> Color.YELLOW;
                case LUMBER -> Color.DARKGREEN;
                case ORE -> Color.SILVER;
                case WOOL -> Color.GREENYELLOW;
                case WILD -> Color.LIGHTYELLOW;
            };


    }
    public String toString() {
        return switch (this)
                {
                    case BRICK -> "b";
                    case GRAIN -> "g";
                    case LUMBER -> "l";
                    case WILD -> "Wild";
                    case ORE -> "o";
                    case WOOL -> "w";
                };

    }

}