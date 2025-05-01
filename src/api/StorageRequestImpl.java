package api;

/**
 * Standard implementation of the StorageRequest interface.
 */
public class StorageRequestImpl implements StorageRequest {
    private final String source;
    private final String delimiter;
    
    /**
     * Creates a new storage request with source path and delimiter.
     * 
     * @param source The source path
     * @param delimiter The delimiter for parsing
     */
    public StorageRequestImpl(String source, String delimiter) {
        this.source = source;
        this.delimiter = delimiter;
    }
    
    @Override
    public String getSource() {
        return source;
    }
    
    @Override
    public String getDelimiter() {
        return delimiter;
    }
}
