package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.applyActionSequence;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class ApplyActionSequenceTest {
    private void testBuildPhase(String boardState, String[] actionSequence, String expected) {
        String actual = applyActionSequence(boardState, actionSequence);

        // get rid of the dice roll
        int resourceStringLength = Integer.parseInt(expected.substring(1, 2));
        expected = expected.charAt(0) + expected.substring(3 + resourceStringLength);
        actual = actual.charAt(0) + actual.substring(3 + resourceStringLength);

        Assertions.assertEquals(
                expected,
                actual,
                "applied action sequence " + Arrays.toString(actionSequence) + " on board state " + boardState
        );
    }

    private void testRollPhase(String boardState, String action) {
        String newState = CatanDiceExtra.applyAction(boardState, action);

        // just compute over all characters, no need to restrict to resource characters
        HashMap<Character, Integer> kept = new HashMap<>();
        for (Character c : newState.toCharArray()) {
            kept.putIfAbsent(c, 0);
            kept.put(c, kept.get(c) + 1);
        }

        // compute how much needed to be kept
        HashMap<Character, Integer> want = new HashMap<>();
        for (Character c : action.substring(4).toCharArray()) {
            want.putIfAbsent(c, 0);
            want.put(c, want.get(c) + 1);
        }

        for (Character c: action.substring(4).toCharArray()) {
            kept.putIfAbsent(c, 0);
            Assertions.assertTrue(
                    kept.get(c) >= want.get(c),
                    "Applied action " + action + " on board state " + boardState + " got " + newState + ", needed " + want.get(c) + " of " + c + " but only kept " + kept.get(c));
        }
    }

    @Test
    public void testFullGame1 () {
        String[][][] fullGame = ExampleGames.FULL_GAME_WITH_ACTIONS1;
        for (int i = 0; i + 1 < fullGame.length; i++) {
            String boardState = fullGame[i][0][0];
            String[] actionSequence = fullGame[i][1];
            String nextState = fullGame[i + 1][0][0];

            if (actionSequence.length == 1 && actionSequence[0].indexOf("keep") == 0) {
                // roll phase
                testRollPhase(boardState, actionSequence[0]);
            } else {
                // build phase
                testBuildPhase(boardState, actionSequence, nextState);
            }
        }
    }

    @Test
    public void testFullGame2 () {
        String[][][] fullGame = ExampleGames.FULL_GAME_WITH_ACTIONS2;
        for (int i = 0; i + 1 < fullGame.length; i++) {
            String boardState = fullGame[i][0][0];
            String[] actionSequence = fullGame[i][1];
            String nextState = fullGame[i + 1][0][0];

            if (actionSequence.length == 1 && actionSequence[0].indexOf("keep") == 0) {
                // roll phase
                testRollPhase(boardState, actionSequence[0]);
            } else {
                // build phase
                testBuildPhase(boardState, actionSequence, nextState);
            }
        }
    }

    @Test
    public void testFullGame3 () {
        String[][][] fullGame = ExampleGames.FULL_GAME_WITH_ACTIONS3;
        for (int i = 0; i + 1 < fullGame.length; i++) {
            String boardState = fullGame[i][0][0];
            String[] actionSequence = fullGame[i][1];
            String nextState = fullGame[i + 1][0][0];

            if (actionSequence.length == 1 && actionSequence[0].indexOf("keep") == 0) {
                // roll phase
                testRollPhase(boardState, actionSequence[0]);
            } else {
                // build phase
                testBuildPhase(boardState, actionSequence, nextState);
            }
        }
    }
}
