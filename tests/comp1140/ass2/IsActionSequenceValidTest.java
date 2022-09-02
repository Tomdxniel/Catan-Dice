package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isActionSequenceValid;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class IsActionSequenceValidTest {
    private void testIsActionSequenceValid(String boardState, String[] actionSequence, boolean expected) {
        boolean actual = isActionSequenceValid(boardState, actionSequence);
        Assertions.assertEquals(
                expected,
                actual,
                "applied action sequence " + Arrays.toString(actionSequence) + " on board state " + boardState
        );
    }

    @Test
    public void testFullGame1() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS1)
            testIsActionSequenceValid(step[0][0], step[1], true);
    }

    @Test
    public void testFullGame2() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS2)
            testIsActionSequenceValid(step[0][0], step[1], true);
    }

    @Test
    public void testFullGame3() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS3)
            testIsActionSequenceValid(step[0][0], step[1], true);
    }

    @Test
    public void testInvalidActionSequence() {
        // test over the sequences
        for (String[][] step : ExampleGames.BOARD_STATE_WITH_INVALID_ACTION_SEQUENCES) {
            testIsActionSequenceValid(step[0][0], step[1], false);
        }

        // also test over just a single action
        for (String[] step : ExampleGames.BOARD_STATE_WITH_INVALID_ACTION) {
            testIsActionSequenceValid(step[0], new String[]{ step[1] }, false);
        }
    }

}
