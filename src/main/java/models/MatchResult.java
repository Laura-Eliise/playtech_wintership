package models;

/**
 * The MatchResult enum represents the possible outcomes of a match in a betting game.
 * <p>
 * Each enum constant corresponds to a specific result: "A" for side A winning, "B" for side B winning,
 * and "DRAW" for a draw. The enum provides methods for converting between enum constants and string values.
 */
public enum MatchResult {
    /**
     * Represents the outcome where side A wins the match.
     */
    A("A"),

    /**
     * Represents the outcome where side B wins the match.
     */
    B("B"),

    /**
     * Represents the outcome of a draw in the match.
     */
    DRAW("DRAW");

    /**
     * The string representation of the enum constant.
     */
    public final String str;

    /**
     * Constructs a new enum constant with the specified string value.
     *
     * @param value the string value corresponding to the enum constant
     */
    MatchResult(String value) {
        str = value;
    }

    /**
     * Converts a string value to the corresponding enum constant.
     *
     * @param value the string value to convert
     * @return the MatchResult enum constant corresponding to the string value
     * @throws IllegalArgumentException if no enum constant with the given string value is found
     */
    public static MatchResult fromString(String value) {
        for (MatchResult myEnum : MatchResult.values()) {
            if (myEnum.str.equalsIgnoreCase(value)) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant with string value: " + value);
    }

    /**
     * Returns the string representation of the enum constant.
     *
     * @return the string representation of the enum constant
     */
    @Override
    public String toString() {
        return switch (this) {
            case A -> "A";
            case B -> "B";
            case DRAW -> "DRAW";
        };
    }
}
