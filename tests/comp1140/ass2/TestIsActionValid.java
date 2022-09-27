package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isActionValid;
import static comp1140.ass2.CatanDiceExtra.isActionWellFormed;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class TestIsActionValid {

    String[][] BOARD_STATE_WITH_INVALID_ACTION = {
            {"W00WXW00X00", "buildJ11"},
            {"W00WXW00X00", "buildR0037"},
            {"X00WR0205XW00X00", "keepbg"},
            {"X00WR0205XW00X00", "tradebl"},
            {"X61bbloWK02R0105R0205R0509S02XR3237W01X00", "keepbgo"},
            {"W00WXW00X00", "keepgm"},
            {"W00WXW00X00", "tradeow"},
            {"W00WXW00X00", "swapow"},
            {"X61bbloWK02R0105R0205R0509S02XR3237W01X00", "BuildR0104"},
            {"W00WXW00X00", "buildR0913"}
    };
    String[][] BOARD_STATE_WITH_VALID_ACTION = {
            {"W00WXW00X00", "buildR4952"},
            {"X00WR0104XW00X00", "buildR1520" },
            {"W51glmowWR0104XR1520W00X00", "keepg"},
            {"W32gmwWR0104XR1520W00X00", "keepmw"},
            {"W63bbglwwWR4549R4952XR0711W00X00", "buildR4953"},
            {"W63bbgmowWR4549R4952R4953S45XK03R0711R1116S16W01X01", "buildK18"},
            {"X63bbllmmWR0104XR2127R2733W00X00", "tradel"},
            {"W63glmmowWJ01R0004R0104R0408XR1621R2127R2733W00X00","swapog"},
            {"X63bglmwwWK01R0003R0004R0104R0307R0408S00XJ07R1621R2127R2733W01X00","buildS16"}

    };


    private String errorPrefix(String[] scenario) {
        return "CatanDiceExtra.isActionValid(" + scenario[0] + ", " + scenario[1] + ")";
    }

    private void test(String[] in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = isActionValid(in[0],in[1]);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void testBoardStateWithInvalidActions() {
        for (String[] s : BOARD_STATE_WITH_INVALID_ACTION) {
            test(s, false);
        }
    }

    @Test
    public void testBoardStateWithValidActions() {
        for (String[] s : BOARD_STATE_WITH_VALID_ACTION){
            test(s, true);
        }
    }

}
