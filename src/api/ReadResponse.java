package api;

import java.util.ArrayList;
import java.util.List;

public interface ReadResponse {
	List<Integer> data = new ArrayList<>(){};
	//Getting rid of this because it needs to be updated once it has the data
	//public static List<Integer> data = null;

	//I figure this would probably be needed at some point, couldn't hurt to have
	public List<Integer> getData();
}