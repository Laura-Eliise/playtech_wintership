package utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * The `ListUtils` class provides utility methods for working with lists.
 */
public class ListUtils {

    /**
     * Finds the index of the first element in the given list that satisfies the specified condition
     * based on the provided comparator function.
     *
     * @param <T>        the type of elements in the list
     * @param array      the list of elements to search
     * @param comparator the comparator function to apply to elements
     * @return the index of the first element satisfying the condition, or -1 if no such element is found
     */
    public static <T> int findFirstIndex(List<T> array, Predicate<T> comparator) {
        for (int i = 0; i < array.size(); i++) {
            if (comparator.test(array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Sums the integer values obtained by applying the provided mapper function to each element in the list.
     *
     * @param <T>    the type of elements in the list
     * @param list   the list of elements
     * @param mapper the function to convert each element to an integer
     * @return the sum of the integer values obtained by applying the mapper function to each element
     */
    public static <T> int sumIntValues(List<T> list, ToIntFunction<T> mapper) {
        return list.stream().mapToInt(mapper).sum();
    }
}
