package api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the StorageAPI.
 */
public class FileDataStorage implements StorageAPI {
    
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
            
            return new StorageResponse(numbers, true);
            
        } catch (IOException e) {
            return new StorageResponse(numbers, false);
        }
    }

    @Override
    public boolean writeData(String destination, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            writer.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
