package api;

import java.util.List;

/**
 * Interface representing the result of a computation operation.
 * Contains the status of the operation and any resulting data.
 */
public interface ComputationResult {
    /**
     * Check if the computation was successful.
     * 
     * @return True if successful, false otherwise
     */
    boolean isSuccess();
    
    /**
     * Get the list of factors from the computation.
     * 
     * @return The list of factors or null if computation failed
     */
    List<Integer> getFactors();
    
    /**
     * Get the status message.
     * 
     * @return The status message
     */
    String getMessage();
}
