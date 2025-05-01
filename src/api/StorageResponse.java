package api;

import java.util.List;

/**
 * Response interface from storage operations.
 */
public interface StorageResponse {
    /**
     * Gets the list of numbers from the storage operation.
     * 
     * @return List of numbers
     */
    List<Integer> getNumbers();
    
    /**
     * Indicates if the operation was successful.
     * 
     * @return True if successful, false otherwise
     */
    boolean isSuccess();
}
