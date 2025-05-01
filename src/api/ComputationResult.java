package api;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the result of a computation operation.
 * Contains the status of the operation and any resulting data.
 */
public class ComputationResult {
    private boolean success;
    private String message;
    private List<String> results;
    
    /**
     * Default constructor creates an empty successful result.
     */
    public ComputationResult() {
        this.success = true;
        this.message = "Operation completed successfully";
        this.results = new ArrayList<>();
    }
    
    /**
     * Create a result with specific success status and message.
     * 
     * @param success Whether the operation succeeded
     * @param message Status message
     */
    public ComputationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.results = new ArrayList<>();
    }
    
    /**
     * Check if the computation was successful.
     * 
     * @return True if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Set the success status of the operation.
     * 
     * @param success The success status to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    /**
     * Get the status message.
     * 
     * @return The status message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Set the status message.
     * 
     * @param message The status message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Add a result string to the results list.
     * 
     * @param result The result to add
     */
    public void addResult(String result) {
        this.results.add(result);
    }
    
    /**
     * Get the list of results.
     * 
     * @return The list of results
     */
    public List<String> getResults() {
        return results;
    }
    
    /**
     * Set the list of results.
     * 
     * @param results The results to set
     */
    public void setResults(List<String> results) {
        this.results = results;
    }
}
