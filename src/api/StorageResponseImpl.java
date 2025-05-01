package api;

import java.util.List;

/**
 * Implementation of the StorageResponse interface.
 */
public class StorageResponseImpl implements StorageResponse {
    private List<Integer> data;
    private boolean success;
    
    public StorageResponseImpl(List<Integer> data, boolean success) {
        this.data = data;
        this.success = success;
    }
    
    public List<Integer> getData() {
        return data;
    }
    
    @Override
    public List<Integer> getNumbers() {
        return data;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
}
