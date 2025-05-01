package api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File-based implementation of the StorageAPI and DataStorage interfaces.
 */
public class FileDataStorage implements StorageAPI, DataStorage {
    
    @Override
    public StorageResponse readData(StorageRequest request) {
        List<Integer> numbers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(request.getSource()))) {
            String line;
            String delimiter = request.getDelimiter();
            
            while ((line = reader.readLine()) != null) {
                String[] values = delimiter.isEmpty() ? 
                        new String[]{line} : line.split(delimiter);
                
                for (String value : values) {
                    if (!value.trim().isEmpty()) {
                        try {
                            numbers.add(Integer.parseInt(value.trim()));
                        } catch (NumberFormatException e) {
                            // Skip non-integer values
                        }
                    }
                }
            }
            
            return new StorageResponseImpl(numbers, true);
            
        } catch (IOException e) {
            return new StorageResponseImpl(numbers, false);
        }
    }

    /**
     * Implements the StorageAPI interface writeData method.
     * 
     * @param destination Path to write data to
     * @param data Data to write
     * @return true if successful, false otherwise
     */
    @Override
    public boolean writeData(String destination, String data) {
        try {
            doWriteData(destination, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    
    /**
     * Common implementation for writing data.
     */
    private void doWriteData(String destination, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            writer.write(data);
        }
    }
    
    @Override
    public String readData(String source, String[] delimiters) throws IOException {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    /**     * Implements the DataStorage interface writeDataContent method.     *      * @param destination The destination path     * @param data The data to write     * @throws IOException If an I/O error occurs     */    @Override    public void writeDataContent(String destination, String data) throws IOException {        doWriteData(destination, data);
    }
}
