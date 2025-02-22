package api;

import java.io.*;

public interface DataStorage {
    String readData(String source, String[] delimiters) throws IOException;
    void writeData(String destination, String data) throws IOException;
}
