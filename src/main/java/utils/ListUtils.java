package utils;

import java.util.List;
import java.util.function.Predicate;

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
}
