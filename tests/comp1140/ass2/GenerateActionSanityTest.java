package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.generateAction;
import static comp1140.ass2.CatanDiceExtra.isActionSequenceValid;

/**
 * This is a sanity test to check that the action sequences given by
 * generateAction are valid. It uses *your* implementation of the
 * isActionSequenceValid function to check the action sequence given by
 * generateAction. Therefore, if you have not implemented isActionSequenceValid
 * correctly then the test result here is meaningless.
 */
@Timeout(value = 60000, unit = TimeUnit.MILLISECONDS)
public class GenerateActionSanityTest {
    private void testGenerateActionSanity(String boardState) {
        String[] actionSequence = generateAction(boardState);
        boolean validity;
        if (actionSequence == null)
            validity = true;
        else
            validity = isActionSequenceValid(boardState, actionSequence);

        Assertions.assertTrue(
                validity,
                "generated action sequence " + Arrays.toString(actionSequence) + " on board state " + boardState
        );
    }

    @Test
    public void testFullGame1() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS1)
            testGenerateActionSanity(step[0][0]);
    }

    @Test
    public void testFullGame2() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS2)
            testGenerateActionSanity(step[0][0]);
    }

    @Test
    public void testFullGame3() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS3)
            testGenerateActionSanity(step[0][0]);
    }
}
