package comp1140.ass2;


// Player.java created by Sam Liersch u7448311
public class Player {

    public String name;
    char playerID;
    public int playerIndex;
    public int score;
    boolean largestArmy = false;
    boolean longestRoad = false;

    Player(String playerName, int playerIndex)
    {
        this.playerIndex = playerIndex;
        this.name = playerName;
        this.playerID = Board.playerIDArray.charAt(playerIndex);
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

