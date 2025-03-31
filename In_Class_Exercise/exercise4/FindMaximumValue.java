package exercise4;

import java.util.Iterator;
import java.util.List;

public class FindMaximumValue {

    public int findMaximumValue(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        Iterator<Integer> iterator = values.iterator();
        int currentMax = iterator.next(); // Initialize with the first element

        while (iterator.hasNext()) {
            int value = iterator.next();
            if (value > currentMax) {
                currentMax = value;
            }
        }
        return currentMax;
    }
}
