package api;

import java.util.List;

/**
 * Implementation of the StorageResponse interface.
 */
public class StorageResponseImpl implements StorageResponse {
    private final List<Integer> numbers;
    private final boolean success;
    
    public StorageResponseImpl(List<Integer> numbers, boolean success) {
        this.numbers = numbers;
        this.success = success;
    }
    
    @Override
    public List<Integer> getNumbers() {
        return numbers;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
}
