package comp1140.ass2;

public class Player {

    String name;
    int score, knight;
    String[] resource;

    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getAvailableKnights(){
        return knight;
    }

    public void setAvailableKnights(int knight){
        this.knight = knight;
    }

    public String[] getAvailableResources(){
        return resource;
    }

    public void setAvailableResources(String[] resource){
        this.resource = resource;
    }
}
