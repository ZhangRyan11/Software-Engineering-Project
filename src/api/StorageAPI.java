package api;

public interface StorageAPI {
    String getSource();
    boolean writeData(String destination, String data);
    String readData(String source, String[] delimiters);
}
