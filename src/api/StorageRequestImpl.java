package api;

/**
 * Implementation of StorageRequest for file operations.
 */
public class StorageRequestImpl implements StorageRequest {
    private String source;
    private String delimiter;
    
    /**
     * Creates a new StorageRequestImpl.
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
