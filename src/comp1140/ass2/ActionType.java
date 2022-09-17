package comp1140.ass2;

public enum ActionType {
    KEEP, BUILD, TRADE, SWAP;
    public static ActionType from(String str) {
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