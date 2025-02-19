package api;

import java.io.IOException;
import java.util.List;



public class PrototypeStorage {


    public PrototypeStorage() {

    }

    public void prototype(PrototypeStorage ds) throws IOException {
        ComputeRequest computeRequest = new ComputeRequestImpl(null, null, null, null, null);

        // Sends the DataStorage a read request with the required information and receives a response with the input data. 
        ReadResponse readResponse = ds.read(new ReadRequest(computeRequest));

        // Sends a write request with the factor result and the destination and receives some kind of confirmation. 
        WriteResponse writeResponse = ds.write(new WriteRequest(computeRequest));
    }


    public ReadResponse read(ReadRequest request) {

    	ReadResponse readResponse = new ReadResponseImpl();
    	List<Integer> data = readResponse.getData();
		return readResponse;

    }

    public WriteResponse write(WriteRequest request) {
        // Implementation here
        return new WriteResponse();
    }
}
