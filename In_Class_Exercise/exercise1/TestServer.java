package exercise1;

import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class TestServer {

	@Test
	public void testAdding() {
		Server server = new Server();
		
		Request request = new Request(2, 3);
		
		// TODO: send the request to the server
		// TODO: check that the call was successful
		// TODO: save the result into 'result'
		
		int result = 0;
		Assert.assertEquals(5, result);
	}
}
