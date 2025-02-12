package api;

import java.util.ArrayList;
import java.util.List;

public class ReadResponseImpl implements ReadResponse {
    private List<Integer> data;

    public ReadResponseImpl() {
        this.data = new ArrayList<>();  // Initialize empty list
    }

    public ReadResponseImpl(List<Integer> data) {
        this.data = data;  // Assign given list
    }

    @Override
    public List<Integer> getData() {
        return data;
    }
}
