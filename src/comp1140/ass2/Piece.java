package comp1140.ass2;

public class Piece {
    private int x;

    private int y;

    private PieceType type;

    private boolean used;


    public Piece(int x, int y, PieceType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.used = false;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public int[] getLocation() {
        return new int[]{ x, y};
    }

    public void setLocation(int[] location) {
        this.x = location[0];
        this.y = location[1];
    }

    public boolean isUsed(){
        return used;
    }
}
