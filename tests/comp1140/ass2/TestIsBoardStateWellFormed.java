package comp1140.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static comp1140.ass2.ExampleGames.*;

// @org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class TestIsBoardStateWellFormed {

    private String errorPrefix(String board_state) {
        return "CatanDice.isBoardStateWellFormed(" + board_state + ")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = CatanDiceExtra.isBoardStateWellFormed(in);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void testWellFormedStates() {
        for(int i=0; i < WELL_FORMED_BOARD_STATE.length; i++){
            test(WELL_FORMED_BOARD_STATE[i], true);
        }
    }

    @Test
    public void testNotWellFormedStates() {
        for(int i=0; i < NOT_WELL_FORMED_BOARD_STATE.length; i++){
            test(NOT_WELL_FORMED_BOARD_STATE[i], false);
        }
    }

    @Test
    public void testFullValidGameStates() {
        for (int i=0; i < FULL_GAME_WITH_ACTIONS.length; i++){
            if(i >= 2){
                // skip initial states
                test(FULL_GAME_WITH_ACTIONS[i][0][0], true);
            }
        }
    }

    // junit launcher doesn't work, so just use a simple main to run
    // the test functions...
    public static void main(String[] args) {
        TestIsBoardStateWellFormed tests = new TestIsBoardStateWellFormed();
        System.out.println("testing...");
        tests.testWellFormedStates();
        tests.testNotWellFormedStates();
        tests.testFullValidGameStates();
        System.out.println("all done!");
    }
}
