package api;

import project.annotations.ConceptualAPI;

@ConceptualAPI
public interface StorageAPI {
    /**
     * Reads data from storage based on the provided request.
     * 
     * @param request The storage request containing path and parsing information
     * @return A response with the parsed data
     */
    StorageResponse readData(StorageRequest request);
    
    /**
     * Writes data to storage at the specified destination.
     * 
     * @param destination Path to write the data to
     * @param data The data to write
     * @return true if write was successful, false otherwise
     */
    boolean writeData(String destination, String data);
}
