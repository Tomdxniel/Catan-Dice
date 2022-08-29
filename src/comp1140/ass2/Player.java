package comp1140.ass2;

import java.util.Arrays;

public class Player {

    String name;
    char playerID;
    int score;
    PieceType[] knights = new PieceType[20];    //mapped locations to used and unused knights.
    PieceType[] pieces = new PieceType[54];           //Mapped to the locations ,  's' for settlements, 't' for cities, "E" for no piece.
    PieceType[] roads = new PieceType[10000]; //FIXME this is just a placeholder until road format is decided
    PieceType[] castles = new PieceType[4];
    boolean largestArmy = false;
    boolean longestRoad = false;

    Player(String playerName, char ID)
    {
        this.name = playerName;
        this.playerID = ID;
    }




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


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerID=" + playerID +
                ", score=" + score +
                ", knights=" + Arrays.toString(knights) +
                ", resource=" + Arrays.toString(CatanDiceExtra.resources) +
                ", pieces=" + Arrays.toString(pieces) +
                //", roads=" + Arrays.toString(roads) +
                ", castles=" + Arrays.toString(castles) +
                '}';
    }
}

