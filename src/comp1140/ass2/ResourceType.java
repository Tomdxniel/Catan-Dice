package comp1140.ass2;

public enum ResourceType {
    BRICK, GRAIN,LUMBER,GOLD,ORE,WOOL;
    //FIXME what is the data size of this piece type?
    //FIXME would having null as the representation for empty be adequate
    public static ResourceType fromChar(char c) {
        return switch (c) {
            case 'b' -> BRICK;
            case 'g' -> GRAIN;
            case 'l' -> LUMBER;
            case 'm' -> GOLD;
            case 'o' -> ORE;
            case 'w' -> WOOL;
            default -> null;
        };
    }



    public char toChar() {
        if(this == GOLD)
        {
            return 'm';
        }
        else
        {
            return this.toString().toLowerCase().charAt(0);
        }
    }
}
