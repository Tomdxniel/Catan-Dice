package comp1140.ass2;

public class CatanDiceExtra {
    private static final String[] names = {"Sam","Jim","Eliz","Tom"};
    public static final int playerCount = 2;



    /*
    Breaks the board state into section of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score] and stores it in respective places
    then does checks to see if boardstate is valid
     */
    public static boolean loadBoard(String boardState)
    {
        Board board = new Board(700,1200);
        int index = 0;
        for(int i = 0; i < playerCount; i++)
        {
            board.players[i] = new Player(names[i],"WXYZ".charAt(i));
        }
        board.resources = new ResourceType[6];

        String resourceString = "bglmow";
        //FIXME do I have to be careful of attacks such as loading a half state that errors out then loading a full state
        //FIXME are we supposed to have definitions of @param and @return for each method we create
        try {
            for(int i = 0; i < playerCount; i++)
            {
                if( board.players[i].playerID == boardState.charAt(0))
                {
                    board.playerTurn = board.players[i];
                }
            }
            //if no valid playerTurn return false
            if("WXYZ".indexOf(board.playerTurn.playerID) < 0)
            {
                return false;
            }
            index++;
            board.numDice = Integer.parseInt(boardState.substring(index, index + 1));
            index++;
            board.rollsDone = Integer.parseInt(boardState.substring(index, index + 1));
            index++;
            for(int i = 0; i < 6; i++)
            {
                if(resourceString.indexOf(boardState.charAt(index)) < 0)
                {
                    break;
                }
                board.resources[i] = ResourceType.fromChar(boardState.charAt(index));
                index++;
            }

            int pos;
            for (int i = 0; i < playerCount; i++) {
                //[ID]

                //Make sure first Player is W next player is X
                if(!("WX".charAt(i) == boardState.charAt(index)))
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
                    if(board.castles[pos].owner != null)
                    {
                        //Castles of pos1 owner is not null it means there is a double
                        return false;
                    }
                    board.castles[pos].owner = board.players[i];
                    index++;
                }

                //Used/Unused Knight
                while (boardState.charAt(index) == 'J' || boardState.charAt(index) == 'K') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 2));
                    if (board.knights[pos].owner != null) {

                        return false;
                    }
                    board.knights[pos].type = (boardState.charAt(index-1) == 'J') ? PieceType.USEDKNIGHT : PieceType.KNIGHT ;
                    board.knights[pos].owner = board.players[i];
                    index += 2;
                }

                //Road
                while (boardState.charAt(index) == 'R') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 4));
                    if(board.roadsMap.containsKey(pos))
                    {
                        if(board.roadsMap.get(pos).owner != null)
                        {
                            return false;
                        }

                        board.roadsMap.get(pos).owner = board.players[i];
                    }
                    else
                    {
                        return false;
                    }

                    //position2 = Integer.parseInt(boardState.substring(index + 2, index + 4));
                    index += 4;

                }

                //Settlement
                while (boardState.charAt(index) == 'S' || boardState.charAt(index) == 'T') {
                    index++;
                    pos = Integer.parseInt(boardState.substring(index, index + 2));
                    //FIXME if its acceptable to check by try catch is it okay to simplify this as if the positon isnt between 0 and 54 it would create an error
                    if (board.settlements[pos].owner != null) {
                        return false;
                    }
                    board.settlements[pos].type = (boardState.charAt(index-1) == 'S') ? PieceType.SETTLEMENT : PieceType.CITY ;
                    board.settlements[pos].owner = board.players[i];
                    index += 2;
                }


            }
            for(int i = 0; i < playerCount; i++)
            {
                //-------------------------------------
                //Player Score
                //Make sure first Player is W next player is X
                if(!(board.players[i].playerID == boardState.charAt(index)))
                {
                    return false;
                }
                index++;

                board.players[i].score = Integer.parseInt(boardState.substring(index, index + 2));
                index += 2;

                //FIXME Is there a more efficient way to do this without using try catch
                try{
                    if(boardState.charAt(index) == 'R')
                    {
                        board.players[i].longestRoad = true;
                        index++;
                    }

                    if(boardState.charAt(index) == 'A'){
                        board.players[i].largestArmy = true;
                        index++;
                    }
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    //String is finished
                }

            }

        }
        catch (Exception e)
        {
            //FIXME why is the to string method here showing a warning of redundant
            return false;
        }
        return true;
    }



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
        return loadBoard(boardState);
    }




    // rebuilds board string of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score]
    public static String boardToString(Board board){
        StringBuilder output = new StringBuilder();
        output.append(board.playerTurn.playerID);
        output.append(board.numDice);
        output.append(board.rollsDone);

        for(ResourceType r : board.resources)
        {
            if(r != null)
            {
                output.append(r.toChar());
            }
        }
        for(int i = 0; i < playerCount; i++)
        {
            output.append(board.players[i].playerID);
            for(int j = 0; j < board.castles.length; j++)
            {
                if(board.castles[j].owner == board.players[i])
                {
                    output.append("C");
                    output.append(j);
                }
            }
            //USED KNIGHT
            for(int j = 0; j < board.knights.length; j++)
            {
                if(board.knights[j].owner == board.players[i] && board.knights[j].type == PieceType.USEDKNIGHT)
                {
                    output.append(board.knights[j].type.toChar());
                    //FIXME is this a valid way to ensure string is of good length
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
            //KNIGHT
            for(int j = 0; j < board.knights.length; j++)
            {
                if(board.knights[j].owner ==  board.players[i]&& board.knights[j].type == PieceType.KNIGHT)
                {
                    output.append(board.knights[j].type.toChar());
                    //FIXME is this a valid way to ensure string is of good length
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }

            //ROADS
            //FIXME this can be improved by only considering roads that are in roadsMap hasMap
            for(int j = 0; j < 10000; j++) {
                if(board.roadsMap.containsKey(j)) {
                    if (board.roadsMap.get(j).owner  == board.players[i]) {
                        output.append("R");
                        output.append(String.format("%4d", j).replace(' ', '0'));
                    }
                }
            }
            //SETTLEMENTS
            for(int j = 0; j < board.settlements.length; j++)
            {
                if(board.settlements[j].type == PieceType.SETTLEMENT && board.settlements[j].owner == board.players[i])
                {
                    output.append(board.settlements[j].type.toChar());
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
            //CITIES
            for(int j = 0; j < board.settlements.length; j++)
            {
                if(board.settlements[j].type == PieceType.CITY && board.settlements[j].owner == board.players[i])
                {
                    output.append(board.settlements[j].type.toChar());
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
        }
        for(int i = 0; i < playerCount; i++)
        {
            output.append(board.players[i].playerID);
            output.append(String.format("%2d",board.players[i].score).replace(' ','0'));
            if(board.players[i].longestRoad)
            {
                output.append('R');
            }
            if(board.players[i].largestArmy)
            {
                output.append('A');
            }

        }
        return output.toString();
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
        // FIXME: Task 4
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
        // FIXME: Task 5
        return "";
    }

    /**
     * Given a valid board state and player action, determine whether the
     * action can be executed.
     * The permitted actions depend on the current game phase:
     *
     * A. Roll Phase (keep action)
     * 1. A keep action is valid if it satisfies the following conditions:
     * - Action follows the correct format : "keep[Resources]", and the
     *   current player has the resources specified.
     * - [Rolls Done] is less than 3
     *
     *
     * B. Build Phase (build, trade, and swap actions)
     *
     * 1. A build action is valid if it satisfies the following conditions:
     * - Action follows the correct format : "build[Structure Identifier]"
     * - The current player has sufficient resources available for building
     *   the structure.
     * - The structure satisfies the build constraints (is connected to the
     *   current players road network).
     * - See details of the cost of buildable structure in README.md.
     *
     * 2. A trade action is valid if it satisfies the following conditions:
     * - Action follows the correct format : "trade[Resources]"
     * - The current player has sufficient resources available to pay for
     *   the trade.
     *
     * 3. A swap action is valid if it satisfies the following conditions:
     * - Action follows the correct format : "swap[Resource Out][Resource In]"
     * - The current player has sufficient resources available to swap out.
     * - The current player has an unused knight (resource joker) on the
     *   board which allows to swap for the desired resource.
     * @param boardState: string representation of the board state.
     * @param action: string representation of the player action.
     * @return true iff the action is executable, false otherwise.
     */
    public static boolean isActionValid(String boardState, String action) {
        // FIXME: Task 7
        return false;
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
        // FIXME: Task 8a
        return null;
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
        // FIXME: Task 8b
        return null;
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
