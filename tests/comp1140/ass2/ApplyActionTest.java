package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.applyAction;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class ApplyActionTest {
    private void testNonKeepAction(String expected, String boardState, String action) {
        String actual = applyAction(boardState, action);
        Assertions.assertEquals(expected, actual, "Applied action " + action + " on board state" + boardState);
    }

    private void testKeepAction(String boardState, String action) {
        String newState = applyAction(boardState, action);

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
    public void testNonKeepActions() {
        for (String[] step : ExampleGames.FULL_GAME1_WITH_NON_KEEP_ACTIONS)
            testNonKeepAction(step[2], step[0], step[1]);
        for (String[] step : ExampleGames.FULL_GAME2_WITH_NON_KEEP_ACTIONS)
            testNonKeepAction(step[2], step[0], step[1]);
        for (String[] step : ExampleGames.FULL_GAME3_WITH_NON_KEEP_ACTIONS)
            testNonKeepAction(step[2], step[0], step[1]);
    }

    @Test
    public void testKeepActions() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS1) {
            if (step[1].length == 0 || step[1][0].indexOf("keep") != 0)
                continue;
            testKeepAction(step[0][0], step[1][0]);
        }
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS2) {
            if (step[1].length == 0 || step[1][0].indexOf("keep") != 0)
                continue;
            testKeepAction(step[0][0], step[1][0]);
        }
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS3) {
            if (step[1].length == 0 || step[1][0].indexOf("keep") != 0)
                continue;
            testKeepAction(step[0][0], step[1][0]);
        }
    }
}
