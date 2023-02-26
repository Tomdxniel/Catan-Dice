package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isBoardStateWellFormed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static comp1140.ass2.ExampleGames.*;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class IsBoardStateWellFormedTest {

    private String errorPrefix(String board_state) {
        return "CatanDiceExtra.isBoardStateWellFormed(" + board_state + ")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = isBoardStateWellFormed(in);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void testWellFormedStates() {
        for (String s : WELL_FORMED_BOARD_STATE) {
            test(s, true);
        }
    }

    @Test
    public void testNotWellFormedStates() {
        for (String s : NOT_WELL_FORMED_BOARD_STATE) {
            test(s, false);
        }
    }

    @Test
    public void testFullValidGame1States() {
        for (int i=0; i < FULL_GAME_WITH_ACTIONS1.length; i++){
            test(FULL_GAME_WITH_ACTIONS1[i][0][0], true);
        }
    }

    @Test
    public void testFullValidGame2States() {
        for (int i=0; i < FULL_GAME_WITH_ACTIONS2.length; i++){
            test(FULL_GAME_WITH_ACTIONS2[i][0][0], true);
        }
    }

    @Test
    public void testFullValidGame3States() {
        for (int i=0; i < FULL_GAME_WITH_ACTIONS3.length; i++){
            test(FULL_GAME_WITH_ACTIONS3[i][0][0], true);
        }
    }
}
