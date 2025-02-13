package project.annotations;

import java.util.*;

public class WrappedHashMap {
	
	private Map<Integer, String> data;
	private boolean needToInitialize;

	public String get(int id){
		if(needToInitialize){
			initalize();
			needToInitialize = false;
		}
		return data.get(id);
	}
	
		public void put(int id, String value){
		if(needToInitialize){
			initalize();
			needToInitialize = false;
			}
		data.put(id, value);
	
		}
		
		private void initalize() {
			int initialCapacity = 10;
			data = new HashMap<>(initialCapacity);
		}
}
