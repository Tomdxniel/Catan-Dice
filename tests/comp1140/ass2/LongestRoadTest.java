package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.longestRoad;

@Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
public class LongestRoadTest {
    private void testLongestRoad(String boardState, int[] expected) {
        int[] actual = longestRoad(boardState);
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFullGame1() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS1.length; i++) {
            testLongestRoad(
                    ExampleGames.FULL_GAME_WITH_ACTIONS1[i][0][0],
                    ExampleGames.LONGEST_ROAD_COUNT_FOR_FULL_GAME1[i]
            );
        }
    }

    @Test
    public void testFullGame2() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS2.length; i++) {
            testLongestRoad(
                    ExampleGames.FULL_GAME_WITH_ACTIONS2[i][0][0],
                    ExampleGames.LONGEST_ROAD_COUNT_FOR_FULL_GAME2[i]
            );
        }
    }

    @Test
    public void testFullGame3() {
        for (int i = 0; i < ExampleGames.FULL_GAME_WITH_ACTIONS3.length; i++) {
            testLongestRoad(
                    ExampleGames.FULL_GAME_WITH_ACTIONS3[i][0][0],
                    ExampleGames.LONGEST_ROAD_COUNT_FOR_FULL_GAME3[i]
            );
        }
    }

    @Test
    public void testReturnArrayLength() {
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS1) {
            int actual = longestRoad(step[0][0]).length;
            Assertions.assertEquals(
                    2,
                    actual,
                    "longestRoad returned an array with length " + actual + " expected length 2"
            );
        }
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS2) {
            int actual = longestRoad(step[0][0]).length;
            Assertions.assertEquals(
                    2,
                    actual,
                    "longestRoad returned an array with length " + actual + " expected length 2"
            );
        }
        for (String[][] step : ExampleGames.FULL_GAME_WITH_ACTIONS3) {
            int actual = longestRoad(step[0][0]).length;
            Assertions.assertEquals(
                    2,
                    actual,
                    "longestRoad returned an array with length " + actual + " expected length 2"
            );
        }
    }
}
