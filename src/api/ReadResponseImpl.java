package api;

import java.util.ArrayList;
import java.util.List;

public class ReadResponseImpl implements ReadResponse {  
    private List<Integer> data;

    public ReadResponseImpl() {
        this.data = new ArrayList<>();
    }

    public ReadResponseImpl(List<Integer> data) {
        this.data = data;
    }

    @Override
    public List<Integer> getData() {
        return data;
    }
}
