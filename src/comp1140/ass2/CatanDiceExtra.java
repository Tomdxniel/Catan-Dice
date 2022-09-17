package comp1140.ass2;
import java.sql.SQLOutput;
import java.util.*;
public class CatanDiceExtra {


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
        //FIXME remove when finished editing board states
        boolean bool = board.loadBoard(boardState);
        if(bool && !board.toString().equals(boardState))
        {
            System.out.println(boardState);
            System.out.println(board.toString());
        }
        return  bool;
    }







    //Loads action into aciton class
    /*public static Action loadAction(String action)
    {

    }*/



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

        } else if (action.substring(0, 5).compareTo("build") == 0){

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

        } else if (action.substring(0,4).compareTo("swap")== 0){

            if (action.length() != 6) return false;

            char in = action.charAt(4);
            char out = action.charAt(5);

            return (resources.indexOf(in) != -1 && resources.indexOf(out) != -1);
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
        // FIXME: Task 5
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
