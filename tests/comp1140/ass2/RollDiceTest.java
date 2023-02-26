package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.util.Arrays.sort;
import static comp1140.ass2.CatanDiceExtra.rollDice;

@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
public class RollDiceTest {
    private final Map<Character, Integer> resourceCharacterMap = Map.of(
            'b', 0,
            'g', 1,
            'l', 2,
            'm', 3,
            'o', 4,
            'w', 5
    );

    @Test
    public void correctResourceStringLength() {
        for (int len = 1; len <= 6; len++) {
            for (int i = 0; i < 200; i++) {
                String actual = rollDice(len);
                Assertions.assertEquals(
                        len,
                        actual.length(),
                        "Wrong resource string length, got string " + actual + " for numOfDice = " + len
                );
            }
        }
    }

    @Test
    public void correctResourceStringOrder() {
        for (int len = 1; len <= 6; len++) {
            for (int i = 0; i < 200; i++) {
                String actual = rollDice(len);
                char[] sortedCharArray = actual.toCharArray();
                sort(sortedCharArray);
                String sortedActual = new String(sortedCharArray);

                Assertions.assertEquals(sortedActual, actual, "String is not sorted alphabetically");
            }
        }
    }

    @Test
    public void uniformDistribution() {
        final char[] resourceCharacters = {'b', 'g', 'l', 'm', 'o', 'w'};
        int[] characterCount = new int[6];

        // get 360 characters for each length
        for (int len = 1; len <= 6; len++) {
            for (int i = 0; i < 360 / len; i++) {
                for (char c : rollDice(len).toCharArray()) {
                    characterCount[resourceCharacterMap.get(c)]++;
                }
            }
        }

        // should have around 360 copies of each character
        for (int i = 0; i < 6; i++) {
            Assertions.assertTrue(
                    300 <= characterCount[i] && characterCount[i] <= 420,
                    "Distribution is not within reasonable range of uniform distribution over 2160 trials, the character "
                            + resourceCharacters[i]
                            + " appeared "
                            + characterCount[i]
                            + " times, expected this number to be between 300 and 420"
            );
        }
    }
}
