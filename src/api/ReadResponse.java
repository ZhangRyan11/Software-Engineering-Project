package api;

import java.util.ArrayList;
import java.util.List;

public interface ReadResponse {
	List<Integer> data = new ArrayList<>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;};


	public List<Integer> getData();
}