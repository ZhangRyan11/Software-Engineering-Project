package api;

import java.util.ArrayList;
import java.util.List;

public interface ReadResponse {
	List<Integer> data = new ArrayList<>(){};


	public List<Integer> getData();
}