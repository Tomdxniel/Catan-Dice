package comp1140.ass2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;
public class Piece extends Circle {
    private int boardIndex;
    private PieceType type;




    public Piece(int boardIndex, PieceType type,double x, double y) {
        this.boardIndex = boardIndex;
        this.type = type;
        switch(this.type)
        {
            case KNIGHT -> drawKnight(x,y,false);
            case USEDKNIGHT -> drawKnight(x,y,true);
            case SETTLEMENT -> drawSettlement(x,y);
            case CITY -> drawCity(x,y);
            case CASTLE -> drawCastle(x,y);
        }
    }

    public void updatePiece(double x, double y){
        switch(this.type)
        {
            case KNIGHT -> drawKnight(x,y,false);
            case USEDKNIGHT -> drawKnight(x,y,true);
            case SETTLEMENT -> drawSettlement(x,y);
            case CITY -> drawCity(x,y);
            case CASTLE -> drawCastle(x,y);
        }
    }

    public void drawKnight(double x,double y,boolean used){
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



    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }


}
