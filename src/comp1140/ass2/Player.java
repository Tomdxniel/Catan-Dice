package comp1140.ass2;


public class Player {

    String name;
    char playerID;
    int score;
    boolean largestArmy = false;
    boolean longestRoad = false;

    Player(String playerName, char ID)
    {
        this.name = playerName;
        this.playerID = ID;
    }





    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerID=" + playerID +
                ", score=" + score +
                '}';
    }
}

