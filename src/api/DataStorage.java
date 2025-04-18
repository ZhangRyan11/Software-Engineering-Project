package api;

import java.io.IOException;


public interface DataStorage {
    String readData(String source, String[] delimiters) throws IOException;
    void writeData(String destination, String data) throws IOException;
}
