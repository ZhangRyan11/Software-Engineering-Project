package api;

import java.io.IOException;

public class PrototypeStorage {

	public void prototype({PrototypeStorage ds) throws IOException {
		
		ComputeRequest computeRequest = new ComputeRequest(null,null,null, null);
		
		//Sends the DataStorage a read request with the required information and Receive a response with the input data. 
		ReadResponse readResponse = ds.read(new ReadRequest(computeRequest));
		
		//Sends a write request with the factor result and the destination and Receive some kind of confirmation. 
		WriteResponse writeResponse = ds.write(new WriteRequest(computeRequest));
		
	}
	
}