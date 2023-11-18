package utils;

/**
 * The Pair class represents a simple pair of two values.
 * It provides methods to retrieve the first and second elements of the pair.
 *
 * @param <S> The type of the first element in the pair.
 * @param <T> The type of the second element in the pair.
 */
public class Pair<S, T> {

    /** The first element of the pair. */
    private final S one;

    /** The second element of the pair. */
    private final T two;

    /**
     * Constructs a new pair with the specified values.
     *
     * @param one The first element of the pair.
     * @param two The second element of the pair.
     */
    public Pair(S one, T two) {
        this.one = one;
        this.two = two;
    }

    /**
     * Gets the first element of the pair.
     *
     * @return The first element of the pair.
     */
    public S getFirst() {
        return one;
    }

    /**
     * Gets the second element of the pair.
     *
     * @return The second element of the pair.
     */
    public T getSecond() {
        return two;
    }
}
