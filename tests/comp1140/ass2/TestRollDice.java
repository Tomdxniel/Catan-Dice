package comp1140.ass2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.util.Arrays.sort;
import static comp1140.ass2.CatanDiceExtra.rollDice;

@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
public class TestRollDice {

    @Test
    public void incorrectNumOfDice(){
        Assertions.assertNull(rollDice(-1));
        Assertions.assertNull(rollDice(0));
        Assertions.assertNull(rollDice(7));
    }



}
