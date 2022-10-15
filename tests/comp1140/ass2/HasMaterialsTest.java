package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.hasMaterials;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class HasMaterialsTest {

    int[][][] SIMPLE_VALID_MATERIAL_LIST_NON_NEGATE = {
            {{0}, {0}},
            {{1}, {-1}},
            {{0, 0}, {0, 0}},
            {{0, 1}, {0, -1}},
            {{1, 0}, {-1, 0}},
            {{1, 1}, {-1, -1}},
            {{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}},
            {{1000, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}},
            {{0, 0, 0, 10000, 0, 0}, {0, 0, 0, -430, 0, 0}},
    };
    int[][][] SIMPLE_NOT_VALID_MATERIAL_LIST_NON_NEGATE = {
            {{0}, {-1}},
            {{1}, {-2}},
            {{0, 0}, {0, -1}},
            {{0, 1}, {0, -2}},
            {{1, 0}, {-2, 0}},
            {{1, 1}, {-2, -2}},
            {{1000, 0, 0, 0, 0, 0}, {-10001, 0, 0, 0, 0, 0}},
            {{0, 0, 0, 10000, 0, 0}, {0, 0, 0, -43000, 0, 0}},
    };
    private String testMessage(int[][] resources) {
        return "CatanDiceExtra.hasMaterials(Required:"
                + Arrays.toString(resources[0])
                + ", Provided:"
                + Arrays.toString(resources[1])
                + ")";
    }

    private void test(int[][] in, boolean expected) {
        String testMessage = testMessage(in);
        boolean out = hasMaterials(in[0], in[1]);
        assertEquals(expected, out, testMessage);
    }

    @Test
    public void testValidNonNegate() {
        for (int[][] testCase : SIMPLE_VALID_MATERIAL_LIST_NON_NEGATE) {
            test(testCase, true);
        }
    }

    @Test
    public void testNotValidNonNegate() {
        for (int[][] testCase : SIMPLE_NOT_VALID_MATERIAL_LIST_NON_NEGATE) {
            test(testCase, false);
        }
    }

}