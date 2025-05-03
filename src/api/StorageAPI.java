package api;

/**
 * Interface for data storage operations
 */
public interface StorageAPI {
    /**
     * Get the source of the data
     * @return The source as a string
     */
    String getSource();

    /**
     * Write data to the specified destination
     * @param destination The destination to write to (e.g., file path)
     * @param data The data to write
     * @return True if successful, false otherwise
     */
    boolean writeData(String destination, String data);

    /**
     * Read data from the specified source using the provided delimiters
     * @param source The source to read from (e.g., file path)
     * @param delimiters Delimiters to use for parsing
     * @return The raw data as a string
     */
    String readData(String source, String[] delimiters);

    /**
     * Parse raw string data into a StorageResponse
     * @param data The raw data to parse
     * @return StorageResponse containing parsed numbers
     */
    StorageResponse parseData(String data);
}
