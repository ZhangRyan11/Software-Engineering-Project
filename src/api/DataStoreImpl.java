package api;

import java.util.concurrent.ConcurrentHashMap;

public class DataStoreImpl {
    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();
    
    public String read(String key) {
        return store.getOrDefault(key, "");
    }
    
    public boolean write(String key, String value) {
        store.put(key, value);
        return true;
    }
}
