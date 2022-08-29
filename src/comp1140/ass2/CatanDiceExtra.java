package comp1140.ass2;

public class CatanDiceExtra {
    //FIXME is it alright having this as a static variable as catanDiceExtra will only exit once
    private static Player playerTurn;
    private static int numDice;
    private static int rollsDone;
    private static final String[] names = {"Sam","Jim","Eliz","Tom"};


    private static final int playerCount = 2;
    private static boolean setupPhase = false;
    public static Player[] players = new Player[playerCount];
    public static ResourceType[] resources;           //"bglmow" corresponding to resource, null for no resource



    /*
    Breaks the board state into section of [ID],[# Dice],[Rolls Done],[Resources],[Placement],[Score] and stores it in respective places
    then does checks to see if boardstate is valid
     */

    //FIXME is it alright to find if string contains invalid data ie char when int is expected by try catch?
    //FIXME what does static on a method do? Does it mean that it can only interact with static elements of this class?
    public static boolean loadBoard(String boardState)
    {
        int index = 0;
        //FIXME is it okay to create a player for each gameState
        for(int i = 0; i < playerCount; i++)
        {
            players[i] = new Player(names[i],"WXYZ".charAt(i));
        }
        //FIXME is there any other orientaion for resources so it does not need to be cleared every method call
        resources = new ResourceType[6];

        String resourceString = "bglmow";
        //FIXME would the better method be transcribing it totally into game states and then checking if valid, or validate as you go along
        //FIXME do I have to be careful of attacks such as loading a half state that errors out then loading a full state
        //FIXME any way in the to debug it with it showing which return was used
        //FIXME are we supposed to have definitions of @param and @return for each method we create
        try {
            for(int i = 0; i < playerCount; i++)
            {
                if( players[i].playerID == boardState.charAt(0))
                {
                    playerTurn =players[i];
                }
            }
            index++;
            //FIXME is creating a substring every time you want to convert to int bad practice?
            numDice = Integer.parseInt(boardState.substring(index, index + 1));
            index++;
            rollsDone = Integer.parseInt(boardState.substring(index, index + 1));
            index++;
            //FIXME should I initiate a stringbuilder every time I want to output a string with +=
            for(int i = 0; i < 6; i++)
            {
                if(resourceString.indexOf(boardState.charAt(index)) < 0)
                {
                    break;
                }
                resources[i] = ResourceType.fromChar(boardState.charAt(index));
                index++;
            }


            //FIXME is there anyway to say not (statement) without bracketing everything

            //FIXME what is the best name convention for pos1&pos2
            int position1;
            int position2;
            for (int i = 0; i < playerCount; i++) {
                //[ID]

                //FIXME is this a adequate way to loop and check different player?

                //Make sure first Player is W next player is X
                if(!("WX".charAt(i) == boardState.charAt(index)))
                {
                    System.out.println("2");
                    return false;
                }
                index++;
                //FIXME should it be checked that it is in alphanumerical order

                //[Placement]
                //Castle
                while (boardState.charAt(index) == 'C') {
                    index++;
                    //FIXME is typecasting like this bad practice?
                    position1 = (int) boardState.charAt(index) - 48;
                    //Is referencing a variable of player1 directly and not using a function of player one bad practice
                    if(players[i].castles[position1] != null)
                    {
                        System.out.println("3");
                        return false;
                    }
                    players[i].castles[position1] = PieceType.CITY;
                    index++;
                }


                //FIXME should it check if knights and used knights don't overlap
                //Used/Unused Knight
                while (boardState.charAt(index) == 'J' || boardState.charAt(index) == 'K') {
                    index++;
                    position1 = Integer.parseInt(boardState.substring(index, index + 2));
                    //FIXME if its acceptable to check by try catch is it okay to simplify this as if the positon isnt between 0 and 20 it would create an error
                    if (!(position1 < 20 && position1 >= 0) || players[i].knights[position1] != null) {
                        return false;
                    }
                    players[i].knights[position1] = (boardState.charAt(index-1) == 'J') ? PieceType.USEDKNIGHT : PieceType.KNIGHT ;

                    index += 2;
                }

                //Road
                while (boardState.charAt(index) == 'R') {
                    index++;
                    position1 = Integer.parseInt(boardState.substring(index, index + 4));
                    if(players[i].roads[position1] != null)
                    {
                        return false;
                    }
                    players[i].roads[position1] = PieceType.ROAD;
                    //position2 = Integer.parseInt(boardState.substring(index + 2, index + 4));
                    index += 4;

                }

                //Settlement
                while (boardState.charAt(index) == 'S' || boardState.charAt(index) == 'T') {
                    index++;
                    position1 = Integer.parseInt(boardState.substring(index, index + 2));
                    //FIXME if its acceptable to check by try catch is it okay to simplify this as if the positon isnt between 0 and 54 it would create an error
                    if (!(position1 < 54 && position1 >= 0)||players[i].pieces[position1] != null) {
                        System.out.println("6");
                        return false;
                    }
                    players[i].pieces[position1] = (boardState.charAt(index-1) == 'S') ? PieceType.SETTLEMENT : PieceType.CITY ;
                    index += 2;
                }



            }
            for(int i = 0; i < playerCount; i++)
            {
                //-------------------------------------
                //Player Score
                //Make sure first Player is W next player is X
                if(!(players[i].playerID == boardState.charAt(index)))
                {
                    System.out.println("7");
                    return false;
                }
                index++;

                players[i].score = Integer.parseInt(boardState.substring(index, index + 2));
                index += 2;

                //FIXME Is there a more efficient way to do this without using try catch
                try{
                    if(boardState.charAt(index) == 'R')
                    {
                        players[i].longestRoad = true;
                        index++;
                    }

                    if(boardState.charAt(index) == 'A'){
                        players[i].largestArmy = true;
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
            System.out.println(e.toString());
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
    public static String boardToString(){
        StringBuilder output = new StringBuilder();
        output.append(playerTurn.playerID);
        output.append(numDice);
        output.append(rollsDone);

        for(ResourceType r : resources)
        {
            if(r != null)
            {
                output.append(r.toChar());
            }
        }
        for(int i = 0; i < playerCount; i++)
        {
            output.append(players[i].playerID);
            for(int j = 0; j < players[i].castles.length; j++)
            {
                if(players[i].castles[j] != null)
                {
                    output.append("C");
                    output.append(j);
                }
            }
            //USED KNIGHT
            for(int j = 0; j < players[i].knights.length; j++)
            {
                if(players[i].knights[j] == PieceType.USEDKNIGHT)
                {
                    output.append(players[i].knights[j].toChar());
                    //FIXME is this a valid way to ensure string is of good length
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
            //KNIGHT
            for(int j = 0; j < players[i].knights.length; j++)
            {
                if(players[i].knights[j] == PieceType.KNIGHT)
                {
                    output.append(players[i].knights[j].toChar());
                    //FIXME is this a valid way to ensure string is of good length
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }

            //ROADS
            for(int j = 0; j < players[i].roads.length; j++)
            {
                if(players[i].roads[j] != null)
                {
                    output.append("R");
                    output.append(String.format("%4d",j).replace(' ','0'));
                }
            }
            //SETTLEMENTS
            for(int j = 0; j < players[i].pieces.length; j++)
            {
                if(players[i].pieces[j] == PieceType.SETTLEMENT)
                {
                    output.append(players[i].pieces[j].toChar());
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
            //CITIES
            for(int j = 0; j < players[i].pieces.length; j++)
            {
                if(players[i].pieces[j] == PieceType.CITY)
                {
                    output.append(players[i].pieces[j].toChar());
                    output.append(String.format("%2d",j).replace(' ','0'));
                }
            }
        }
        for(int i = 0; i < playerCount; i++)
        {
            output.append(players[i].playerID);
            output.append(String.format("%2d",players[i].score).replace(' ','0'));
            if(players[i].longestRoad)
            {
                output.append('R');
            }
            if(players[i].largestArmy)
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
