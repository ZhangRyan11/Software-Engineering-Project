package api;

import java.util.List;

/**
 * Response object from storage operations.
 */
public class StorageResponse {
    private final List<Integer> numbers;
    private final boolean success;
    
    public StorageResponse(List<Integer> numbers, boolean success) {
        this.numbers = numbers;
        this.success = success;
    }
    
    public List<Integer> getNumbers() {
        return numbers;
    }
    
    public boolean isSuccess() {
        return success;
    }
}
