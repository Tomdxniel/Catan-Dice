package comp1140.ass2;

import comp1140.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
            HexType. WOOL,
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
    //Array to show the max level of the buildings
    public static int[] levelArray =
            {1,2,1,
            0,0,0,0,
            2,1,1,2,
            0,0,0,0,0,
            1,2,2,2,1,
            0,0,0,0,0,0,
            0,0,0,0,0,0,
            1,2,2,2,1,
            0,0,0,0,0,
            2,1,1,2,
            0,0,0,0,
            1,2,1};
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

    public static final Integer[] cities =
            {
                    1,
                    7, 10,
                    17, 18, 19,
                    34, 35, 36,
                    43, 46,
                    52

            };
//FIXME theres a typo in positional indexing in readme, coordinate 50 does not exist

    public Hex[][] hexes = new Hex[5][5];
    public Piece[] castles = new Piece[4];
    public Piece[] knights = new Piece[20];
    public Piece[] settlements = new Piece[54];
    public Text turnText = new Text();
    public Text scoreText = new Text();
    public Text resourceLabel = new Text();
    public Text rollText = new Text();
    public Text errorText = new Text();
    public ResourcePiece[] resourceDisplay = new ResourcePiece[6];
    public Group hexPlate = new Group();
    public Group castleLayer = new Group();
    public Group knightLayer = new Group();
    public Group settlementLayer = new Group();
    public Group roadLayer = new Group();
    public Group turnLayer = new Group();
    public VBox turnBox = new VBox();
    //FIXME whats the difference between map and hashMap
    //FIXME are constants names all caps
    //FIXME How do you replace all of a variable with the same name with a different name
    //FIXME should these be public // should a getter class be used
    public HashMap<Integer,Piece> roadsMap = new HashMap<>();
    public static final int hexSize = 75;
    public boolean setupPhase = false;
    public int[] resources;           //"bglmow" corresponding to resource,{b,g,l,m,o,w}

    public Player playerTurn;
    public int numDice;
    public int rollsDone;
    public int playerCount = 2;
    public Player[] players;
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

                        //Set settlements/Cities at hex vertexes
                        if(settlements[coordinate] == null)
                        {
                            settlements[coordinate] = new Piece(
                                    coordinate,//Piece index
                                    PieceType.SETTLEMENT,//Piece type
                                    x + hexPoints[i*2],//startX
                                    y + hexPoints[i*2 + 1]);//startY
                            settlements[coordinate].maxLevel = levelArray[coordinate];
                            settlements[coordinate].updatePiece();
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


                        knights[knightIndex] = new Piece(knightIndex, PieceType.KNIGHT,x,y);
                        knightLayer.getChildren().add(knights[knightIndex]);

                    }
                    else
                    {

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

        //Create box to store turn textInfo
        turnBox.setSpacing(10);
        turnBox.setLayoutX(BOARD_WIDTH/20);
        turnBox.setLayoutY(BOARD_HEIGHT/10 * 3.75);
        turnBox.getChildren().addAll(turnText,rollText,scoreText);
        turnLayer.getChildren().add(turnBox);

        //Create text of whose turn it is
        turnText.setFont(Font.font(20));

        //Create text of what the score is
        scoreText.setFont(Font.font(20));

        //Create text to say how many rolls are left
        rollText.setFont(Font.font(20));

        //Create label for resource list
        resourceLabel.setX(BOARD_WIDTH/10 * 8);
        resourceLabel.setY(BOARD_HEIGHT/10 * 2.5);
        resourceLabel.setText("Resources:");
        resourceLabel.setFont(Font.font(20));
        turnLayer.getChildren().add(resourceLabel);

        //Create error text
        errorText.setX(BOARD_WIDTH/3);
        errorText.setY(BOARD_HEIGHT/20 * 19.5);
        errorText.setFont(Font.font(20));
        turnLayer.getChildren().add(errorText);

        //Add 6 resource squares
        for(int i = 0; i < 6; i++)
        {

            resourceDisplay[i] = new ResourcePiece(-1,BOARD_WIDTH/10 * 9,BOARD_HEIGHT/10 * (i + 2.5));

            turnLayer.getChildren().add(resourceDisplay[i].outline);
            turnLayer.getChildren().add(resourceDisplay[i]);
        }


    }
    public void updateTurnInfo()
    {
        //Add text to say players turn
        turnText.setText("Players Turn: " + this.playerTurn.name + " (" + this.playerTurn.playerID + ").");
        StringBuilder scoreOut = new StringBuilder();
        for(Player i : this.players)
        {
            scoreOut.append(i.name);
            scoreOut.append(": ");
            scoreOut.append(i.score);
            scoreOut.append("\n");
        }
        scoreText.setText("Score List: \n" + scoreOut);
        if(this.setupPhase)
        {
            rollText.setText("Setup Phase");
        }
        else
        {
            rollText.setText("Rolls Left: " + (3 - rollsDone));
        }
        int count = 0;
        for(int i = 0 ; i < 6; i++)
        {
            for(int y = 0; y < resources[i]; y++)
            {
                if(count > 5)
                {
                    throw new RuntimeException();
                    //If color index > 5 total resources in resource list > max resource number
                    //FIXME create proper error message
                }
                this.resourceDisplay[count].type = i;
                count ++;
            }
        }
        while (count < 6)
        {
            this.resourceDisplay[count].type = -1;
            count++;
        }
        for(ResourcePiece r : resourceDisplay)
        {
            r.updateColor();
        }

    }




    /*
    Breaks the board state into section of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score] and stores it in respective places
    then does checks to see if board state is valid
     */

    //FIXME are we supposed to have definitions of @param and @return for each method we create
    public boolean loadBoard(String boardState)
    {
        int index = 0;
        boolean winner = false;
        boolean hasLongestRoad = false;
        boolean hasLargestArmy = false;
        this.players = new Player[playerCount];
        for(int i = 0; i < playerCount; i++)
        {
            this.players[i] = new Player(names[i],i);
        }

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
                    //FIXME if its acceptable to check by try catch is it okay to simplify this as if the position isn't between 0 and 54 it would create an error
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
    // rebuilds board string of [ID],[# Dice],[Rolls Done],[Resources],[ID],[Placement],[Score]
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(this.playerTurn.playerID);
        output.append(this.numDice);
        output.append(this.rollsDone);
        Integer[] roadSort = new Integer[this.roadsMap.keySet().size()];
        for(int i = 0; i < 6; i++)
        {
            output.append(resourceArray.substring(i,i+1).repeat(this.resources[i]));
        }
        for(int i = 0; i < this.playerCount; i++)
        {
            output.append(this.players[i].playerID);
            for(Piece c : castles)
            {
                if(c.owner == this.players[i])
                {
                    output.append(c);
                }
            }
            //Knight
            for(Piece k : knights)
            {
                if(k.owner == this.players[i] && k.type == PieceType.KNIGHT)
                {
                    output.append(k);
                }
            }

            //UsedKnight
            for(Piece k : knights)
            {
                if(k.owner == this.players[i] && k.type == PieceType.USEDKNIGHT)
                {
                    output.append(k);
                }
            }

            //ROADS
            this.roadsMap.keySet().toArray(roadSort);
            Arrays.sort(roadSort);
            for(int j : roadSort) {
                if (this.roadsMap.get(j).owner  == this.players[i]) {
                    output.append("R");
                    output.append(String.format("%4d", j).replace(' ', '0'));
                }
            }
            //SETTLEMENTS
            for(Piece s : settlements)
            {
                if(s.type == PieceType.SETTLEMENT && s.owner == this.players[i])
                {
                    output.append(s);
                }
            }
            //CITIES
            for(Piece t : settlements)
            {
                if(t.type == PieceType.CITY && t.owner == this.players[i])
                {
                    output.append(t);
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
