// File: Main.java
import java.util.List;
import java.util.ArrayList;

public class Main {

    // 1. Generic method to compute the number of elements in any Iterable.
    public static <T> int getLength(Iterable<T> iterable) {
        int count = 0;
        for (T element : iterable) {
            count++;
        }
        return count;
    }

    // 2. Modified method that only accepts Sizeable items and computes their total size.
    public static <T extends Sizeable> int getTotalSize(Iterable<T> iterable) {
        int totalSize = 0;
        for (T item : iterable) {
            totalSize += item.getSize();
        }
        return totalSize;
    }

    public static void main(String[] args) {
        // Create a List of Box objects (each Box implements Sizeable)
        List<Box> boxes = new ArrayList<>();
        boxes.add(new Box(5));
        boxes.add(new Box(10));
        boxes.add(new Box(3));

        // Verify getLength works on any Iterable
        System.out.println("Number of elements: " + getLength(boxes));  // Expected: 3

        // Verify getTotalSize works for Sizeable items
        System.out.println("Total size: " + getTotalSize(boxes));         // Expected: 18
    }
}
