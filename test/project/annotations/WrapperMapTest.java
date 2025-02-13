package project.annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class WrapperMapTest {

	public void testMap() {
		String value = "five";
		int key = 5;
		WrappedHashMap map = new WrappedHashMap();
		map.put(key,value);
		Assertions.assertEquals(value, map.get(key));
		
	}
	
}
