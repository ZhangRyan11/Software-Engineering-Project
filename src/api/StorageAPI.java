package api;
import project.annotations.ProcessAPI;

@ProcessAPI
public interface StorageAPI {
    StorageResponse readData(StorageRequest request);
    void writeData(String destination, String data);
}
