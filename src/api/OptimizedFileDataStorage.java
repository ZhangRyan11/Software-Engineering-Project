package api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OptimizedFileDataStorage implements DataStorage, StorageAPI {
    private static final ConcurrentHashMap<String, String> FILE_CACHE = new ConcurrentHashMap<>();
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
        
        // Not in cache, read from file
        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            String result = content.toString();
            FILE_CACHE.put(source, result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void writeDataContent(String destination, String content) {
        writeData(destination, content);
    }
}
