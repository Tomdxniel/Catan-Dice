package comp1140.ass2;
import java.sql.SQLOutput;
import java.util.*;
public class CatanDiceExtra {
    //Required resources to build objects
    //Knight Road Settlement City
    public static int[][] reqResources = {
            {0,1,0,0,1,1},
            {1,0,1,0,0,0,},
            {1,1,1,0,0,1},
            {0,2,0,0,3,0}};


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

    public static boolean isBoardStateWellFormed(String boardState) {
        Board board = new Board(700,1200);
        return  board.loadBoard(boardState);
    }



    //Loads action into action class
    public static boolean loadAction(String actionString, Action action)
    {
        //Check if action has type
        if(actionString.length() < 4) return false;
        String type;
        String actionSubject;
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
                        action.pieceType = PieceType.ROAD;
                        if(actionSubject.length()!= 5)
                        {
                            return false;
                        }
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        pos2 = Integer.parseInt(actionSubject.substring(3,5));
                        if(!(pos1 < pos2 && pos1 >=0 && pos2 < 54))
                        {
                            return false;
                        }
                        action.pieceIndex = Integer.parseInt(actionSubject.substring(1,5));
                    }
                    case  'C' -> {
                        action.pieceType = PieceType.CASTLE;
                        if(actionSubject.length() != 2) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,2));
                        if(!(pos1 >= 0 && pos1 < 4)) return false;
                        action.pieceIndex = pos1;
                    }
                    case 'S' -> {
                        action.pieceType = PieceType.SETTLEMENT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        action.pieceIndex = pos1;
                    }
                    case 'T' -> {
                        action.pieceType = PieceType.CITY;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        action.pieceIndex = pos1;
                    }
                    //Why K for knight
                    case 'K' -> {
                        action.pieceType = PieceType.KNIGHT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 20)) return false;
                        action.pieceIndex = pos1;
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
                for(char r : actionSubject.toCharArray())
                {
                    if((int) lastChar > r) return false;
                    if(r == 'm') return false;
                    if(Board.resourceArray.indexOf(r) == -1) return false;
                    resourceArray[Board.resourceArray.indexOf(r)]++;
                    resourceArray[3] -= 2;
                }
                action.resourceArray = resourceArray;
            }
            case SWAP -> {

                action.type = ActionType.SWAP;
                if (actionSubject.length() != 2) return false;
                if(Board.resourceArray.indexOf(actionSubject.charAt(0)) == -1) return false;
                if(Board.resourceArray.indexOf(actionSubject.charAt(1)) == -1) return false;
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
    public static boolean isActionWellFormed(String action) {
        boolean flag = false;
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
    public static boolean isActionValid(String boardState, String action) {
            boolean result = false;
            boolean p1 = (boardState.indexOf("W") == 0);
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
            if (boardState.substring(1,3).compareTo("00") == 0) {
                if (action.substring(0, 6).compareTo("buildR") == 0){
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
                } else if (action.substring(0, 5).compareTo("build") == 0) {
                    if (action.charAt(5) == 'R') {
                        int cor1 = Integer.parseInt(action.substring(6,8));
                        int cor2 = Integer.parseInt(action.substring(8,10));
                        boolean conditions = cor2 - cor1 <= 6 && cor2 - cor1 > 2 && canBuild(boardState, "bl");
                        List<String> buildList = new ArrayList<>(Arrays.asList(buildings));
                        if (p1) {
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
                    if (action.charAt(5) == 'K') {
                        result = canBuild(boardState, "gow");
                    }
                    if (action.charAt(5) == 'T') {
                        result = canBuild(boardState, "ggooo");
                    }
                    if (action.charAt(5) == 'S') {
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


        // Alternative version of is action valid that relies on Board, and action Class
    public static boolean isActionValidAlternative(String boardState, String actionString) {
        Board board = new Board(0,0);
        //Check if board string is wellFormed
        if(!board.loadBoard(boardState)) return false;
        Action action = new Action();
        //Check if action is wellFormed
        if(!loadAction(actionString,action)) return false;
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
            //Clockwise
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
            //AntiClockwise
            index = Math.max(Board.coastRoads.indexOf(pos1),Board.coastRoads.indexOf(pos2));
            if(pos1 == 0 && pos2 == 29)
            {
                index = 29;
            }
            index = index + 30;
            for(int y = 0; y < 5; y++)
            {
                roadIndex = Math.min(Board.coastRoads.get(index % 30),Board.coastRoads.get((index - 1 + 30) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get(index % 30),Board.coastRoads.get((index - 1 + 30) % 30));
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
            return (action.type == ActionType.KEEP && hasMaterials(board.resources,action.resourceArray,true));
        }

        boolean flag;

        //Build phase
        switch (action.type)
        {
            case BUILD -> {
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
                        if(!hasMaterials(board.resources,reqResources[0],true)) return false;
                        if(board.knights[action.pieceIndex].owner != null) return false;
                        flag = false;
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
                        if(!hasMaterials(board.resources,reqResources[1],true)) return false;
                        if(board.roadsMap.get(action.pieceIndex).owner != null) return false;
                        pos1 = action.pieceIndex % 100;
                        pos2 = action.pieceIndex / 100;
                        if(board.settlements[pos1].owner != null
                                && board.settlements[pos2].owner != null) return false;
                    }
                    case SETTLEMENT -> {
                        if(!hasMaterials(board.resources,reqResources[2],true)) return false;
                        if(board.settlements[action.pieceIndex].owner != null) return false;
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
                        if(!hasMaterials(board.resources,reqResources[3],true)) return false;
                        if(board.settlements[action.pieceIndex].owner != board.playerTurn) return false;
                    }
                }

            }
            case TRADE -> {
                if(!hasMaterials(board.resources,action.resourceArray,false)) return false;
            }
            case SWAP -> {
                if(!hasMaterials(board.resources,action.resourceArray,false)) return false;
                flag = false;
                for(Piece knight : board.knights)
                {
                    if(knight.owner == board.playerTurn
                            && knight.type == PieceType.KNIGHT
                            && (Board.hexTypeArray[knight.boardIndex] == action.requiredType
                            || Board.hexTypeArray[knight.boardIndex] == HexType.WILD))
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

    //Returns whether the required materials are available
    private static boolean hasMaterials(int[] available, int[] required, boolean negate)
    {
        if(available.length != required.length) return false;
        if(negate)
        {
            for(int i = 0; i < required.length; i++)
            {
                required[i] = -required[i];
            }
        }
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
    public static int[] longestRoad(String boardState) {
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        int[] output = new int [board.playerCount];
        List<Integer> visited = new ArrayList<>();
        List<Integer> roads = new ArrayList<>();
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
    public static int[] largestArmy(String boardState) {
        //Hight and width can be 0 as we are not
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        int[] output = new int [board.playerCount];
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
     * @param boardState: string representation of the board state.
     * @param action: string representation of the player action.
     * @return string representation of the updated board state.
     */
    public static String applyAction(String boardState, String action) {
        // FIXME: Task 9
        return null;
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
