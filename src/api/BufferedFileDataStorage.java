package api;

import java.io.*;
import java.nio.file.*;

public class BufferedFileDataStorage implements DataStorage {
    private static final int BUFFER_SIZE = 8192;

    @Override
    public String readData(String source, String[] delimiters) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(source))) {
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
        }
        return content.toString();
    }

    @Override
    public void writeData(String destination, String data) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(destination))) {
            writer.write(data);
        }
    }
}
