package comp1140.ass2;

//Resource.java created by Eliz So u7489812
public enum Resource {
    BRICK, GRAIN, LUMBER, GOLD, ORE, WOOL;
    public static Resource fromChar(char c){
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

    public static Resource fromInt(int i){
        return switch (i) {
            case 0 -> BRICK;
            case 1 -> GRAIN;
            case 2 -> LUMBER;
            case 3 -> GOLD;
            case 4 -> ORE;
            case 5 -> WOOL;
            default -> null;
        };
    }

    public static Resource[] fromIntArray(int[] resources){
        Resource[] res = new Resource[resources.length];

        int ind = 0;

        for (int i = 0; i < resources.length; i++){
            for (int j = 0; j < resources[i]; j++)
            {
                res[ind] = fromInt(i);
                ind++;
            }
        }

        return res;
    }

    @Override
    public String toString(){
        return switch (this) {
            case BRICK -> "b";
            case GRAIN -> "g";
            case LUMBER -> "l";
            case GOLD -> "m";
            case ORE -> "o";
            case WOOL -> "w";
            default -> null;
        };
    }
}
