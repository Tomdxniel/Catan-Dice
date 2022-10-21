package comp1140.ass2;

import comp1140.ass2.gui.Game;
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









    /**
     * Check if the string encoding of a player action is well-formed.
     * Note that this does not mean checking if the action is valid
     * (represents a player action that the player could get to in game play),
     * only that the string representation is syntactically well-formed.
     * <p>
     * A description of the board state string will be provided in a
     * later update of the project README.
     *
     * @param actionString: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     * a player action, false otherwise.
     */
    public static boolean isActionWellFormed(String actionString)
    {
        Action action = new Action();
        return action.loadAction(actionString);
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
                Game.errorMessage = "Board state is not well formed";
                return false;
            }
            //Check action is well-formed
            if(!action.loadAction(actionState))
            {
                Game.errorMessage = "Action is not well formed";
                return false;
            }
            //Check action is valid
            if(!isActionValid(board,action))
            {
                Game.errorMessage = "Action is not valid";
                return false;
            }
            return true;
        }

    //isActionValid created by Sam Liersch u7448311
    //is Action valid relies on Board and Action class instead of strings
    public static boolean isActionValid(Board board, Action action) {

        int pos1;
        int pos2;

        //SetupPhase
        if(board.setupPhase)
        {
            int index;
            int roadIndex;
            if(action.type != ActionType.BUILD||action.pieceType != PieceType.ROAD) {
                Game.errorMessage = "Only Coast Roads can be built in setup phase";
                return false;
            }
            pos1 = action.pieceIndex % 100;
            pos2 = action.pieceIndex / 100;
            if(!board.roadsMap.containsKey(action.pieceIndex)) return false;
            if(!Board.coastRoads.contains(pos1)) return false;
            if(!Board.coastRoads.contains(pos2)) return false;
            if(board.roadsMap.get(action.pieceIndex).owner != null) {
                Game.errorMessage = "Can only build a road that has not been built";
                return false;
            }
            if(Math.abs(Board.coastRoads.indexOf(pos1) - Board.coastRoads.indexOf(pos2)) > 1 && action.pieceIndex != 3) {
                Game.errorMessage = "Can only build valid Roads";
                return false;
            }
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
                    Game.errorMessage = "Setup phase requires roads to have 5 roads between";
                    return false;
                }
                //AntiClockwise
                roadIndex = Math.min(Board.coastRoads.get((index - y - 1) % 30),Board.coastRoads.get((index - y - 2) % 30)) * 100;
                roadIndex += Math.max(Board.coastRoads.get((index - y - 1) % 30),Board.coastRoads.get((index - y - 2) % 30));
                if(!board.roadsMap.containsKey(roadIndex)) return false;
                if(board.roadsMap.get(roadIndex).owner != null)
                {
                    Game.errorMessage = "Setup phase requires roads to have 5 roads between";
                    return false;
                }
            }
            return true;

        }

        //Roll phase
        if(board.rollsDone < 3)
        {
            if(action.type != ActionType.KEEP)
            {
                Game.errorMessage = "Rolls must be completed before any other move";
                return false;
            }
            for(int i = 0; i < 6; i++)
            {
                if(board.resources[i] < action.resourceArray[i])
                {
                    return false;
                }
            }
            return true;
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
                        if(!flag) {
                            Game.errorMessage = "Castle requires 6 of the same resource";
                            return false;
                        }
                        if(board.castles[action.pieceIndex].owner != null) {
                            Game.errorMessage = "Cannot build a castle if already built";
                            return false;
                        }
                    }
                    case KNIGHT -> {
                        if(!hasMaterials(board.resources,action.resourceArray)) {
                            Game.errorMessage = "Insufficient resources for Knight";
                            return false;
                        }
                        if(board.knights[action.pieceIndex].owner != null) {
                            Game.errorMessage = "Cannot build a Knight if already built";
                            return false;
                        }
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
                                    if(!flag)
                                    {
                                        Game.errorMessage = "Knights require an adjacent piece";
                                        return false;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    case ROAD -> {

                        if(!hasMaterials(board.resources,action.resourceArray)) {
                            Game.errorMessage = "Insufficient resources for a Road";
                            return false;
                        }
                        if(board.roadsMap.get(action.pieceIndex) == null) return false;
                        if(board.roadsMap.get(action.pieceIndex).owner != null) {
                            Game.errorMessage = "Cannot build a Road if already built";
                            return false;
                        }
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
                            Game.errorMessage = "Roads can only be built in a chain";
                            return false;
                        }
                    }
                    case SETTLEMENT -> {
                        if(!hasMaterials(board.resources,action.resourceArray)) {
                            Game.errorMessage = "Insufficient resources for Settlement";
                            return false;
                        }
                        if(board.settlements[action.pieceIndex].owner != null) {
                            Game.errorMessage = "Cannot build a Settlement if already built";
                            return false;
                        }
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
                        if(!hasMaterials(board.resources,action.resourceArray))
                        {
                            Game.errorMessage = "Insufficient resources to build this City";
                            return false;
                        }
                        if(board.settlements[action.pieceIndex].maxLevel != 2)
                        {
                            Game.errorMessage = "This Cities max Level is 1";
                            return false;
                        }
                        if(board.settlements[action.pieceIndex].owner != board.playerTurn)
                        {
                            Game.errorMessage = "Can only upgrade owned settlements";
                            return false;
                        }
                    }
                }

            }
            case TRADE -> {
                if(!hasMaterials(board.resources,action.resourceArray))
                {
                    Game.errorMessage = "Insufficient materials to trade";
                    return false;
                }
            }
            case SWAP -> {
                if(!hasMaterials(board.resources,action.resourceArray))
                {
                    Game.errorMessage = "Insufficient Resources to Swap";
                    return false;
                }
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
                if(!flag)
                {
                    Game.errorMessage = "Requires a knight of correct type to Swap";
                    return false;
                }
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



    //longestRoad created by Sam Liersch u7448311

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

    //applyAction created by Thomas Daniel u7490675, Edited by Sam Liersch u7448311
    //is Action valid relies on Board and Action class instead of strings
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
            //Largest army min is 2, only set player to have the largest army if they have an army size of 3 or greater
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
    //isActionSequenceValid created by Eliz So u7489812 Edited by Sam Liersch u7448311
    public static boolean isActionSequenceValid(String boardState, String[] actionSequence) {
        Board state = new Board(0, 0);

        // check if there are problems with loading the boardState.
        if (!state.loadBoard(boardState)) return false;
        Action action;

        for (String act : actionSequence){
            action = new Action();
            //checks if action is well formed
            if (action.loadAction(act)){
                if (!isActionValid(state, action)) return false;
                applyAction(state, action);
            } else {
                return false;
            }
        }

        return true;
    }


    //applyActionSequence created by Sam Liersch u7448311

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
    public static String applyActionSequence(String boardState, String[] actionSequence)
    {
        Board board = new Board(0, 0);
        board.loadBoard(boardState);

        return applyActionSequence(board, actionSequence);
    }


    public static String applyActionSequence(Board board, String[] actionSequence) {


        Action action;

        //loops through the action sequence
        if (actionSequence == null) return null;
        for (String act : actionSequence){

            action = new Action();
            action.loadAction(act);
            applyAction(board, action);
            if(board.playerTurn.score > 9)
            {
                return board.toString();
            }
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

    //generateAllPossibleActionSequences created by Eliz So u7489812
    public static String[][] generateAllPossibleActionSequences(String boardState) {

        System.out.println(boardState);
        // the action types include keep, build, swap, and trade
        // find the resources that it has
        // for keep, find all the resources it has, and keep the combinations of it
        // for swapping, find out what it can swap
        // for trading, find out what can be traded.
        // for build, find all the things it can build before keeping, swapping and trading the resource and also after
        // if none can be done, return false;

        System.out.println("start");

        Board board = new Board(0, 0);
        board.loadBoard(boardState);


        Action action;



        //start of the game
        if (board.setupPhase){
            ArrayList<Action> actions = new ArrayList<>();
            for (int i = 1; i < coastRoads.size(); i++){

                action = new Action();

                //gets the position of the coastRoads
                int pos0 = coastRoads.indexOf(i-1);
                int pos1 = coastRoads.indexOf(i);

                //creates an action string for the road.
                String buildRoad = "buildR";
                if (pos0 < 10) buildRoad += '0' + pos0;
                else buildRoad += pos0;
                if (pos1 < 10) buildRoad += '0' + pos1;
                else buildRoad += pos1;

                //checks if the action is valid.
                if (action.loadAction(buildRoad))
                    if (isActionValid(board, action))
                        actions.add(action);
            }
            System.out.println("starting");
            return new String[][]{actionsToString(actions)};

        }



        //roll phase
        else if (board.rollsDone < 3){
            ArrayList<String> rolls =  generateRollPhase(board);
            String[][] output = new String[rolls.size()][1];
            for (int j = 0; j < rolls.size(); j++){
                output[j] = new String[]{rolls.get(j)};
            }
            return output;
        }

        //build phase

        //generate the build actions
        ArrayList<ArrayList<Action>> build = new ArrayList<>();
        ArrayList<ArrayList<Action>> tempBuild;




        build.addAll(buildGenerator(board));

        for (int j = 0 ; j < build.size(); j++ ){

            ArrayList<Action> actions = build.get(j);

            System.out.println("actions: " + Arrays.toString(actions.toArray()));
            if (actions.isEmpty()) break;

            Board temp = new Board(0, 0);
            temp.loadBoard(board.toString());

            boolean trade = false;
            Action tradeAct = new Action();
            String newTrade;

            ArrayList<Action> tempAct = new ArrayList<>();

            for (Action act : actions){
                tempAct.add(act);
                applyAction(temp, act);
                if (act.type == ActionType.TRADE)
                {
                    tradeAct = act;
                    trade = true;
                }
                System.out.println(temp.toString());
            }



            System.out.println("tempAct: " + Arrays.toString(tempAct.toArray()));

            tempBuild = buildGenerator(temp);
            System.out.println("tempBuild: " + Arrays.toString(tempBuild.toArray()));

            for (ArrayList<Action> acts : tempBuild){

                for (Action act : acts){
                    if (trade && act.type == ActionType.TRADE){
                        // finds out what else to be traded and add to the same trade string
                        newTrade = act.toString();
                        ArrayList<Character> output = new ArrayList<>();
                        ArrayList<Character> insert = new ArrayList<>();

                        for (char c : tradeAct.toString().toCharArray())
                            output.add(c);
                        for (char c : newTrade.toCharArray())
                            insert.add(c);

                        int out = 5;
                        int in = 5;



                        //merge the two trades together
                        while (out < output.size() && in < insert.size()){

                            if (output.get(out) > insert.get(in)){
                                output.add(out, insert.get(in));
                                in++;
                            }
                            out++;

                        }

                        //add the remaining of the elements in
                        while (in < insert.size()){
                            output.add(insert.get(in));
                            in++;
                        }


                        Action tradeNew = new Action();
                        tradeNew.loadAction(output.toString());

                        tempAct.remove(tradeAct);
                        tempAct.add(0, tradeNew);




                    } else {
                        if (act != null)
                            tempAct.add(act);
                    }
                }

                if (!build.contains(tempAct) && tempAct.size() > 0)
                    build.add(tempAct);
            }
        }




        //change it back to String[][]
        ArrayList<String[]> temp = new ArrayList<>();



        //changes the arrayList to String[]
        for (ArrayList<Action> actions : build) {
            String[] toAdd = actionsToString(actions);
            if (!temp.contains(toAdd))
                temp.add(toAdd);

        }


        String[][] output = new String[temp.size()][];


        //change the ArrayList of actions to String[][]
        for (int i = 0; i < temp.size(); i++){
            if (temp.get(i) != null)
                output[i] = temp.get(i);
            else
                output[i] = new String[]{};
        }

        //Task 12
        for (String[] stringArray : output){
            System.out.println(Arrays.toString(stringArray));
        }
        Board tempState;
        Action tempAction;
        for(String[] actions : output)
        {
            tempState = new Board(0,0);
            tempState.loadBoard(boardState);
            for(String strAction : actions)
            {
                tempAction = new Action();
                tempAction.loadAction(strAction);
                if(!isActionValid(tempState, tempAction))
                {

                    return new String[][] {{}};
                }
                applyAction(tempState,tempAction);
            }
        }



        return output;

    }


    //buildGenerator created by Eliz So u7489812
    //assists generating all possible actions
    public static ArrayList<ArrayList<Action>> buildGenerator(Board board){
        int[] resources = board.resources; //bglmow

        int[][] required = {
                {0,1,0,0,1,1}, // Knight
                {1,0,1,0,0,0}, // Road
                {1,1,1,0,0,1}, // Settlement
                {0,2,0,0,3,0}  // City
        };

        //checks resources state after building a structure
        int[][] outcome = missingResources(resources);

        ArrayList<ArrayList<Action>> validActions = new ArrayList<>();

        //check for the structures that can directly built off
        for (int i = 0; i < required.length; i++){

            boolean build = true;

            //if all the resources in outcome[i] > 0, then the building can be built.
            for (int j = 0; j < resources.length; j++){
                if (outcome[i][j] < 0) {
                    build = false;
                    break;
                }
            }

            //checks where the person can build their structure
            if (build){
                ArrayList<Action> builds = attemptBuild(i, board);
                if (builds != null && builds.size() > 0)
                    for (Action b : builds) validActions.add(new ArrayList<>(Collections.singletonList(b)));
            }
        }

        // check for swap resources
        validActions.addAll(swapResources(board));

        // check for trade resources
        for (int i = 0; i < required.length; i++){
            ArrayList<ArrayList<Action>> t = tradeResources(i, board);
            if (t != null) System.out.println(t.size());
            if (t != null && t.size() > 0)
                validActions.addAll(tradeResources(i, board));
        }

        validActions.add(new ArrayList<>());

        return validActions;
    }

    //missing resources created by Eliz So u7489812
    //outputs what resources are missing for each structure
    public static int[][] missingResources (int[] curRes){

        int[][] required = {
                {0,1,0,0,1,1}, // Knight
                {1,0,1,0,0,0}, // Road
                {1,1,1,0,0,1}, // Settlement
                {0,2,0,0,3,0}  // City
        };
        int[][] outcome = new int[4][6];

        for (int i = 0; i < required.length; i++){
            for (int j = 0; j < curRes.length; j++){
                outcome[i][j] = curRes[j] - required[i][j];
            }
        }

        return outcome;
    }

    //tradeResources created by Eliz So u7489812
    //trades resources
    public static ArrayList<ArrayList<Action>> tradeResources(int i, Board board){

        int[] res = board.resources;
        if (res[3] < 2) return null;

        Action action;
        ArrayList<ArrayList<Action>> validActions = new ArrayList<>();
        int[][] outcome = missingResources(res);

        action = new Action();
        ArrayList<Action> builds;
        StringBuilder trade = new StringBuilder("trade");

        int gold = res[3];

        //checks what resources are missing and needs to be traded
        for (int j = 0; j < outcome[i].length; j++) {
            if (outcome[i][j] < 0) {
                for (int temp = 0; temp < (outcome[i][j] * -1); temp++) {
                    if (gold < 2) {
                        break;
                    }
                    trade.append(Resource.fromInt(j).toString());
                    gold -= 2;
                }
            }
        }

        //attempts to trade the resources
        if (trade.toString().compareTo("trade") != 0) {

            System.out.println(trade);

            Board temp = new Board(0, 0);
            temp.loadBoard(board.toString());

            if (action.loadAction(trade.toString())) {
                if (isActionValid(board, action)) {

                    //if resources can be traded, check if the structures can be built
                    //remove all redundant actions
                    applyAction(temp, action);
                    System.out.println("Need to build: " + i);
                    builds = attemptBuild(i, temp);

                    if (builds != null && builds.size() > 0) {
                        //if structures can be built after trading, add to validActions
                        for (Action b : builds) {
                            ArrayList<Action> temps = new ArrayList<>();
                            temps.add(action);
                            temps.add(b);
                            validActions.add(temps);
                        }
                    }
                }
            }
        }

        return validActions;
    }


    //swapResources created by Eliz So u7489812
   //checks what resources can be swapped.
    public static ArrayList<ArrayList<Action>> swapResources(Board board) {

        int[] resources = board.resources; //bglmow

        int[][] outcome = missingResources(resources);
        int[] amountReq = {3, 2, 4, 5}; //K, R, S, T
        int totalRes = Arrays.stream(resources).sum();
        ArrayList<Resource> needs;
        ArrayList<Resource> avail;
        ArrayList<ArrayList<Action>> swaps;
        ArrayList<ArrayList<Action>> validActions = new ArrayList<>();

        Action action;

        //checks each structure that can be built.
        for (int i = 0; i < amountReq.length; i++) {

            System.out.println("building: " + i);
            System.out.println(Arrays.toString(outcome[i]));

            if (totalRes < amountReq[i]) continue;

            needs = new ArrayList<>();
            avail = new ArrayList<>();

            //checks on resources that are needed, and checks on resources that are available.
            for (int j = 0; j < outcome[i].length; j++) {
                if (outcome[i][j] < 0) {
                    for (int k = outcome[i][j]; k < 0; k++)
                        needs.add(Resource.fromInt(j));
                } else if (outcome[i][j] > 0) {
                    for (int k = outcome[i][j]; k > 0; k--)
                        avail.add(Resource.fromInt(j));
                }
            }

            //if it can be built, it should've been built in the previous function, no swaps are needed
            if (needs.size() <= 0) continue;

            int index = 0;

            ArrayList<Action> tempSwaps;

            Resource[] availArray = new Resource[avail.size()];

            for (int j = 0; j < availArray.length; j++)
                availArray[j] = avail.get(j);

            //if there are enough resources to swap
            if (needs.size() <= avail.size()){

                //finds all the combination
                ArrayList<Resource[]> combs = combination(availArray, needs.size());

                //loop through each combination
                for (Resource[] comb : combs){
                    tempSwaps = new ArrayList<>();
                    index = 0;

                    //goes through each of the resources and build the swap string.
                    for (Resource res : comb){
                        action = new Action();
                        if (res == null) continue;
                        String swapString = "swap" + res.toString() + needs.get(index).toString();
                        //System.out.println(swapString);

                        // check if this swap is valid
                        if (action.loadAction(swapString)){
                            if (isActionValid(board, action)){
                                tempSwaps.add(action);
                                index++;
                            }
                        }
                    }

                    //create a temporary board and apply the swaps
                    Board temp = new Board(0, 0);
                    temp.loadBoard(board.toString());

                    for (Action swap : tempSwaps){
                        applyAction(temp, swap);
                    }

                    //gives the available builds for structure i
                    ArrayList<Action> builds = attemptBuild(i, temp);

                    //if there are builds after the swap, add it to the ArrayList
                    if (builds != null && builds.size() > 0) {
                        for (Action b : builds) {
                            ArrayList<Action> temps = new ArrayList<>();
                            temps.addAll(tempSwaps);
                            temps.add(b);
                            //System.out.println(Arrays.toString(temps.toArray()));
                            validActions.add(temps);
                        }
                    }

                }
            }
            // if there aren't enough resources to swap, trade
            else {

                ArrayList<Resource[]> combs = combination(availArray, avail.size());

                for (Resource[] comb : combs) {

                    tempSwaps = new ArrayList<>();
                    index = 0;

                    //goes through each of the resources and build the swap string.
                    for (Resource res : comb){
                        action = new Action();
                        if (res == null) continue;
                        String swapString = "swap" + res.toString() + needs.get(index).toString();
                        //System.out.println(swapString);

                        // check if this swap is valid
                        if (action.loadAction(swapString)){
                            if (isActionValid(board, action)){
                                tempSwaps.add(action);
                                index++;
                            }
                        }
                    }

                    //create a temporary board and apply the swaps
                    Board temp = new Board(0, 0);
                    temp.loadBoard(board.toString());

                    for (Action swap : tempSwaps){
                        applyAction(temp, swap);
                    }

                    ArrayList<ArrayList<Action>> trades = tradeResources(i, temp);

                    if (trades != null && trades.size() > 0){
                        for (ArrayList<Action> actions : trades){
                            ArrayList<Action> temps = new ArrayList<>();
                            temps.addAll(tempSwaps);
                            temps.addAll(actions);
                            //System.out.println(Arrays.toString(temps.toArray()));
                            validActions.add(temps);
                        }
                    }

                }

            }


        }
        //System.out.println(validActions.size());
        return validActions;
    }

    //attemptBuild created by Eliz So u7489812
    //attempts to build the piece on the board, returns all the possible places it can be built.
    public static ArrayList<Action> attemptBuild(int piece, Board board){

        ArrayList<Action> output = new ArrayList<>();
        Action action;

        switch (piece){
            case 0 -> {
                StringBuilder buildKnight;

                //checks all the knight spots, see if a knight can be added
                for (int i = 0; i < 20; i++){
                    buildKnight = new StringBuilder("buildK");
                    action = new Action();

                    //append the string to build the action
                    if (i < 10){
                        buildKnight.append(0);
                    }
                    buildKnight.append(i);

                    //checks if the action is valid
                    if (action.loadAction(buildKnight.toString()))
                        if (isActionValid(board, action))
                            output.add(action);
                }

            }

            case 1 -> {

                StringBuilder buildRoad;

                for (int i = 1 ; i < coordinateArray.length; i++){

                    action = new Action();

                    //gets the position of the coordinateArray
                    int pos0 = coordinateArray[i-1];
                    int pos1 = coordinateArray[i];

                    //creates an action string for the road.
                    buildRoad = new StringBuilder("buildR");
                    if (pos0 > pos1){
                        int temp = pos0;
                        pos0 = pos1;
                        pos1 = temp;
                    }
                    if (pos0 < 10)
                        buildRoad.append(0);
                    buildRoad.append(pos0);
                    if (pos1 < 10)
                        buildRoad.append(0);
                    buildRoad.append(pos1);

                    //checks if the action is valid.
                    if (action.loadAction(buildRoad.toString())){
                        boolean check = isActionValid(board, action);
                        if (check)
                            output.add(action);
                    }


                }

            }
            case 2 -> {

                StringBuilder buildSettle;

                //checks each of the spot if a settlement can be built
                for (int i = 0; i < 54; i++){

                    action = new Action();
                    buildSettle = new StringBuilder("buildS");

                    //if less than 10, add a zero in font
                    if (i < 10)
                        buildSettle.append(0);
                    buildSettle.append(i);

                    if (action.loadAction(buildSettle.toString()))
                        if (isActionValid(board, action))
                            output.add(action);

                }

            }
            case 3 -> {

                StringBuilder buildCity;

                //checks all the spot if a city can be built
                for (int i = 0; i < 54; i++){
                    action = new Action();
                    buildCity = new StringBuilder("buildT");

                    //if less than 10, add a 0 in front
                    if (i < 10)
                        buildCity.append(0);
                    buildCity.append(i);

                    if (action.loadAction(buildCity.toString()))
                        if (isActionValid(board, action))
                            output.add(action);
                }

            }
            default -> {
                return null;
            }
        }

        return output;
    }





    //change an ArrayList of actions to an array of String.
    public static String[] actionsToString(ArrayList<Action> actions){

        if (actions == null || actions.size() == 0) return null;

        String[] output = new String[actions.size()];

        for (int i = 0; i < actions.size(); i++){
            output[i] = actions.get(i).toString();
        }

        return output;
    }

    //generateRollPhase created by Eliz So u7489812
    //created to keep all the possible combinations.
    public static ArrayList<String> generateRollPhase(Board board){

        ArrayList<String> actions = new ArrayList<>();
        Resource[] resources = Resource.fromIntArray(board.resources);

        actions.add("keep");

        ArrayList<Resource[]> combs;

        //finds all the combination of resources the player can keep.
        for (int i = 1; i <= resources.length; i++){
            combs = combination(resources, i);

            for (Resource[] comb : combs){

                StringBuilder keepString = new StringBuilder("keep");

                for (Resource res : comb){
                    keepString.append(res.toString());
                }

                if (!actions.contains(keepString.toString()))
                {
                    actions.add(keepString.toString());
                }
            }
        }

        return actions;

    }

    //combination created by Eliz So u7489812
    // finds all the resources combination (nCr) with given r
    public static ArrayList<Resource[]> combination(Resource[] res, int r){
        ArrayList<Resource[]> combs = new ArrayList<>();
        helperComb(res, new Resource[r], combs, 0, res.length - 1, 0 );
        return combs;
    }

    //helperComb created by Eliz So u7489812
    // helper function for combination
    private static void helperComb(Resource[] res, Resource[] data, ArrayList<Resource[]> combs, int start, int end, int index){
        if (index == data.length){
            combs.add(data.clone());
        } else if (start <= end){
            data[index] = res[start];
            helperComb(res, data, combs, start + 1, end, index + 1);
            helperComb(res, data, combs, start + 1, end, index);

        }
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
        Board board = new Board(0,0);
        board.loadBoard(boardState);
        if(board.setupPhase)
        {
            return new String[] {};

        }
        if(board.rollsDone != 3)
        {
            return new String[] {};
        }

        String[][] actions = generateAllPossibleActionSequences(boardState);



        return generateAction(board);


    }
    public static String[] generateAction(Board board)
    {
        String boardString = board.toString();
        String[][] actionsSequence = generateAllPossibleActionSequences(boardString);
        String[] alt = {};
        Board tempBoard;
        Action action;
        String[] maxSequence = {};
        int maxAction = 0;
        for(String[] actions : actionsSequence)
        {
            tempBoard = new Board(0,0);
            tempBoard.loadBoard(boardString);
            if(actions != null)
            {
                for(String actionString : actions)
                {
                    action = new Action();
                    action.loadAction(actionString);
                    if(action.type == ActionType.BUILD)
                    {
                        alt = actions;
                    }
                    applyAction(tempBoard,action);
                }
            }

            if(board.playerTurn.score > maxAction)
            {
                maxAction = board.playerTurn.score;
                maxSequence = actions;
            }
        }


        //if no best action is found play biuld action
        if(maxAction == board.playerTurn.score)
        {
            maxSequence = alt;
        }
        if(maxSequence != null)
        {
            for(String i : maxSequence)
            {
                System.out.println(i);
            }
        }
        return maxSequence;
    }
}
