package api;

import java.util.ArrayList;
import java.util.List;

public class ReadResponseImpl implements ReadResponse {  // ✅ Change interface to class
    private List<Integer> data;

    public ReadResponseImpl() {
        this.data = new ArrayList<>();  // ✅ Initialize list in constructor
    }

    public ReadResponseImpl(List<Integer> data) {
        this.data = data;  // ✅ Assign provided list
    }

    @Override
    public List<Integer> getData() {
        return data;
    }
}
