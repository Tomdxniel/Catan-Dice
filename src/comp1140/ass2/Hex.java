package comp1140.ass2;

public class Hex {
    private int x;

    private int y;

    private HexType type;


    public Hex(int x, int y, HexType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public HexType getType() {
        return type;
    }

    public void setType(HexType type) {
        this.type = type;
    }

    public int[] getLocation() {
        return new int[]{ x, y};
    }

    public void setLocation(int[] location) {
        this.x = location[0];
        this.y = location[1];
    }
}

