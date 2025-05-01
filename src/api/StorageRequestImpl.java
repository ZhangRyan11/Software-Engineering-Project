package api;

/**
 * Implementation of StorageRequest for file operations.
 */
public class StorageRequestImpl implements StorageRequest {
    private String source;
    private String[] delimiters;
    
    /**
     * Creates a new StorageRequestImpl.
     * 
     * @param source The source path
     * @param delimiter The delimiter for parsing
     */
    public StorageRequestImpl(String source, String delimiter) {
        this.source = source;
        this.delimiters = new String[]{ delimiter };
    }
    
    @Override
    public String getPath() {
        return source;
    }
    
    @Override
    public String[] getParams() {
        return delimiters;
    }
    
    @Override
    public String getSource() {
        return source;
    }
    
    @Override
    public String[] getDelimiters() {
        return delimiters;
    }
}
