package api;


import java.util.ArrayList;
import java.util.List;

/**
 * Response interface from storage operations.
 */
public interface StorageResponse {
    List<Integer> data = new ArrayList<>(){
        private static final long serialVersionUID = 1L;
    };

    /**
     * Gets the list of integers from the storage response.
     * 
     * @return List of integers
     */
    List<Integer> getNumbers();

    /**
     * Indicates if the operation was successful.
     * 
     * @return True if successful, false otherwise
     */
    boolean isSuccess();

}
