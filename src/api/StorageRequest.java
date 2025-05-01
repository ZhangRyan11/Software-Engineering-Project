package api;

/**
 * Request interface for reading data from storage.
 */
public interface StorageRequest {
    /**
     * Gets the source path for the data.
     * 
     * @return The source path
     */
    String getSource();
    
    /**
     * Gets the delimiter used for parsing the data.
     * 
     * @return The delimiter
     */
    String getDelimiter();

}
