package match;

public enum MatchResult {
    A("A"),
    B("B"),
    DRAW("DRAW");

    public final String stringify;

    MatchResult(String value) {
        stringify = value;
    }

    public static MatchResult fromString(String value) {
        for (MatchResult myEnum : MatchResult.values()) {
            if (myEnum.stringify.equalsIgnoreCase(value)) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant with string value: " + value);
    }

    @Override
    public String toString() {
        return  switch (this) {
            case A -> "A";
            case B -> "B";
            case DRAW -> "DRAW";
        };
    }
}
