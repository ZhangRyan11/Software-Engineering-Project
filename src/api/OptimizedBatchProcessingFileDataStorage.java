package api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OptimizedBatchProcessingFileDataStorage implements DataStorage, StorageAPI {
    private static final ConcurrentHashMap<String, String> FILE_CACHE = new ConcurrentHashMap<>();
    private static final int BATCH_SIZE = 1000;
    private String source = "";

    public List<Integer> readNumbers(ComputeRequest request) {
        // Implementation details
        return new ArrayList<>();
    }

    public String getSource() {
        return source;
    }

    public boolean writeData(String destination, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            writer.write(data);
            FILE_CACHE.put(destination, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readData(String source, String[] delimiters) {
        this.source = source;
        // Check cache first
        String cachedContent = FILE_CACHE.get(source);
        if (cachedContent != null) {
            return cachedContent;
        }

        // Not in cache, read in batches
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            char[] buffer = new char[BATCH_SIZE];
            int charsRead;
            
            while ((charsRead = reader.read(buffer, 0, buffer.length)) != -1) {
                content.append(buffer, 0, charsRead);
            }
            
            String result = content.toString();
            FILE_CACHE.put(source, result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Batch read data from multiple sources.
     * 
     * @param sources List of source paths
     * @param delimiters Array of delimiters
     * @return List of contents from each source
     */
    public List<String> batchReadData(List<String> sources, String[] delimiters) {
        List<String> results = new ArrayList<>();
        
        for (String source : sources) {
            results.add(readData(source, delimiters));
        }
        
        return results;
    }

    /**
     * Batch write data to multiple destinations.
     * 
     * @param writeOperations Map of destination paths to content
     * @return true if all operations succeeded, false otherwise
     */
    public boolean batchWriteData(Map<String, String> writeOperations) {
        boolean allSucceeded = true;
        
        for (Map.Entry<String, String> entry : writeOperations.entrySet()) {
            if (!writeData(entry.getKey(), entry.getValue())) {
                allSucceeded = false;
            }
        }
        
        return allSucceeded;
    }

    /**
     * Clears the file content cache.
     */
    public void clearCache() {
        FILE_CACHE.clear();
    }

    public void writeDataContent(String destination, String content) {
        writeData(destination, content);
    }
}
