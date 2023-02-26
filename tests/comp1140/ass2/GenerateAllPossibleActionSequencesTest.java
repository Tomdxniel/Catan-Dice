package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.generateAllPossibleActionSequences;

@Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
public class GenerateAllPossibleActionSequencesTest {
    private void testGenerateAllPossibleActions(String boardState,String[][] expected) {
        String[][] actual = generateAllPossibleActionSequences(boardState);
        HashSet<List<String>> actualSet = new HashSet<>();

        for (String[] actionSequence : actual)
            actualSet.add(Arrays.asList(actionSequence));

        for (String[] actionSequence : expected) {
            Assertions.assertTrue(actualSet.contains(Arrays.asList(actionSequence)),
                    "missing action sequence " + Arrays.toString(actionSequence) + " for board state " + boardState
            );
        }

        HashSet<List<String>> expectedSet = new HashSet<>();
        for(String[] actionSequence: expected){
            expectedSet.add(Arrays.asList(actionSequence));
        }

        for (String[] actionSequence : actual) {
            Assertions.assertTrue(expectedSet.contains(Arrays.asList(actionSequence)),
                    "contains incorrect action sequence " + Arrays.toString(actionSequence) + " for board state " + boardState
            );
        }
    }

    @Test
    public void testFullGame1() {
        for (String[][][] step : ExampleGames.FULL_GAME1_WITH_ALL_POSSIBLE_ACTION_SEQUENCES) {
            testGenerateAllPossibleActions(step[0][0][0], step[1]);
        }
    }

    @Test
    public void testFullGame2() {
        for (String[][][] step : ExampleGames.FULL_GAME2_WITH_ALL_POSSIBLE_ACTION_SEQUENCES) {
            testGenerateAllPossibleActions(step[0][0][0], step[1]);
        }
    }

    @Test
    public void testFullGame3() {
        for (String[][][] step : ExampleGames.FULL_GAME3_WITH_ALL_POSSIBLE_ACTION_SEQUENCES) {
            testGenerateAllPossibleActions(step[0][0][0], step[1]);
        }
    }
}
