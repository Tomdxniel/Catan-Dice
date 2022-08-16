package comp1140.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class TestIsActionWellFormed {

    private String errorPrefix(String action) {
        return "CatanDice.isActionWellFormed(\"" + action + "\")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = CatanDiceExtra.isActionWellFormed(in);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void testWellFormedActions() {
        for(int i = 0; i < ExampleGames.WELL_FORMED_ACTION_STATE.length; i++){
            test(ExampleGames.WELL_FORMED_ACTION_STATE[i], true);
        }
    }

    @Test
    public void testNotWellFormedActions() {
        for(int i = 0; i < ExampleGames.NOT_WELL_FORMED_ACTION_STATE.length; i++){
            test(ExampleGames.NOT_WELL_FORMED_ACTION_STATE[i], false);
        }
    }

    @Test
    public void testFullValidGameActions() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS.length; i++){
            if(ExampleGames.FULL_GAME_WITH_ACTIONS[i][1].length > 0){
                for(String action : ExampleGames.FULL_GAME_WITH_ACTIONS[i][1]){
                    test(action, true);
                }
            }
        }
    }

    // junit launcher doesn't work, so just use a simple main to run
    // the test functions...
    public static void main(String[] args) {
        TestIsActionWellFormed tests = new TestIsActionWellFormed();
        System.out.println("testing...");
        tests.testWellFormedActions();
        tests.testNotWellFormedActions();
        tests.testFullValidGameActions();
        System.out.println("all done!");
    }
}
