package comp1140.ass2;
import com.sun.javafx.logging.PlatformLogger;

import java.sql.SQLOutput;
import java.util.*;

import static comp1140.ass2.Board.*;
import static comp1140.ass2.HexType.BRICK;
import static comp1140.ass2.HexType.WILD;

public class CatanDiceExtra {
    //Required resources to build objects

    /**
     * Check if the string encoding of a board state is well-formed.
     * Note that this does not mean checking if the state is valid
     * (represents a state that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
     *
     * A description of the board state string will be provided in a
     * later update of the project README.
     *
     * @param boardState: The string representation of the board state.
     * @return true iff the string is a well-formed representation of
     * a board state, false otherwise.
     */

    //isBoardStateWellFormed created by Sam Liersch u7448311
    public static boolean isBoardStateWellFormed(String boardState) {
        //Create a model board from the board string and check if its valid
        Board board = new Board(0,0);
        return  board.loadBoard(boardState);
    }


    public static boolean isActionWellFormed(String actionString)
    {
        Action action = new Action();
        return loadAction(actionString,action);
    }


    //loadActionValid created by Sam Liersch u7448311
    //Loads action into action class
    public static boolean loadAction(String actionString, Action action)
    {
        //Required resources for building
        int[][] reqResources = {
            {0,-1,0,0,-1,-1},//Knight
            {-1,0,-1,0,0,0,},//Road
            {-1,-1,-1,0,0,-1},//Settlement
            {0,-2,0,0,-3,0}};//City


        //Check if action has type
        if(actionString.length() < 4) return false;
        String type;
        String actionSubject;
        //Swap is the only 4 letter action all other actions are 5 letters
        if(actionString.charAt(3) == 'p')
        {
            actionSubject = actionString.substring(4);
            type = actionString.substring(0,4);
        }
        else
        {
            actionSubject = actionString.substring(5);
            type = actionString.substring(0,5);
        }
        ActionType actionType = ActionType.fromString(type);
        if(actionType == null) return false;
        int pos1;
        int pos2;
        char lastChar =' ';
        int[] resourceArray = new int[] {0,0,0,0,0,0};
        switch (actionType)
        {
            case KEEP -> {
                //Check if correct length
                if (actionSubject.length() > 6) return false;
                action.type = ActionType.KEEP;
                if(actionSubject.length() > 0)
                {
                    lastChar = actionSubject.charAt(0);
                }
                for(char c : actionSubject.toCharArray())
                {
                    //If resources are not in order
                    if((int) lastChar > (int) c)
                    {
                        return false;
                    }
                    //if resources is invalid
                    if(Board.resourceArray.indexOf(c) < 0)
                    {
                        return false;
                    }
                    resourceArray[Board.resourceArray.indexOf(c)] ++;
                    lastChar = c;
                }
                action.resourceArray = resourceArray;
            }
            case BUILD -> {

                action.type = ActionType.BUILD;
                switch (actionSubject.charAt(0))
                {
                    case 'R' -> {
                        //Road
                        action.pieceType = PieceType.ROAD;
                        if(actionSubject.length()!= 5)
                        {
                            return false;
                        }
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        pos2 = Integer.parseInt(actionSubject.substring(3,5));
                        //Check if road is in correct format and within the index bounds
                        if(!(pos1 < pos2 && pos1 >=0 && pos2 < 54))
                        {
                            return false;
                        }
                        action.pieceIndex = Integer.parseInt(actionSubject.substring(1,5));
                        action.resourceArray = reqResources[1];
                    }
                    case  'C' -> {
                        //Castle
                        action.pieceType = PieceType.CASTLE;
                        if(actionSubject.length() != 2) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,2));
                        if(!(pos1 >= 0 && pos1 < 4)) return false;
                        action.pieceIndex = pos1;
                        //Potential bug, ReqResources for castle can change depending on available resources
                        //And so resource req is calculated when action is applied
                        action.resourceArray = resourceArray;
                    }
                    case 'S' -> {
                        //Settlement
                        action.pieceType = PieceType.SETTLEMENT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        action.pieceIndex = pos1;
                        action.resourceArray = reqResources[2];
                    }
                    case 'T' -> {
                        //City
                        action.pieceType = PieceType.CITY;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        action.pieceIndex = pos1;
                        action.resourceArray = reqResources[3];
                    }
                    case 'K' -> {
                        //Knight
                        action.pieceType = PieceType.KNIGHT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 20)) return false;
                        action.pieceIndex = pos1;
                        action.resourceArray = reqResources[0];
                    }
                    default ->
                    {
                        return false;
                    }
                }
            }
            case TRADE -> {
                action.type = ActionType.TRADE;
                lastChar = actionSubject.charAt(0);
                //Check resources are in order
                for(char r : actionSubject.toCharArray())
                {
                    if((int) lastChar > r) return false;
                    //Cannot trade money for money
                    if(r == 'm') return false;
                    //Ensure resource is of the valid type
                    if(Board.resourceArray.indexOf(r) == -1) return false;
                    resourceArray[Board.resourceArray.indexOf(r)]++;
                    resourceArray[3] -= 2;

                }
                action.resourceArray = resourceArray;
            }
            case SWAP -> {

                action.type = ActionType.SWAP;

                //Can only swap 1 resource for 1 resource
                if (actionSubject.length() != 2) return false;
                //Can't Swap 1 resource for the same resource
                if(actionSubject.charAt(0) == actionSubject.charAt(1)) return false;
                //Check if it's a valid resource
                if(Board.resourceArray.indexOf(actionSubject.charAt(0)) == -1) return false;
                if(Board.resourceArray.indexOf(actionSubject.charAt(1)) == -1) return false;

                action.requiredType = HexType.fromChar(actionSubject.charAt(1));
                resourceArray[Board.resourceArray.indexOf(actionSubject.charAt(0))] --;
                resourceArray[Board.resourceArray.indexOf(actionSubject.charAt(1))] ++;
                action.resourceArray = resourceArray;
            }
        }
        return true;
    }



    /**
     * Check if the string encoding of a player action is well-formed.
     * Note that this does not mean checking if the action is valid
     * (represents a player action that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
     *
     * A description of the board state string will be provided in a
     * later update of the project README.
     *
     * @param action: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     * a player action, false otherwise.
     */

    //isActionWellFormed created by Eliz So, u7489812
    public static boolean ActionWellFormed(String action) {

        String resources = "bglmow";

        if (action.length() < 4) return false;

        if (action.substring(0,4).compareTo("keep") == 0){

            if (action.length() == 4) return true;

            char[] sort = action.substring(4).toCharArray();
            char[] res = action.substring(4).toCharArray();
            Arrays.sort(sort);

            if (Arrays.compare(sort, res) != 0){
                return false;
            }

            for (int i = 4; i < action.length(); i++){
                char c = action.charAt(i);
                int index = resources.indexOf(c);
                if (index == -1) return false;
            }

            return true;

        } else if (action.substring(0,4).compareTo("swap")== 0){

            if (action.length() != 6) return false;

            char in = action.charAt(4);
            char out = action.charAt(5);

            return (resources.indexOf(in) != -1 && resources.indexOf(out) != -1);
        }
        if (action.length() < 5) return false;
        else if (action.substring(0, 5).compareTo("build") == 0){

            if (action.length() <= 5) return false;

            char struct = action.charAt(5);
            if (struct == 'R'){

                if (action.length() != 10) return false;

                int fir = Integer.parseInt(action.substring(6,8));
                int sec = Integer.parseInt(action.substring(8,10));

                return (fir < sec && fir >= 0 && sec <= 53);


            } else if (struct == 'C'){

                if (action.length() != 7) return false;
                // 0, 1, 2, 3
                int n = action.charAt(6)-48;
                return (n <= 3);

            } else if (struct == 'S' || struct == 'T') {

                if (action.length() != 8) return false;
                // 00 - 53
                int n = Integer.parseInt(action.substring(6, 8));
                return (n >= 0 && n <= 53);


            } else if (struct == 'K'){

                if (action.length() != 8) return false;
                // 00 - 19
                int n = Integer.parseInt(action.substring(6,8));
                return (n >= 0 && n <= 19);

            }

        }else if (action.substring(0,5).compareTo("trade") == 0){


            if (action.length() == 5) return false;

            char[] sort = action.substring(5).toCharArray();
            char[] res = action.substring(5).toCharArray();
            Arrays.sort(sort);

            if (Arrays.compare(sort, res) != 0){
                return false;
            }

            for (int i = 5; i < action.length(); i++){
                char c = action.charAt(i);
                if (c == 'm') return false;
                if (resources.indexOf(c) == -1) return false;
            }

            return true;

        }
	    return false;

    }

    /**
     * Roll the specified number of *random* dice, and return the
     * rolled resources in string form.
     * The outcomes of dice rolls should be uniformly distributed.
     *
     * @param numOfDice the number of dices to roll
     * @return alphabetically ordered [Resources] with characters
     * 'b', 'l', 'w', 'g', 'o', 'm'.
     */
    //rollDice created by Eliz So, u7489812
    public static String rollDice(int numOfDice) {

        if (numOfDice < 1 || numOfDice > 6) return null;

        Random rand = new Random();
        char[] resources = new char[]{'b', 'l', 'w', 'g', 'o', 'm'};
        char[] output = new char[numOfDice];

        for (int i = 0; i < numOfDice; i++){
            output[i] = resources[rand.nextInt(6)];
        }

        Arrays.sort(output);

        return new String(output);
    }

    /**
     * Given a valid board state and player action, determine whether the
     * action can be executed.
     * The permitted actions depend on the current game phase:
     *
     * A. Roll Phase (keep action)
     * 1. A keep action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "keep[Resources]", and the
     *   current player has the resources specified.
     * - [Rolls Done] is less than 3
     *
     *
     * B. Build Phase (build, trade, and swap actions)
     *
     * 1. A build action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "build[Structure Identifier]"
     * - The current player has sufficient resources available for building
     *   the structure.
     * - The structure satisfies the build constraints (is connected to the
     *   current players road network).
     * - See details of the cost of buildable structure in README.md.
     *
     * 2. A trade action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "trade[Resources]"
     * - The current player has sufficient resources available to pay for
     *   the trade.
     *
     * 3. A swap action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "swap[Resource Out][Resource In]"
     * - The current player has sufficient resources available to swap out.
     * - The current player has an unused knight (resource joker) on the
     *   board which allows to swap for the desired resource.
     * @param boardState: string representation of the board state.
     * @param action: string representation of the player action.
     * @return true iff the action is executable, false otherwise.
     */
    // isActionValid created by Thomas Daniel, u7490675
    public static boolean ActionValid(String boardState, String action) {
            boolean result = false;
            boolean p1 = (boardState.indexOf("W") == 0);  // checks what players turn
            String[] inland = new String[]{"08","12","17","22","28","34","39","44","40","45","41",
                    "36","31","25","19","14","09","13","18","23","29","35","30","24"};
            String[] buildings = new String[]{"00","01","02","07","08","09","10","16","17","18","19","20","33",
                    "34","35","36","37","43","44","45","46","51","52","53"};
            String[] woodKnights = new String[]{"K05","K08","K15","J09","J10","J05","J08","J15","K09","K10"};
            String[] woolKnights = new String[]{"K06","K00","K19","K13","J09","J10","J06","J00","J19","J13","K09","K10"};
            String[] oreKnights = new String[]{"K02","K03","K16","K17","J09","J10","J02","J03","J16","J17","K09","K10"};
            String[] grainKnights = new String[]{"K01","K07","K12","K18","J09","J10","J01","J07","J12","J18","K09","K10"};
            String[] brickKnights = new String[]{"K04","K11","K14","J09","J10","J04","J11","J14","K09","K10"};
            String[] jokerKnights = new String[]{"J09","J10","K09","K10"};
            String wString = boardState.substring(4,boardState.indexOf("X", 1));
            String xString = boardState.substring(boardState.indexOf("X")+1);
            if (boardState.substring(1,3).compareTo("00") == 0) { // condition for set up phase
                if (action.substring(0, 6).compareTo("buildR") == 0){ // condition for Building road
                    if (boardState.contains(action.substring(6,8)) || (boardState.contains(action.substring(8,10)))){
                    } else {
                        int cor1 = Integer.parseInt(action.substring(6,8));
                        int cor2 = Integer.parseInt(action.substring(8,10));
                        List<String> nameList = new ArrayList<>(Arrays.asList(inland));
                        if (cor2 - cor1 <= 6 && cor2 - cor1 > 2 ){
                            if (nameList.contains(action.substring(6,8)) || nameList.contains(action.substring(8,10))){
                            } else{
                                result = true;
                            }
                        }
                    }
                }
            } else {
                char[] resources = boardState.substring(3, boardState.indexOf("W", 1)).toCharArray();
                if (action.substring(0, 4).compareTo("keep") == 0 && Integer.parseInt(String.valueOf(boardState.charAt(2))) < 3) {
                    char[] req = action.substring(4).toCharArray();
                    int count = 0;
                    int start = 0;
                    for (int i = 0; i < resources.length; i++) {
                        if (action.indexOf(resources[i], start) != -1) {
                            count++;
                            start = action.indexOf(resources[i]) + 1;
                        }
                    }
                    result = count >= req.length;
                } else if (action.substring(0, 5).compareTo("build") == 0) { // Build action condition
                    if (action.charAt(5) == 'R') {
                        int cor1 = Integer.parseInt(action.substring(6,8));
                        int cor2 = Integer.parseInt(action.substring(8,10));
                        boolean conditions = cor2 - cor1 <= 6 && cor2 - cor1 > 2 && canBuild(boardState, "bl");
                        List<String> buildList = new ArrayList<>(Arrays.asList(buildings));
                        if (p1) { // if player 1 check valid for wStrings, else xStrings
                            if (wString.contains(action.substring(6, 8)) || (wString.contains(action.substring(8, 10)))){
                                if (buildList.contains(action.substring(6, 8))) {
                                    if (wString.contains(action.substring(6, 8)) && ! wString.contains(action.substring(8, 10))) {
                                        result = conditions && (wString.contains("S" + action.substring(6, 8)) || wString.contains("T" + action.substring(6, 8)));
                                    } else result = conditions;
                                } else if (buildList.contains(action.substring(8, 10))) {
                                    if (wString.contains(action.substring(8, 10)) && ! wString.contains(action.substring(6, 8))) {
                                        result = conditions && (wString.contains("S" + action.substring(8, 10)) || wString.contains("T" + action.substring(8, 10)));
                                    } else result = conditions;
                                } else result = conditions;
                            }} else if (xString.contains(action.substring(6, 8)) || (xString.contains(action.substring(8, 10)))) {
                            if (buildList.contains(action.substring(6, 8))) {
                                if (xString.contains(action.substring(6, 8)) && ! xString.contains(action.substring(8, 10))) {
                                    result = conditions && (xString.contains("S" + action.substring(6, 8)) || xString.contains("T" + action.substring(6, 8)));
                                } else result = conditions;
                            } else if (buildList.contains(action.substring(8, 10))) {
                                if (xString.contains(action.substring(8, 10)) && ! xString.contains(action.substring(6, 8))) {
                                    result = conditions && (xString.contains("S" + action.substring(8, 10)) || xString.contains("T" + action.substring(8, 10)));
                                } else result =  conditions;
                            } else result = conditions;
                        }}
                    if (action.charAt(5) == 'K') { // building knight
                        result = canBuild(boardState, "gow");
                    }
                    if (action.charAt(5) == 'T') { // building city
                        result = canBuild(boardState, "ggooo");
                    }
                    if (action.charAt(5) == 'S') { // building settlement
                        result = canBuild(boardState, "bglw");
                    }
                } else if (action.substring(0, 5).compareTo("trade") == 0) {
                    result = canBuild(boardState, "mm".repeat(action.length() - 5));

                } else if (action.substring(0, 4).compareTo("swap") == 0) {
                    boolean valid = false;
                    char c = action.charAt(5);
                    if (p1) {
                        switch (c) {
                            case 'b' ->  valid = hasKnight(wString,brickKnights) ;
                            case 'l' ->  valid = hasKnight(wString,woodKnights);
                            case 'g' ->  valid = hasKnight(wString,grainKnights);
                            case 'o' ->  valid = hasKnight(wString,oreKnights);
                            case 'w' ->  valid = hasKnight(wString,woolKnights);
                            case 'm' ->  valid = hasKnight(wString,jokerKnights);
                        }
                    } else {
                        switch (c) {
                            case 'b' ->  valid = hasKnight(xString,brickKnights) ;
                            case 'l' ->  valid = hasKnight(xString,woodKnights);
                            case 'g' ->  valid = hasKnight(xString,grainKnights);
                            case 'o' ->  valid = hasKnight(xString,oreKnights);
                            case 'w' ->  valid = hasKnight(xString,woolKnights);
                            case 'm' ->  valid = hasKnight(xString,jokerKnights);
                        }}
                    result = valid && canBuild(boardState, String.valueOf(action.charAt(4)));
                }
            }return result;
        }
    // canBuild created by Thomas Daniel, u7490675
        public static boolean canBuild(String boardState, String needs) {
            boolean buildable;
            char[] resources = boardState.substring(3, boardState.indexOf("W", 1)).toCharArray();
            char[] req = needs.toCharArray();
            int count = 0;
            int start = 0;
            for (int i = 0; i < resources.length; i++){
                if (needs.indexOf(resources[i], start) != -1){
                    count++;
                    start = needs.indexOf(resources[i])+1;
                }
            }
            buildable = count >= req.length;
            return buildable;
        }
    // hasKnight created by Thomas Daniel, u7490675
        public static boolean hasKnight(String string, String[] knights) {
            boolean found = false;
            for (String knight : knights) {
                if (string.contains(knight)) {
                    found = true;
                    break;
                }
            }
            return found;
        }



        public static boolean isActionValid(String boardState, String actionState)
        {
            Board board = new Board(0,0);
            Action action = new Action();
            //Check board state is wellformed
            if(!board.loadBoard(boardState))
            {
                return false;
            }
            //Check action is wellformed

            if(!loadAction(actionState, action))
            {
                return false;
            }
            //Check action is valid
            if(!isActionValid(board,action))
            {
                return false;
            }
            return true;
        }

    //isActionValid created by Sam Liersch u7448311
    // is Action valid relies on Board  and Action class instead of strings
    public static boolean isActionValid(Board board, Action action) {

        int pos1;
        int pos2;

        //SetupPhase
        if(board.setupPhase)
        {
            int index;
            int roadIndex;
            if(action.type != ActionType.BUILD) return false;
            if(action.pieceType != PieceType.ROAD) return false;
            pos1 = action.pieceIndex % 100;
            pos2 = action.pieceIndex / 100;

            if(!Board.coastRoads.contains(pos1)) return false;
            if(!Board.coastRoads.contains(pos2)) return false;
            if(Math.abs(Board.coastRoads.indexOf(pos1) - Board.coastRoads.indexOf(pos2)) > 1) return false;

            //FIXME how do you properly wrap around
            //check no road is within 5 pieces clockwise
            index = Math.max(Board.coastRoads.indexOf(pos1),Board.coastRoads.indexOf(pos2));
            if(pos1 == 0 && pos2 == 29)
            {
                index = 0;
            }
            for(int y = 0; y < 5; y++)
            {

                roadIndex = Math.min(Board.coastRoads.get(index % 30),Board.coastRoads.get((index + 1) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get(index % 30),Board.coastRoads.get((index + 1) % 30));
                if(!board.roadsMap.containsKey(roadIndex)) return false;
                if(board.roadsMap.get(roadIndex).owner != null)
                {
                    return false;
                }
                index++;
            }
            //check no road is within 5 pieces AntiClockwise
            index = Math.max(Board.coastRoads.indexOf(pos1),Board.coastRoads.indexOf(pos2));
            if(pos1 == 0 && pos2 == 29)
            {
                index = 29;
            }
            index = index + 30;
            for(int y = 0; y < 5; y++)
            {
                roadIndex = Math.min(Board.coastRoads.get((index - 1) % 30),Board.coastRoads.get((index - 2  + 30) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get((index - 1) % 30),Board.coastRoads.get((index - 2 + 30) % 30));
                if(!board.roadsMap.containsKey(roadIndex)) return false;
                if(board.roadsMap.get(roadIndex).owner != null)
                {
                    return false;
                }
                index--;
            }
            return true;

        }

        //Roll phase
        if(board.rollsDone < 3)
        {
            for(int i = 0; i < 6; i++)
            {
                if(board.resources[i] < action.resourceArray[i])
                {
                    return false;
                }
            }

            return (action.type == ActionType.KEEP);
        }
        //Used if multipe check are needed
        boolean flag;

        //Build phase
        switch (action.type)
        {
            case BUILD -> {
                //First Check if req resources are met
                //Also Check if no player has already built on piece
                switch (action.pieceType)
                {
                    case CASTLE -> {
                        flag = false;

                        for(int i = 0; i < 6; i ++)
                        {
                            if(board.resources[i] > 5)
                            {
                                flag = true;
                                break;
                            }
                        }
                        if(!flag) return false;
                        if(board.castles[action.pieceIndex].owner != null) return false;
                    }
                    case KNIGHT -> {
                        if(!hasMaterials(board.resources,action.resourceArray)) return false;
                        if(board.knights[action.pieceIndex].owner != null) return false;
                        flag = false;
                        //Check at least 1 piece surrounding knight hex is built by player
                        for(Hex[] hexArray : board.hexes)
                        {
                            for(Hex hex : hexArray)
                            {
                                if(hex != null &&
                                        (hex.index == action.pieceIndex
                                                || (hex.index == 9 && action.pieceIndex == 10)))
                                {
                                    for(Piece piece : hex.settlement)
                                    {
                                        if(piece.owner == board.playerTurn)
                                        {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    for(Piece road : hex.roads)
                                    {
                                        if(road.owner == board.playerTurn)
                                        {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(!flag) return false;
                                    break;
                                }
                            }
                        }
                    }
                    case ROAD -> {

                        if(!hasMaterials(board.resources,action.resourceArray)) return false;
                        if(board.roadsMap.get(action.pieceIndex).owner != null) return false;
                        pos1 = action.pieceIndex % 100;
                        pos2 = action.pieceIndex / 100;

                        int rPos1; //Index of the road being compared
                        int rPos2;
                        flag = false;
                        Piece[] roads = board.roadsMap.values().toArray(new Piece[0]);
                        //Check if there is a settlement at the end of a road or just another road
                        boolean pos1Settlement = board.settlements[pos1].maxLevel !=0;
                        boolean pos2Settlement = board.settlements[pos2].maxLevel !=0;
                        //If either of the points don't contain a settlement check for road on that point
                        if(!pos1Settlement || !pos2Settlement)
                        {
                            for(Piece road : roads)
                            {
                                rPos1 = road.boardIndex % 100;
                                rPos2 = (int)road.boardIndex / 100;
                                if(road.owner == board.playerTurn && road.boardIndex != action.pieceIndex)
                                {
                                    if(!pos1Settlement && (rPos1 == pos1 || rPos2 == pos1))
                                    {
                                        flag = true;
                                        break;
                                    }
                                    if(!pos2Settlement && (rPos1 == pos2 || rPos2 == pos2))
                                    {
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(!flag && (board.settlements[pos1].owner == board.playerTurn
                                || board.settlements[pos2].owner == board.playerTurn))
                        {
                            flag = true;
                        }
                        if(!flag)
                        {
                            return false;
                        }
                    }
                    case SETTLEMENT -> {
                        if(!hasMaterials(board.resources,action.resourceArray)) return false;
                        if(board.settlements[action.pieceIndex].owner != null) return false;
                        if(board.settlements[action.pieceIndex].maxLevel == 0) return false;
                        Piece[] roads = board.roadsMap.values().toArray(new Piece[0]);
                        flag = false;
                        for(Piece road : roads)
                        {
                            pos1 = road.boardIndex % 100;
                            pos2 = (int)road.boardIndex / 100;
                            if(road.owner == board.playerTurn
                                    && (pos1 == action.pieceIndex || pos2 == action.pieceIndex))
                            {
                                flag = true;
                                break;
                            }
                        }
                        if(!flag) return false;
                    }
                    case CITY -> {
                        if(!hasMaterials(board.resources,action.resourceArray)) return false;
                        if(board.settlements[action.pieceIndex].owner != board.playerTurn) return false;
                    }
                }

            }
            case TRADE -> {
                if(!hasMaterials(board.resources,action.resourceArray)) return false;
            }
            case SWAP -> {
                if(!hasMaterials(board.resources,action.resourceArray)) return false;
                flag = false;
                //Check if correct knight is owned for trade
                for(Piece knight : board.knights)
                {
                    if(knight.owner == board.playerTurn
                            && knight.type == PieceType.KNIGHT
                            && (hexTypeArray[knight.boardIndex] == action.requiredType
                            || hexTypeArray[knight.boardIndex] == HexType.WILD))
                    {
                        flag = true;
                        break;
                    }
                }
                if(!flag) return false;
            }
        }

        return true;
    }

    // hasMaterials created by Sam Liersch u7448311
    //Returns whether the required materials are available
    public static boolean hasMaterials(int[] available, int[] required)
    {
        if(available.length != required.length) return false;
        //Check player has the resources to keep
        for(int i = 0; i < available.length; i++)
        {
            if(available[i] + required[i] < 0)
            {
                return false;
            }
        }
        return true;
    }



    /**
     * Return an integer array containing the length of the longest contiguous
     * road owned by each player.
     * For example : given [Board State] =
     * "W61bgglwwWJ05K01K02R0105R0205R0206R0509R0610R0913R1015S02S09S10XK12R2026R2632R3137R3237R3742S37W07RAX01"
     * - Player 'W' has the longest road length of 6
     * - Player 'X' has the longest road length of 4
     * - The method should return {6, 4}
     * @param boardState: string representation of the board state.
     * @return array of contiguous road lengths, one per player.
     */



    public static int[] longestRoad(String boardState){
        //Height and width can be 0 as we are not looking at the board
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        return longestRoad(board);
    }
    // longest Road created by Sam Liersch u7448311
    public static int[] longestRoad(Board board) {
        int[] output = new int [board.playerCount];
        List<Integer> visited = new ArrayList<>();
        List<Integer> roads = new ArrayList<>();
        //Iterate through all owned roads and find the longest road from that road
        for(int i = 0; i < board.playerCount; i++)
        {
            for(Integer y : board.roadsMap.keySet())
            {
                if(board.roadsMap.get(y).owner == board.players[i])
                {
                    roads.add(y);
                }
            }
            output[i] = findMaxRoad(roads,visited,-1);
            roads.clear();
        }
        return output;
    }

    // findMaxRoad created by Sam Liersch u7448311
    // Given a list of roads owned by a player finds the road with the max length
    private static int findMaxRoad(List<Integer> roads, List<Integer> visited,int lastPos)
    {
        //FIXME Im using lastPos -1 to start the recursion is this bad practice?
        int max = 0;
        Integer[] posArray = roads.toArray(new Integer[0]);
        for(Integer i : posArray)
        {
            if(i/100 == lastPos || lastPos == -1)
            {
                roads.remove(i);
                visited.add(i);
                max = Math.max(max, findMaxRoad(roads,visited,i%100));
                roads.add(i);
                visited.remove(i );

            }
            if(i%100 == lastPos|| lastPos == -1)
            {
                roads.remove(i);
                visited.add(i);
                max = Math.max(max, findMaxRoad(roads,visited,i/100));
                roads.add(i);
                visited.remove(i);

            }
        }
        return Math.max(max,visited.size());
    }

    /**
     * Return an integer array containing the size of the army owned by
     * each player.
     * For example : given [Board State] =
     * "W61bgglwwWJ05K01K02R0105R0205R0206R0509R0610R0913R1015S02S09S10XK12R2026R2632R3137R3237R3742S37W07RAX01"
     * - Player 'W' has an army of size 3
     * - Player 'X' has an army of size 1
     * - The method should return {3, 1}
     * @param boardState: string representation of the board state.
     * @return array of army sizes, one per player.
     */

    // largestArmy created by Sam Liersch u7448311
    public static int[] largestArmy(String boardState){
        //Height and width can be 0 as we are not looking at the board
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        return largestArmy(board);
    }
    public static int[] largestArmy(Board board) {

        int[] output = new int [board.playerCount];
        //counts the number of knights owned by a player
        for(int i = 0; i < board.playerCount; i ++)
        {
            output[i] = 0;
            for(Piece p : board.knights)
            {
                //FIXME does the first statement always process before the second one? if owner is null indexOf will give an error
                if(p.owner != null && "WXYZ".indexOf(p.owner.playerID) == i)
                {
                    output[i] ++;
                }
            }
        }
        return output;
    }

    /**
     * Given a valid board state and player action, this method should return
     * the next new board state that results from executing the action.
     * This method should both handle Start of the Game, Middle of the Game,
     * and Game End.
     *
     * A. Start of the Game
     * For example : given boardState = "W00WXW00X00", action = "buildR0205"
     * - Player 'W' builds a road from index 02 to 05
     * - The next boardState should be "X00WR0205XW00X00"
     * - Consult details of the rules for the Start of the Game in README.md
     *
     * B. Middle of the Game
     * For example : given boardState = "W61bbbgwwWR0205R0509S02XR3237W01X00", action = "keepbbbw"
     * - Player 'W' keeps three brick and one wool, and re-rolls two dice.
     * - The next boardState should be "W62[Next Resources]WR0205R0509S02XR3237W01X00"
     * - [Next Resources] can be any 6 resources that contain 3 bricks, 1 wool
     * - Some examples of [Next Resources] are "bbbbmw", "bbbglw", "bbblow", etc
     *
     * C. Game End
     * For example : given boardState = "X63lmoWK01K02K04K05K06R0105R0205R0206R0408R0509R0610R0812R0813R0913R0914R1014R1015R1318R1419R1520S01S02S08S09T10XJ09K10K11K12K15K16R1824R1924R1925R2025R2026R2430R2531R2632R3035R3036R3136R3137R3237R3641R3742R4145R4146R4246R4549S19S20S37S45T36W06X10RA"
     * - Player 'X' got the score 10 and game ended
     * - No action can be applied at this stage
     * @param boardString: string representation of the board state.
     * @param actionString: string representation of the player action.
     * @return string representation of the updated board state.
     */
    public static String applyAction(String boardString, String actionString) {
        Board  board = new Board(0,0);
        Action action = new Action();
        board.loadBoard(boardString);
        loadAction(actionString,action);

        applyAction(board,action);
        return board.toString();
    }

    public static void  applyAction(Board board, Action action) {
        // If action is not valid do not apply move
        Player lArmyHolder = null;
        Player lRoadHolder = null;
        for (Player p : board.players) {
            if (p.longestRoad) {
                lRoadHolder = p;
            }
            if (p.largestArmy) {
                lArmyHolder = p;
            }
        }

        if (board.setupPhase) {
            board.roadsMap.get(action.pieceIndex).owner = board.playerTurn;
            return;
        }
        switch (action.type) {
            case KEEP -> {
                int count = 0;
                board.resources = new int[]{0, 0, 0, 0, 0, 0};
                Random rand = new Random();

                for (int i = 0; i < 6; i++) {
                    count += action.resourceArray[i];
                    board.resources[i] += action.resourceArray[i];
                }
                for (int i = count; i < board.numDice; i++) {
                    board.resources[rand.nextInt(6)]++;
                }
                board.rollsDone ++;
            }
            case BUILD -> {
                switch (action.pieceType) {
                    case ROAD -> {
                        board.roadsMap.get(action.pieceIndex).owner = board.playerTurn;
                    }
                    case SETTLEMENT -> {
                        board.playerTurn.score++;
                        board.settlements[action.pieceIndex].owner = board.playerTurn;
                    }
                    case CASTLE -> {
                        board.playerTurn.score += 2;
                        board.castles[action.pieceIndex].owner = board.playerTurn;
                        for (int i = 0; i < 6; i++) {
                            if (board.resources[i] > 5) {
                                action.resourceArray[i] = -6;
                            }
                        }
                    }
                    case CITY -> {
                        board.playerTurn.score++;
                        board.settlements[action.pieceIndex].type = PieceType.CITY;
                    }
                    case KNIGHT -> {
                        board.knights[action.pieceIndex].owner = board.playerTurn;
                    }
                }
                for (int i = 0; i < 6; i++) {
                    board.resources[i] += action.resourceArray[i];
                }
            }
            case TRADE -> {
                for (int i = 0; i < 6; i++) {
                    board.resources[i] += action.resourceArray[i];
                }
            }
            // FIXME: swap characters for USEDKNIGHTS and KNIGHTS
            case SWAP -> {
                for (Piece knight : board.knights) {
                    if (knight.type == PieceType.KNIGHT
                            && knight.owner == board.playerTurn
                            && action.requiredType == hexTypeArray[knight.boardIndex]) {
                        //If right knight type is found edit resources and set knight to used knight
                        for (int i = 0; i < 6; i++) {
                            board.resources[i] += action.resourceArray[i];
                        }
                        knight.type = PieceType.USEDKNIGHT;
                        return;
                    }
                }
                //If no correct knight type is found check for wild type knights
                for (Piece knight : board.knights) {
                    if (knight.type == PieceType.KNIGHT
                            && knight.owner == board.playerTurn
                            && action.requiredType == WILD) {
                        //If right knight type is found edit resources and set knight to used knight
                        for (int i = 0; i < 6; i++) {
                            board.resources[i] += action.resourceArray[i];
                        }
                        knight.type = PieceType.USEDKNIGHT;
                        return;
                    }
                }
                return;
            }
        }
        if(action.type == ActionType.BUILD && action.pieceType == PieceType.ROAD)
        {
            int[] roadLength = longestRoad(board);
            //Set longest road min to 4 as players have to have at least a road of length 5
            int longest = 4;
            if(lRoadHolder != null)
            {
                longest = roadLength[lRoadHolder.playerIndex];
            }
            Player maxRoadPlayer = null;
            for(int i = 0; i < roadLength.length; i++)
            {
                if(longest < roadLength[i])
                {
                    maxRoadPlayer = board.players[i];
                    longest = roadLength[i];
                }
            }
            if(maxRoadPlayer != null)
            {
                if(lRoadHolder != null)
                {
                    lRoadHolder.longestRoad = false;
                    lRoadHolder.score -= 2;
                }
                maxRoadPlayer.score += 2;
                maxRoadPlayer.longestRoad = true;
            }
        }
        if(action.type == ActionType.BUILD && action.pieceType == PieceType.KNIGHT)
        {
            int[] armyCount = largestArmy(board);
            //Largest army min is 3, only set player to have the largest army if they have an army size of 3 or greater
            int largest = 2;
            if(lArmyHolder != null)
            {
                largest = armyCount[lArmyHolder.playerIndex];
            }

            Player maxArmyPlayer = null;
            for(int i = 0; i < armyCount.length; i++)
            {
                if(largest < armyCount[i])
                {
                    maxArmyPlayer = board.players[i];
                    largest = armyCount[i];
                }
            }
            if(maxArmyPlayer != null)
            {
                if(lArmyHolder != null)
                {
                    lArmyHolder.largestArmy = false;
                    lArmyHolder.score -= 2;
                }
                maxArmyPlayer.score += 2;
                maxArmyPlayer.largestArmy = true;
            }
        }
    }



    /**
     * Given valid board state, this method checks if a sequence of player
     * actions is executable.
     * For example : given boardState = "W63bbglowWR0205R0509S02XR3237W01X00", actionSequence = {"buildK02","swapbo","buildR0105"}
     * - Player 'W' has resources available to build a knight at index 02 using 1 ore, 1 wool and 1 grain
     * - Player 'W' has resources available to swap 1 brick for 1 ore, using the knight
     * - Player 'W' has resources available to build a road at index 01 to 05 using 1 brick and 1 lumber
     * @param boardState: string representation of the board state.
     * @param actionSequence: array of strings, each representing one action
     * @return true if the sequence is executable, false otherwise.
     */
    public static boolean isActionSequenceValid(String boardState, String[] actionSequence) {
        // FIXME: Task 10a
        return false;
    }

    /**
     * Given a valid board state and a sequence of player actions, this
     * method returns the new board state after executing the sequence of
     * actions. You can assume that the sequence is executable.
     * For example : given boardState = "W63bbglowWR0205R0509S02XR3237W01X00", actionSequence = "buildK02","swapbo","buildR0105"
     * - The next boardState should be "X61[Next Resources]WK02R0105R0205R0509S02XR3237W01X00"
     * - Player 'W' knight at index 02 is built
     * - Player 'W' swaps a resource and the knight becomes used
     * - Player 'W' built road R0105
     * - Player 'W' turn ends and the current player becomes 'X'
     * - [Next Resources] can be any of 6 resources of player 'X'
     * @param boardState: string representation of the board state
     * @param actionSequence: array of strings, each representing one action
     * @return string representation of the new board state
     */
    public static String applyActionSequence(String boardState, String[] actionSequence) {
        // FIXME: Task 10b
        return null;
    }

    /**
     * Given a valid board state, return all applicable player action sequences.
     * The method should return an array of sequences, where each sequence is
     * an array of action string representations.
     *
     * If the current phase of the game is the Start of Game phase, each of
     * the sequences should contain just one road building action (that is
     * a permitted initial road for the player).
     *
     * If the current phase of the game is the Roll phase, each of the
     * sequences should contain just one action, specifying a possible
     * next roll (i.e., resources to keep).
     *
     * If the current phase is the Build phase, the sequences should be all
     * non-redundant sequences of trade, swap and build actions that the
     * player can take.
     *
     * In this context, an action sequence is considered non-redundant if
     * 1. All resources gained through trade and swap actions are totally used.
     *    i.e. the turn finishes with 0 of that resource.
     * 2. A trade action occurs at most once during the action sequence.
     * 3. Gained resources through the trade and swap actions are not later
     *    traded/swapped away.
     * 4. The empty sequence is always non-redundant (i.e. the player takes no
     *    action).
     *
     * Note, there are other sources of redundancy in action sequences besides the
     * ones that are listed here. One of the more noteworthy ones is the ordering of
     * actions within a sequence whereby two different action sequences may result
     * in the same state when applied. While this is not relevant for this task, it
     * may prove useful to consider this for your "smart" game AI in task 14.
     *
     * In the build phase, one of the possible sequences is always to end
     * the player's turn without taking any action, i.e., an empty sequence.
     *
     * The order of the action sequences in the return array is unspecified,
     * i.e., does not matter. (Of course, the order of actions within each
     * sequence does matter.)
     *
     * @param boardState: string representation of the current board state.
     * @return array of possible action sequences.
     */
    public static String[][] generateAllPossibleActionSequences(String boardState) {
        // FIXME: Task 12
        return null;
    }

    /**
     * Given a valid board state, return a valid action sequence.
     *
     * This method is the interface to your game AI. It is given the current
     * state of the game, and should return the sequence of actions your AI
     * chooses to take.
     *
     * An array of length 0 is interpreted as finishing the current turn
     * without taking any further action.
     *
     * @param boardState: string representation of the board state.
     * @return array of strings representing the actions the AI will take.
     */
    public static String[] generateAction(String boardState) {
        // FIXME: Task 13
        // FIXME: Task 14 Implement a "smart" generateAction()
        return null;
    }
}
