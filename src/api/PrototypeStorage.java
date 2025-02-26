package api;

import java.io.IOException;



public class PrototypeStorage {


    public PrototypeStorage() {

    }

    public void prototype(PrototypeStorage ds) throws IOException {
        ComputeRequest computeRequest = new ComputeRequestImpl(null, null, null, null, null);

        ds.read(new ReadRequest(computeRequest));

        ds.write(new WriteRequest(computeRequest));
    }


    public ReadResponse read(ReadRequest request) {

    	ReadResponse readResponse = new ReadResponseImpl();
    	readResponse.getData();
		return readResponse;

    }

    public WriteResponse write(WriteRequest request) {
        // Implementation here
        return new WriteResponse();
    }
}
