package api;

import java.io.*;
import java.nio.file.*;

public class FileDataStorage implements DataStorage {
    @Override
    public String readData(String source, String[] delimiters) throws IOException {
        return Files.readString(Paths.get(source));
    }

    @Override
    public void writeData(String destination, String data) throws IOException {
        Files.writeString(Paths.get(destination), data);
    }
}
