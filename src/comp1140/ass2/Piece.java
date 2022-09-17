package comp1140.ass2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
public class Piece extends Circle {
    public int boardIndex;
    public Player owner;
    public PieceType type;
    //Player color, Dark for usable piece light for un usable
    private static final Color[] playerColor = {Color.GREEN,Color.LIGHTGREEN,//Player1
                                   Color.BLUE,Color.LIGHTBLUE,//Player2
                                   Color.PINK,Color.LIGHTPINK,//Player3
                                   Color.YELLOW,Color.LIGHTYELLOW};//Player4




    public Piece(int boardIndex, PieceType type,double x, double y) {
        this.boardIndex = boardIndex;
        this.type = type;
        switch(this.type)
        {
            case KNIGHT -> drawKnight(x,y);
            case USEDKNIGHT -> drawKnight(x,y);
            case SETTLEMENT -> drawSettlement(x,y);
            case CITY -> drawCity(x,y);
            case CASTLE -> drawCastle(x,y);
        }
    }
    public Piece(int boardIndex, PieceType type, double startX, double startY, double endX, double endY)
    {
        this.boardIndex = boardIndex;
        this.type = type;
        switch(this.type)
        {
            case ROAD -> drawRoad(startX,startY,endX,endY);
        }

    }

    public void updatePiece(){
        if(owner != null)
        {
            if(type == PieceType.USEDKNIGHT)
            {
                this.setFill(playerColor["WXYZ".indexOf(owner.playerID) * 2 + 1]);
            }
            else
            {
                this.setFill(playerColor["WXYZ".indexOf(owner.playerID)*2]);
            }
        }
    }

    public void drawKnight(double x,double y){
        this.setRadius(10);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    public void drawCity(double x,double y){

    }

    public void drawSettlement(double x,double y){
        this.setRadius(15);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setFill(Color.AQUA);
    }

    public void drawCastle(double x,double y){

    }

    public void drawRoad(double startX,double startY, double endX, double endY)
    {
        this.setRadius(10);
        this.setLayoutX(startX + (endX-startX)/2);
        this.setLayoutY(startY + (endY-startY)/2);
        this.setFill(Color.RED);
    }



    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return switch (this.type) {
            case KNIGHT -> "J" + String.format("%2d",this.boardIndex).replace(' ','0');
            case USEDKNIGHT -> "K" + String.format("%2d",this.boardIndex).replace(' ','0');
            case SETTLEMENT -> "S" + String.format("%2d",this.boardIndex).replace(' ','0');
            case CITY -> "T" + String.format("%2d",this.boardIndex).replace(' ','0');
            case CASTLE -> "C" + this.boardIndex;
            default -> null;
        };

    }

}