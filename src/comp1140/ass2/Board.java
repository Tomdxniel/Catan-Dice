package comp1140.ass2;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.*;

// Board.java created by Sam Liersch u7448311
public class Board {
    //Coordinates of the points bordering each hex, north first and then clockwise
    public static final int[] coordinateArray = {0,4,8,12,7,3,
                                                 1,5,9,13,8,4,
                                                 2,6,10,14,9,5,
                                                 7,12,17,22,16,11,
                                                 8,13,18,23,17,12,
                                                 9,14,19,24,18,13,
                                                 10,15,20,25,19,14,
                                                 16,22,28,33,27,21,
                                                 17,23,29,34,28,22,
                                                 18,24,30,35,29,23,
                                                 19,25,31,36,30,24,
                                                 20,26,32,37,31,25,
                                                 28,34,39,43,38,33,
                                                 29,35,40,44,39,34,
                                                 30,36,41,45,40,35,
                                                 31,37,42,46,41,36,
                                                 39,44,48,51,47,43,
                                                 40,45,49,52,48,44,
                                                 41,46,50,53,49,45};
    //Array specifying the type of each hex
    public static final HexType[] hexTypeArray = {
            HexType.WOOL,
            HexType.GRAIN,
            HexType.ORE,
            HexType.ORE,
            HexType.BRICK,
            HexType.LUMBER,
            HexType.WOOL,
            HexType.GRAIN,
            HexType.LUMBER,
            HexType.WILD,
            HexType.WILD,
            HexType.BRICK,
            HexType.GRAIN,
            HexType.WOOL,
            HexType.BRICK,
            HexType.LUMBER,
            HexType.ORE,
            HexType.ORE,
            HexType.GRAIN,
            HexType.WOOL
    };
    //List of all the points on the coast
    public static List<Integer> coastRoads = new ArrayList<>(Arrays.asList(
                    0,
                    4,
                    1,
                    5,
                    2,
                    6,
                    10,
                    15,
                    20,
                    26,
                    32,
                    37,
                    42,
                    46,
                    50,
                    53,
                    49,
                    52,
                    48,
                    51,
                    47,
                    43,
                    38,
                    33,
                    27,
                    21,
                    16,
                    11,
                    7,
                    3
    ));
//FIXME theres a typo in positional indexing in readme, coordinate 50 does not exist

    public Hex[][] hexes = new Hex[5][5];
    public Piece[] castles = new Piece[4];
    public Piece[] knights = new Piece[20];
    public Piece[] settlements = new Piece[54];

    public Group hexPlate = new Group();
    public Group castleLayer = new Group();
    public Group knightLayer = new Group();
    public Group settlementLayer = new Group();
    public Group roadLayer = new Group();

    //FIXME whats the difference between map and hashMap
    public HashMap<Integer,Piece> roadsMap = new HashMap<>();
    //FIXME are constants names all caps
    //FIXME How do you replace all of a variable with the same name with a different name
    //FIXME should these be public // should a getter class be used
    public static final int hexSize = 75;
    public boolean setupPhase = false;
    public int[] resources;           //"bglmow" corresponding to resource,{b,g,l,m,o,w}

    public Player playerTurn;
    public int numDice;
    public int rollsDone;
    public static final double boarderScale = 0.3;
    public final int playerCount = 2;
    public Player[] players = new Player[playerCount];
    private static final String[] names = {"Sam","Jim","Eliz","Tom"}; //Names of each player, Jim is just filler
    public static final String resourceArray = "bglmow";
    public static final String playerIDArray = "WXYZ";

    public double BOARD_HEIGHT,BOARD_WIDTH;
    public Board(double VIEWER_HEIGHT, double VIEWER_WIDTH){
        this.BOARD_HEIGHT = VIEWER_HEIGHT;
        this.BOARD_WIDTH = VIEWER_WIDTH;
        Hex.setUpHex(hexSize,VIEWER_HEIGHT,VIEWER_WIDTH);
        this.createHexes();
    }

    public void createHexes()
    {
        Hex outLineHex;
        int pieceIndex = 0;
        int coordinate;
        int knightIndex = 0;


        //location of 2 neighbouring hex vertexes for roads
        int maxPos;
        int minPos;
        Double[] hexPoints = Hex.generatePoints(hexSize);

        //location of hex on window
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
                    outLineHex = new Hex(q,r,null,hexSize * 1.05);
                    outLineHex.setFill(Color.BLACK);
                    hexPlate.getChildren().add(outLineHex);
                    //Main hexes
                    hexes[r+2][q+2] = new Hex(q,r,hexTypeArray[knightIndex],hexSize*0.95);
                    hexPlate.getChildren().add(hexes[r+2][q+2]);
                    x = hexes[r+2][q+2].getLayoutX();
                    y = hexes[r+2][q+2].getLayoutY();

                    //Set pieces on hex edges
                    for(int i = 0; i < 6; i++)
                    {
                        coordinate = coordinateArray[pieceIndex + i];

                        //FIXME would settlements only be null if it was not initialized?
                        //Set settlements/Citites at hex vertexes
                        if(settlements[coordinate] == null)
                        {
                            createOutlinePiece(
                                    PieceType.SETTLEMENT,//Piece type
                                    x + hexPoints[i*2],//startX
                                    y + hexPoints[i*2 + 1],//startY
                                    settlementLayer);//Group

                            settlements[coordinate] = new Piece(
                                    coordinate,//Piece index
                                    PieceType.SETTLEMENT,//Piece type
                                    x + hexPoints[i*2],//startX
                                    y + hexPoints[i*2 + 1]);//startY
                            settlementLayer.getChildren().add(settlements[coordinate]);
                        }
                        hexes[r+2][q+2].settlement[i] = settlements[coordinate];

                    }
                    for(int i = 0; i < 6; i++)
                    {
                        maxPos = Math.max(coordinateArray[pieceIndex + i],coordinateArray[pieceIndex + (i + 1) % 6]);
                        minPos = Math.min(coordinateArray[pieceIndex + i],coordinateArray[pieceIndex + (i + 1) % 6]);
                        if(!roadsMap.containsKey(minPos * 100+maxPos))
                        {
                            roadsMap.put(minPos * 100 + maxPos,new Piece(
                                    minPos * 100 + maxPos,//roadIndex
                                    PieceType.ROAD, //Piece types
                                    x + hexPoints[i*2],//startX
                                    y + hexPoints[i*2+ 1],//startY
                                    x + hexPoints[(i+1)%6*2],//endX
                                    y + hexPoints[(i+1)%6*2 + 1]//endY
                                    ));

                            roadLayer.getChildren().add(roadsMap.get(minPos*100 + maxPos));
                        }
                        hexes[r+2][q+2].roads[i] = roadsMap.get(minPos*100 + maxPos);
                    }

                    pieceIndex += 6;
                    hexes[r+2][q+2].index = knightIndex;

                    //For each hex create a slightly bigger hex and have it be completely black to create a border
                    if(knightIndex != 9)//The two knights at Pos 9 & 10 are handled separately
                    {

                        createOutlinePiece(PieceType.KNIGHT,x,y,knightLayer);

                        knights[knightIndex] = new Piece(knightIndex, PieceType.KNIGHT,x,y);
                        knightLayer.getChildren().add(knights[knightIndex]);

                    }
                    else
                    {
                        createOutlinePiece(PieceType.KNIGHT,x-15,y,knightLayer);
                        createOutlinePiece(PieceType.KNIGHT,x+15,y,knightLayer);

                        knights[9] = new Piece(9,PieceType.KNIGHT,x-15,y);
                        knights[10] = new Piece(10,PieceType.KNIGHT,x+15,y);

                        knightLayer.getChildren().add(knights[9]);
                        knightLayer.getChildren().add(knights[10]);
                        knightIndex++;
                    }
                    knightIndex++;


                }
            }

        }
        //Castles are always static and appear on the four corners of the board
        castles[0] = new Piece(0,PieceType.CASTLE,BOARD_WIDTH/10,BOARD_HEIGHT/10);
        castles[1] = new Piece(1,PieceType.CASTLE,BOARD_WIDTH -BOARD_WIDTH/10,BOARD_HEIGHT/10);
        castles[2] = new Piece(2,PieceType.CASTLE,BOARD_WIDTH - BOARD_WIDTH/10,BOARD_HEIGHT -BOARD_HEIGHT/10);
        castles[3] = new Piece(3,PieceType.CASTLE,BOARD_WIDTH/10,BOARD_HEIGHT -BOARD_HEIGHT/10);
        castleLayer.getChildren().addAll(castles);

    }

    //To create a boarder for a piece create a new piece of different colour that's slightly bigger and underneath
    private void createOutlinePiece(PieceType type,double x, double y, Group group)
    {
        Piece outlinePiece = new Piece(0,type,x,y);
        outlinePiece.scale(boarderScale);
        outlinePiece.setFill(Color.DARKGREY);
        group.getChildren().add(outlinePiece);
    }



    /*
    Breaks the board state into section of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score] and stores it in respective places
    then does checks to see if boardstate is valid
     */

    public boolean loadBoard(String boardState)
    {
        int index = 0;
        boolean winner = false;
        boolean hasLongestRoad = false;
        boolean hasLargestArmy = false;
        for(int i = 0; i < playerCount; i++)
        {
            this.players[i] = new Player(names[i],playerIDArray.charAt(i));
        }

        //FIXME are we supposed to have definitions of @param and @return for each method we create
        try {
            for(int i = 0; i < playerCount; i++)
            {
                if( this.players[i].playerID == boardState.charAt(0))
                {
                    this.playerTurn = this.players[i];
                }
            }
            //if no valid playerTurn return false
            if(playerIDArray.indexOf(this.playerTurn.playerID) < 0)
            {
                return false;
            }
            index++;
            //Test dice number is valid
            if("03456".contains(boardState.substring(index, index + 1)))
            {
                this.numDice = Integer.parseInt(boardState.substring(index, index + 1));
                if('0' == boardState.charAt(index))
                {
                    this.setupPhase = true;
                }
            }
            else
            {
                return false;
            }
            index++;
            //Test Rolls done is valid
            if("0123".contains(boardState.substring(index, index + 1)))
            {
                this.rollsDone = Integer.parseInt(boardState.substring(index, index + 1));
                if('0' == boardState.charAt(index) && !this.setupPhase)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            index++;

            this.resources = new int[]{0,0,0,0,0,0};
            char currentChar;
            char prevChar = boardState.charAt(index);
            //Check resources are in the correct order and valid
            for(int i = 0; i < 6; i++)
            {
                currentChar = boardState.charAt(index);
                if( resourceArray.indexOf(currentChar) < 0 || this.setupPhase)
                {
                    break;
                }
                //Testing whether resources are in order
                if((int)prevChar > (int) currentChar)
                {
                    return false;
                }
                prevChar = currentChar;

                this.resources[resourceArray.indexOf(currentChar)] ++;
                index++;
            }

            int pos;
            for (int i = 0; i < playerCount; i++) {
                //[ID]

                //Make sure first Player is W next player is X
                if(!(playerIDArray.charAt(i) == boardState.charAt(index)))
                {
                    return false;
                }
                index++;
                //[Placement]
                //Castle
                while (boardState.charAt(index) == 'C') {
                    index++;
                    pos = (int) boardState.charAt(index) - 48;
                    //FIXME Is referencing a variable of player1 directly and not using a function of player one bad practice
                    if(this.castles[pos].owner == null)
                    {
                        this.castles[pos].owner = this.players[i];
                    }
                    index++;
                }

                //Used/Unused Knight
                while (boardState.charAt(index) == 'J' || boardState.charAt(index) == 'K') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 2));
                    if (this.knights[pos].owner == null) {
                        this.knights[pos].type = (boardState.charAt(index-1) == 'K') ? PieceType.USEDKNIGHT : PieceType.KNIGHT ;
                        this.knights[pos].owner = this.players[i];
                    }
                    index += 2;
                }

                //Road
                //FIXME Why is road R0440 valid?
                while (boardState.charAt(index) == 'R') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 4));
                    if(pos%100 < 54 && pos/100 < 54 && pos/100 < pos%100) {
                        if (this.roadsMap.containsKey(pos)) {
                            if (this.roadsMap.get(pos).owner == null) {
                                this.roadsMap.get(pos).owner = this.players[i];
                            }

                        }
                    }
                    else
                    {
                        return false;
                    }
                    index += 4;

                }

                //Settlement
                while (boardState.charAt(index) == 'S' || boardState.charAt(index) == 'T') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 2));
                    //FIXME if its acceptable to check by try catch is it okay to simplify this as if the positon isnt between 0 and 54 it would create an error
                    if (this.settlements[pos].owner == null) {
                        this.settlements[pos].type = (boardState.charAt(index-1) == 'S') ? PieceType.SETTLEMENT : PieceType.CITY ;
                        this.settlements[pos].owner = this.players[i];
                    }
                    index += 2;
                }


            }
            for(int i = 0; i < playerCount; i++)
            {
                //-------------------------------------
                //Player Score
                //Make sure first Player is W next player is X
                if(!(this.players[i].playerID == boardState.charAt(index)))
                {
                    return false;
                }
                index++;

                this.players[i].score = Integer.parseInt(boardState.substring(index, index + 2));
                //2 winners cant exist
                if(this.players[i].score >= 10)
                {
                    if(winner)
                    {
                        return false;
                    }
                    winner = true;
                }
                //Final score always less than 13
                if(this.players[i].score > 12)
                {
                    return false;
                }
                index += 2;
                //FIXME Is there a more efficient way to do this without using try catch
                try{
                    if(boardState.charAt(index) == 'R')
                    {
                        //Duplicate  of longestRoad
                        if(hasLongestRoad)
                        {
                            return false;
                        }
                        hasLongestRoad = true;
                        this.players[i].longestRoad = true;
                        index++;
                    }

                    if(boardState.charAt(index) == 'A'){
                        //Duplicate  of largestArmy
                        if(hasLargestArmy)
                        {
                            return false;
                        }
                        hasLargestArmy = true;
                        this.players[i].largestArmy = true;
                        index++;
                    }

                }
                catch (StringIndexOutOfBoundsException e)
                {
                    //String is finished
                }

            }
            //Check if no extra characters on end
            if(boardState.length() != index)
            {
                return false;
            }



        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    @Override
    // rebuilds board string of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score]
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(this.playerTurn.playerID);
        output.append(this.numDice);
        output.append(this.rollsDone);
        Integer[] roadSort = new Integer[this.roadsMap.keySet().size()];
        for(int i = 0; i < 6; i++)
        {
            for(int y = 0; y < this.resources[i]; y++)
            {
                output.append(resourceArray.charAt(i));
            }
        }
        for(int i = 0; i < this.playerCount; i++)
        {
            output.append(this.players[i].playerID);
            for(int j = 0; j < this.castles.length; j++)
            {
                if(this.castles[j].owner == this.players[i])
                {
                    output.append(this.castles[j].toString());
                }
            }
            //Knight
            for(int j = 0; j < this.knights.length; j++)
            {
                if(this.knights[j].owner == this.players[i] && this.knights[j].type == PieceType.KNIGHT)
                {
                    output.append(this.knights[j].toString());
                }
            }
            //UsedKnight
            for(int j = 0; j < this.knights.length; j++)
            {
                if(this.knights[j].owner ==  this.players[i]&& this.knights[j].type == PieceType.USEDKNIGHT)
                {
                    output.append(this.knights[j].toString());
                }
            }

            //ROADS
            //FIXME is there anyway to map through the hashMap without using lambda calculus or converting to an array first
            this.roadsMap.keySet().toArray(roadSort);
            Arrays.sort(roadSort);
            for(int j : roadSort) {
                if (this.roadsMap.get(j).owner  == this.players[i]) {
                    output.append("R");
                    output.append(String.format("%4d", j).replace(' ', '0'));
                }
            }
            //SETTLEMENTS
            for(int j = 0; j < this.settlements.length; j++)
            {
                if(this.settlements[j].type == PieceType.SETTLEMENT && this.settlements[j].owner == this.players[i])
                {
                    output.append(this.settlements[j].toString());
                }
            }
            //CITIES
            for(int j = 0; j < this.settlements.length; j++)
            {
                if(this.settlements[j].type == PieceType.CITY && this.settlements[j].owner == this.players[i])
                {
                    output.append(this.settlements[j].toString());
                }
            }
        }
        for(int i = 0; i < this.playerCount; i++)
        {
            output.append(this.players[i].playerID);
            output.append(String.format("%2d",this.players[i].score).replace(' ','0'));
            if(this.players[i].longestRoad)
            {
                output.append('R');
            }
            if(this.players[i].largestArmy)
            {
                output.append('A');
            }

        }
        return output.toString();
    }

}
