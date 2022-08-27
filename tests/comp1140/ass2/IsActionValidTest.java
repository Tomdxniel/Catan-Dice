package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isActionValid;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class IsActionValidTest {
    private void testIsActionValid(String boardState, String action, boolean expected) {
        Assertions.assertEquals(
                expected,
                isActionValid(boardState, action),
                "on boardState " + boardState + " tested validity of action " + action);
    }


    @Test
    public void testFullGame1() {
        for (String[][] step: ExampleGames.FULL_GAME_WITH_ACTIONS1) {
            if (step[1].length > 0) {
                testIsActionValid(step[0][0], step[1][0], true);
            }
        }
    }

    @Test
    public void testFullGame2() {
        for (String[][] step: ExampleGames.FULL_GAME_WITH_ACTIONS2) {
            if (step[1].length > 0) {
                testIsActionValid(step[0][0], step[1][0], true);
            }
        }
    }

    @Test
    public void testFullGame3() {
        for (String[][] step: ExampleGames.FULL_GAME_WITH_ACTIONS3) {
            if (step[1].length > 0) {
                testIsActionValid(step[0][0], step[1][0], true);
            }
        }
    }

    @Test
    public void testInvalidActions() {
        for (String[] invalidActionScenario : ExampleGames.BOARD_STATE_WITH_INVALID_ACTION) {
            testIsActionValid(invalidActionScenario[0], invalidActionScenario[1], false);
        }
    }
}
