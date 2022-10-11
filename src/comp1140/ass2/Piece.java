package comp1140.ass2;

import comp1140.ass2.gui.Game;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Arrays;

// Piece.java created by Sam Liersch u7448311
public class Piece extends Polygon {
    public int boardIndex;
    public Player owner;
    public PieceType type;
    //Player color, Dark for usable piece light for used pieces
    private static final Color[] playerColor = {Color.GREEN,Color.LIGHTGREEN,//Player1
                                   Color.BLUE,Color.LIGHTBLUE,//Player2
                                   Color.PINK,Color.LIGHTPINK,//Player3
                                   Color.YELLOW,Color.LIGHTYELLOW};//Player4




    public Piece(int boardIndex, PieceType type,double x, double y) {
        this.boardIndex = boardIndex;
        this.type = type;
        this.setLayoutX(x);
        this.setLayoutY(y);
        //Update piece to move it to the correct position
        updatePiece();
        //Default colour is gray
        this.setFill(Color.LIGHTGRAY);
        this.setOnMouseClicked((event) ->
        {

            //FIXME move loadBoard to be a constructor and remove
            if(Game.board.resources != null && this.type!=null)
            {
                int count = 0;
                int lastType = -1;
                //If player selects a resource then knight
                Action action = new Action();
                StringBuilder actionString = new StringBuilder();
                actionString.append("build");
                actionString.append(this);
                if(this.type == PieceType.KNIGHT || this.type == PieceType.USEDKNIGHT)
                {
                    actionString.replace(5,6,"K");

                    for(ResourcePiece r: Game.board.resourceDisplay)
                    {
                        if(r.clicked)
                        {
                            lastType = r.type;
                            count ++;
                        }
                    }

                }
                if(count == 1)
                {

                    actionString.setLength(0);
                    actionString.append("swap");
                    actionString.append(Board.resourceArray.charAt(lastType));
                    actionString.append(Board.hexTypeArray[this.boardIndex].toString());
                    if(Board.hexTypeArray[this.boardIndex] != HexType.WILD && actionString.charAt(4) != actionString.charAt(5))
                    {
                        //FIXME create a propper error message;
                        if(!CatanDiceExtra.loadAction(actionString.toString(),action))
                        {
                            throw new RuntimeException();
                        }
                        Game.applyAction(action);
                    }

                }
                else
                {
                    //FIXME create a propper error message;
                    if(!CatanDiceExtra.loadAction(actionString.toString(),action))
                    {
                        throw new RuntimeException();
                    }

                    Game.applyAction(action);
                }
            }
        });

    }

    //For pieces that require two points eg Roads with a point from and a point to
    public Piece(int boardIndex, PieceType type, double startX, double startY, double endX, double endY)
    {
        this.boardIndex = boardIndex;
        this.type = type;
        switch(this.type)
        {
            case ROAD -> drawRoad(startX,startY,endX,endY);
        }

        this.setFill(Color.LIGHTGRAY);


        this.setOnMouseClicked((event) ->
        {

            if (Game.board.resources != null && this.type != null) {
                Action action = new Action();
                StringBuilder actionString = new StringBuilder();
                actionString.append("build");
                actionString.append("R");
                actionString.append(String.format("%4d", this.boardIndex).replace(' ', '0'));
                //FIXME create a propper error message;
                if (!CatanDiceExtra.loadAction(actionString.toString(), action)) {
                    throw new RuntimeException();
                }

                Game.applyAction(action);

            }
        });
    }

    public void updatePiece(){
        this.setFill(Color.LIGHTGRAY);
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
        switch(this.type)
        {
            case KNIGHT -> drawKnight();
            case USEDKNIGHT -> drawKnight();
            case SETTLEMENT -> drawSettlement();
            case CITY -> drawCity();
            case CASTLE -> drawCastle();
        }
    }

    public void drawKnight(){
        Double[] points = new Double[]{10.0,10.0,-10.0,10.0,-10.0,-10.0,10.0,-10.0};
        this.getPoints().addAll(points);
    }

    public void drawCity(){
        Double[] points = new Double[]{10.0,10.0,-10.0,10.0,-10.0,-10.0,0.0,-15.0,10.0,-10.0};
        this.getPoints().clear();
        this.getPoints().addAll(points);
    }

    public void drawSettlement(){
        Double[] points = new Double[]{10.0,10.0,-10.0,10.0,-10.0,-10.0,10.0,-10.0};
        this.getPoints().addAll(points);
    }

    public void drawCastle(){;
        Double[] points = new Double[]{15.0,15.0,-15.0,15.0,-15.0,-15.0,0.0,-30.0,15.0,-15.0};
        this.getPoints().addAll(points);
    }

    public void drawRoad(double startX,double startY, double endX, double endY)
    {
        double roadWidth = 0.03;//roadWidth is 0.1 of length
        double roadLength = 0.3;//roadLength is 0.5 of length
        double vecX = endX-startX;
        double vecY = endY-startY;
        double invecX = startY - endY;
        double invecY = endX-startX;
        //points of the four corners of a road
        Double[] points = new Double[]{
                roadLength * vecX + roadWidth * invecX,roadLength * vecY + roadWidth * invecY,
                roadLength * vecX + -roadWidth * invecX,roadLength * vecY + -roadWidth * invecY,
                -roadLength * vecX + -roadWidth * invecX,-roadLength * vecY + -roadWidth * invecY ,
                -roadLength * vecX + roadWidth * invecX,-roadLength * vecY + roadWidth * invecY};
        this.getPoints().addAll(points);
        this.setLayoutX(startX + (endX-startX)/2);
        this.setLayoutY(startY + (endY-startY)/2);
    }

    public void scale(double scale)
    {
        //scale the piece up from the centre of the piece outwards
        Double[] points = this.getPoints().toArray(new Double[0]);
        for(int i = 0; i < points.length; i++)
        {
            points[i] = points[i] * scale/100;
        }
        this.getPoints().clear();
        this.getPoints().addAll(points);
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