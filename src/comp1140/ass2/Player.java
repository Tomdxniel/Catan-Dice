package comp1140.ass2;

public class Player {

    String name;
    char playerID;
    int score;
    PieceType[] knights = new PieceType[20];    //mapped locations to used and unused knights.
    char[] resource = new char[6];          //"bglmow" corresponding to resource, null for no resource
    PieceType[] pieces = new PieceType[54];           //Mapped to the locations ,  's' for settlements, 't' for cities, "E" for no piece.
    PieceType[] roads = new PieceType[10000]; //FIXME this is just a placeholder until road format is decided
    PieceType[] castles = new PieceType[4];






    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public PieceType[] getAvailableKnights(){
        return knights;
    }

    public void setAvailableKnights(PieceType[] knights){
        this.knights = knights;
    }

    public char[] getAvailableResources(){
        return resource;
    }

    public void setAvailableResources(char[] resource){
        this.resource = resource;
    }
}
