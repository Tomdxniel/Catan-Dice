package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static comp1140.ass2.CatanDiceExtra.isActionWellFormed;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
public class TestIsActionWellFormed {

    String[] SIMPLE_WELL_FORMED_ACTION = {
        "keepbmmoww", "buildR0004", "tradebw", "swapgb"
    };
    String[] SIMPLE_NOT_WELL_FORMED_ACTION = {
        "build", "trade", "swap", "kee", "buildA0204", "tradeacdsdf", "swapfs",
        "buil", "tra", "tradem", "swapggbb"
    };

    String[] WELL_FORMED_ACTION = {
        "keepbmow", "keep",
        "buildR0150", "buildR0053",
        "buildC0", "buildC3", "buildC1",
        "buildS53", "buildS00", "buildS21",
        "buildT00", "buildT53", "buildT20",
        "buildK00", "buildK19", "buildK10",
        "tradebglow", "tradegg",
        "swapbg", "swapgb", "swapmw", "swaplo"
    };

    String[] NOT_WELL_FORMED_ACTION = {
        "keepbmowombowmb", "keepwomlgb",
        "buildR-112", "buildR0054", "buildR01100", "buildR02, 03",
        "buildC-1", "buildC4", "buildC15",
        "buildS-1", "buildS54", "buildS154",
        "buildT-1", "buildT54", "buildT154",
        "buildK-1", "buildK20", "buildK120",
        "trademm", "tradebglmow", "tradewombg", "trade12",
        "swapbgb", "swapwom", "swap12"
    };

    private String errorPrefix(String board_state) {
        return "CatanDiceExtra.isBoardStateWellFormed(" + board_state + ")";
    }

    private void test(String in, boolean expected) {
        String errorPrefix = errorPrefix(in);
        boolean out = isActionWellFormed(in);
        assertEquals(expected, out, errorPrefix);
    }

    @Test
    public void testSimpleWellFormedActions() {
        for (String s : SIMPLE_WELL_FORMED_ACTION) {
            test(s, true);
        }
    }

    @Test
    public void testSimpleNotWellFormedActions() {
        for (String s : SIMPLE_NOT_WELL_FORMED_ACTION){
            test(s, false);
        }
    }

    @Test
    public void testWellFormedActions(){
        for (String s : WELL_FORMED_ACTION){
            test(s, true);
        }
    }

    @Test
    public void testNotWellFormedActions(){
        for (String s : NOT_WELL_FORMED_ACTION){
            test(s, false);
        }
    }

}
