package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isActionWellFormed;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class IsActionWellFormedTest {

    private String errorPrefix(String action) {
        return "CatanDiceExtra.isActionWellFormed(\"" + action + "\")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = isActionWellFormed(in);
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
    public void testFullValidGame1Actions() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS1.length; i++){
            if(ExampleGames.FULL_GAME_WITH_ACTIONS1[i][1].length > 0){
                for(String action : ExampleGames.FULL_GAME_WITH_ACTIONS1[i][1]){
                    test(action, true);
                }
            }
        }
    }

    @Test
    public void testFullValidGame2Actions() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS2.length; i++){
            if(ExampleGames.FULL_GAME_WITH_ACTIONS2[i][1].length > 0){
                for(String action : ExampleGames.FULL_GAME_WITH_ACTIONS2[i][1]){
                    test(action, true);
                }
            }
        }
    }

    @Test
    public void testFullValidGame3Actions() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS3.length; i++){
            if(ExampleGames.FULL_GAME_WITH_ACTIONS3[i][1].length > 0){
                for(String action : ExampleGames.FULL_GAME_WITH_ACTIONS3[i][1]){
                    test(action, true);
                }
            }
        }
    }
}
