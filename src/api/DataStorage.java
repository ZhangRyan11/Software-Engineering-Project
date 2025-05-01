package api;

import java.io.IOException;

/**
 * Interface for data storage operations
 */
public interface DataStorage {
    /**
     * Read data from a source
     * @param source Path to read from
     * @param delimiters Array of delimiters to use when parsing
     * @return String containing the read data
     * @throws IOException If an I/O error occurs
     */
    String readData(String source, String[] delimiters) throws IOException;
    
    /**
     * Write content to a destination
     * @param destination Path to write data to
     * @param data Data to write
     * @throws IOException If an I/O error occurs
     */
    void writeDataContent(String destination, String data) throws IOException;
    
    /**
     * Write data to a destination with error handling
     * @param destination Path to write data to
     * @param data Data to write
     * @return true if successful, false otherwise
     */
    default boolean writeData(String destination, String data) {
        try {
            writeDataContent(destination, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
