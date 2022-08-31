package comp1140.ass2;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Board {
    public static final int[] coordinateArray = {0,4,8,12,7,3,
                                                 1,5,9,13,8,4,
                                                 2,6,10,14,9,5,
                                                 7,12,17,22,16,11,
                                                 8,13,18,23,17,12,
                                                 9,14,19,24,18,13,
                                                 10,15,20,25,19,14,
                                                 16,22,28,33,27,21,
                                                 17,23,29,34,28,22,
                                                 18,24,30,35,28,23,
                                                 19,25,31,36,30,24,
                                                 20,26,32,37,31,25,
                                                 28,34,39,43,38,33,
                                                 29,35,40,44,39,34,
                                                 30,36,41,45,40,35,
                                                 31,37,42,46,41,36,
                                                 39,44,48,51,47,43,
                                                 40,45,49,52,48,44,
                                                 41,46,50,53,49,45};
//FIXME theres a typo in positional indexing, coordinate 50 does not exist

    public Hex[][] hexes = new Hex[5][5];
    public Piece[] castle = new Piece[4];
    public Piece[] knights = new Piece[20];
    public Piece[] settlements = new Piece[54];
    public Piece[] Road;

    public Group hexPlate = new Group();
    public Group castleLayer = new Group();
    public Group knightLayer = new Group();
    public Group settlementLayer = new Group();
    public Group roadLayer = new Group();

    //FIXME are constants all caps
    public static final int hexSize = 75;
    public static final Color hexColor = Color.LIGHTGRAY;



    public void createHexes()
    {
        Hex outLineHex;
        int pieceIndex = 0;
        int coordinate;
        int knightIndex = 0;
        Double[] hexPoints = Hex.generatePoints(hexSize);
        double x;
        double y;
        for(int r = -2; r <= 2; r++)
        {
            for(int q = -2; q <=2; q++)
            {
                if(Math.abs(r+q) < 3)
                {
                   //FIXME im not storing the objects anywhere for the large hexes, is this bad practice?
                    //create Hex for black border
                    outLineHex = new Hex(q,r,null,hexSize+3);
                    outLineHex.setFill(Color.BLACK);
                    hexPlate.getChildren().add(outLineHex);
                    //Main hexes
                    hexes[r+2][q+2] = new Hex(q,r,null,hexSize-3);
                    hexPlate.getChildren().add(hexes[r+2][q+2]);
                    x = hexes[r+2][q+2].getLayoutX();
                    y = hexes[r+2][q+2].getLayoutY();

                    //Set pieces on hex edges
                    for(int i = 0; i < 6; i++)
                    {
                        coordinate = coordinateArray[pieceIndex];

                        //FIXME would settlements only be null if it was not initialized?

                        if(settlements[coordinate] == null)
                        {
                            settlements[coordinate] = new Piece(coordinate,PieceType.SETTLEMENT,x + hexPoints[i*2],y + hexPoints[i*2 + 1]);
                            settlementLayer.getChildren().add(settlements[coordinate]);
                            ;
                        }
                        hexes[r+2][q+2].settlement[i] = settlements[coordinate];
                        pieceIndex++;
                    }
                    if(knightIndex != 9)
                    {
                        knights[knightIndex] = new Piece(knightIndex, PieceType.KNIGHT,x,y);
                        knightLayer.getChildren().add(knights[knightIndex]);
                    }
                    else
                    {
                        knightIndex++;
                    }

                    knightIndex++;

                }
            }

        }

    }

}
