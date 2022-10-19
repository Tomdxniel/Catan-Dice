package comp1140.ass2;

import javafx.scene.paint.Color;

import java.util.*;

import static comp1140.ass2.Board.*;
import static comp1140.ass2.HexType.WILD;

public class CatanDiceExtra {
    //Required resources to build objects

    /**
     * Check if the string encoding of a board state is well-formed.
     * Note that this does not mean checking if the state is valid
     * (represents a state that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
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
        return action.loadAction(actionString);
    }






    /**
     * Check if the string encoding of a player action is well-formed.
     * Note that this does not mean checking if the action is valid
     * (represents a player action that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
     * <p>
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

            if (action.length() == 5) return false;

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
     * <p>
     * A. Roll Phase (keep action)
     * 1. A keep action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "keep[Resources]", and the
     *   current player has the resources specified.
     * - [Rolls Done] is less than 3
     * <p>
     *
     * B. Build Phase (build, trade, and swap actions)
     * <p>
     * 1. A build action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "build[Structure Identifier]"
     * - The current player has sufficient resources available for building
     *   the structure.
     * - The structure satisfies the build constraints (is connected to the
     *   current players road network).
     * - See details of the cost of buildable structure in README.md.
     * <p>
     * 2. A trade action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "trade[Resources]"
     * - The current player has sufficient resources available to pay for
     *   the trade.
     * <p>
     * 3. A swap action is valid if it satisfies the following conditions:
     * - comp1140.ass2.Action follows the correct format : "swap[Resource Out][Resource In]"
     * - The current player has sufficient resources available to swap out.
     * - The current player has an unused knight (resource joker) on the
     *   board which allows to swap for the desired resource.
     * @param boardState: string representation of the board state.
     * @param actionState: string representation of the player action.
     * @return true iff the action is executable, false otherwise.
     */

        public static boolean isActionValid(String boardState, String actionState)
        {
            Board board = new Board(0,0);
            Action action = new Action();
            //Check board state is well-formed
            if(!board.loadBoard(boardState))
            {
                return false;
            }
            //Check action is well-formed

            if(action.loadAction(actionState))
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
            if(!board.roadsMap.containsKey(action.pieceIndex)) return false;
            if(!Board.coastRoads.contains(pos1)) return false;
            if(!Board.coastRoads.contains(pos2)) return false;
            if(board.roadsMap.get(action.pieceIndex).owner != null) return false;
            if(Math.abs(Board.coastRoads.indexOf(pos1) - Board.coastRoads.indexOf(pos2)) > 1 && action.pieceIndex != 3) return false;
            //check no road is within 5 pieces clockwise
            index = Math.max(Board.coastRoads.indexOf(pos1),Board.coastRoads.indexOf(pos2));
            //if piece is 3 the clockwise head is pos 3 not 0
            if(action.pieceIndex == 3)
            {
                index = 0;
            }
            index = index + 30;
            for(int y = 0; y < 5; y++)
            {
                //Clockwise
                roadIndex = Math.min(Board.coastRoads.get((index + y) % 30),Board.coastRoads.get((index+ y + 1) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get((index + y) % 30),Board.coastRoads.get((index + y + 1) % 30));
                if(!board.roadsMap.containsKey(roadIndex)) return false;
                if(board.roadsMap.get(roadIndex).owner != null)
                {
                    return false;
                }
                //AntiClockwise
                roadIndex = Math.min(Board.coastRoads.get((index - y - 1) % 30),Board.coastRoads.get((index - y - 2) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get((index - y - 1) % 30),Board.coastRoads.get((index - y - 2) % 30));
                if(!board.roadsMap.containsKey(roadIndex)) return false;
                if(board.roadsMap.get(roadIndex).owner != null)
                {
                    return false;
                }
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
        //Used if multiple check are needed
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
                                rPos2 = road.boardIndex / 100;
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
                            pos2 = road.boardIndex / 100;
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
                if(p.owner != null && playerIDArray.indexOf(p.owner.playerID) == i)
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
     * <p>
     * A. Start of the Game
     * For example : given boardState = "W00WXW00X00", action = "buildR0205"
     * - Player 'W' builds a road from index 02 to 05
     * - The next boardState should be "X00WR0205XW00X00"
     * - Consult details of the rules for the Start of the Game in README.md
     * <p>
     * B. Middle of the Game
     * For example : given boardState = "W61bbbgwwWR0205R0509S02XR3237W01X00", action = "keepbbbw"
     * - Player 'W' keeps three brick and one wool, and re-rolls two dice.
     * - The next boardState should be "W62[Next Resources]WR0205R0509S02XR3237W01X00"
     * - [Next Resources] can be any 6 resources that contain 3 bricks, 1 wool
     * - Some examples of [Next Resources] are "bbbbmw", "bbbglw", "bbblow", etc
     * <p>
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
        action.loadAction(actionString);

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
                    case ROAD -> board.roadsMap.get(action.pieceIndex).owner = board.playerTurn;
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
                    case KNIGHT -> board.knights[action.pieceIndex].owner = board.playerTurn;
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
                            && Board.hexTypeArray[knight.boardIndex] == WILD) {
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

        Board board = new Board(0, 0);

        if (!board.loadBoard(boardState)) return false;
        Action action;

        for (String act : actionSequence){
            action = new Action();
            if (action.loadAction(act)){
                if (!isActionValid(board, action)) return false;
                applyAction(board, action);
            } else {
                return false;
            }
        }

        return true;
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
        // FIXME: Task 10a

        Board board = new Board(0, 0);

        board.loadBoard(boardState);
        Action action;

        for (String act : actionSequence){
            action = new Action();
            action.loadAction(act);
            applyAction(board, action);
        }

        action = new Action();
        action.loadAction("keep");

        if(board.setupPhase)
        {
            if((board.playerTurn.playerIndex + 1)%board.players.length == 0)
            {
                board.numDice = 3;
                board.rollsDone = 0;
                board.setupPhase = false;
                board.resources = new int[] {0,0,0,0,0,0};

                applyAction(board,action);
            }
            board.playerTurn = board.players[(board.playerTurn.playerIndex + 1)%board.players.length];
        }
        else
        {
            if(board.playerTurn.score < 10) {
                if (board.numDice < 6) {
                    board.numDice++;
                }
                board.rollsDone = 0;
                board.resources = new int[]{0, 0, 0, 0, 0, 0};
                applyAction(board, action);
                board.playerTurn = board.players[(board.playerTurn.playerIndex + 1)%board.players.length];
                System.out.println(board);
            }
        }
        return board.toString();
    }

    /**
     * Given a valid board state, return all applicable player action sequences.
     * The method should return an array of sequences, where each sequence is
     * an array of action string representations.
     * <p>
     * If the current phase of the game is the Start of Game phase, each of
     * the sequences should contain just one road building action (that is
     * a permitted initial road for the player).
     * <p>
     * If the current phase of the game is the Roll phase, each of the
     * sequences should contain just one action, specifying a possible
     * next roll (i.e., resources to keep).
     * <p>
     * If the current phase is the Build phase, the sequences should be all
     * non-redundant sequences of trade, swap and build actions that the
     * player can take.
     * <p>
     * In this context, an action sequence is considered non-redundant if
     * 1. All resources gained through trade and swap actions are totally used.
     *    i.e. the turn finishes with 0 of that resource.
     * 2. A trade action occurs at most once during the action sequence.
     * 3. Gained resources through the trade and swap actions are not later
     *    traded/swapped away.
     * 4. The empty sequence is always non-redundant (i.e. the player takes no
     *    action).
     * <p>
     * Note, there are other sources of redundancy in action sequences besides the
     * ones that are listed here. One of the more noteworthy ones is the ordering of
     * actions within a sequence whereby two different action sequences may result
     * in the same state when applied. While this is not relevant for this task, it
     * may prove useful to consider this for your "smart" game AI in task 14.
     * <p>
     * In the build phase, one of the possible sequences is always to end
     * the player's turn without taking any action, i.e., an empty sequence.
     * <p>
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
     * <p>
     * This method is the interface to your game AI. It is given the current
     * state of the game, and should return the sequence of actions your AI
     * chooses to take.
     * <p>
     * An array of length 0 is interpreted as finishing the current turn
     * without taking any further action.
     *
     * @param boardState: string representation of the board state.
     * @return array of strings representing the actions the AI will take.
     */
    public static String[] generateAction(String boardState) {

        /*
        ** swap resources first
        1) check knight
        2) check castle
        3) check longest road
        4) check settlement
        5) build road
        6) keep resources
         */

        // FIXME: Task 13
        // FIXME: Task 14 Implement a "smart" generateAction()
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        return generateAction(board);
    }
    public static String[] generateAction(Board board)
    {
        return new String[] {"keep"};
    }
}
