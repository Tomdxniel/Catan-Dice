package comp1140.ass2;


// ActionType.java created by Sam Liersch u7448311
public enum ActionType {
    KEEP, BUILD, TRADE, SWAP;
    public static ActionType fromString(String str) {
        return switch (str) {
            case "keep" -> KEEP;
            case "build" -> BUILD;
            case "trade"-> TRADE;
            case "swap" -> SWAP;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this)
            {
                case KEEP -> "keep";
                case BUILD -> "build";
                case TRADE-> "trade";
                case SWAP -> "swap";
            };

    }

}