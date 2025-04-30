package api;

/**
 * Request object for reading data from storage.
 */
public class StorageRequest {
    private final String source;
    private final String delimiter;
    
    public StorageRequest(String source, String delimiter) {
        this.source = source;
        this.delimiter = delimiter;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getDelimiter() {
        return delimiter;
    }
}
