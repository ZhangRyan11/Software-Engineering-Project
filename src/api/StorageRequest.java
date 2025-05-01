package api;

/**
 * Interface defining storage request operations.
 */
public interface StorageRequest {
    /**
     * Gets the path for the storage request.
     * 
     * @return The path as a String
     */
    String getPath();
    
    /**
     * Gets the parameters for the storage request.
     * 
     * @return An array of parameter strings
     */
    String[] getParams();
    
    /**
     * Gets the source for the storage request.
     * 
     * @return The source as a String
     */
    String getSource();
    
    /**
     * Gets the delimiters for the storage request.
     * 
     * @return An array of delimiter strings
     */
    String[] getDelimiters();
}
