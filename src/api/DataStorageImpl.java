package api;

import datastore.DataStorageGrpc;
import datastore.Datastoreservice.WriteResponseStatus;

public class DataStorageImpl extends DataStorageGrpc.DataStorageImplBase {
    private final DataStoreImpl dataStore;
    
    public DataStorageImpl(DataStoreImpl dataStore) {
        this.dataStore = dataStore;
    }
    
    @Override
    public void read(datastore.Datastoreservice.ReadRequest request, 
                    io.grpc.stub.StreamObserver<datastore.Datastoreservice.ReadResponse> responseObserver) {
        // Implement read operation using dataStore
        String key = request.getSource();
        String value = dataStore.read(key);
        
        datastore.Datastoreservice.ReadResponse.Builder responseBuilder = 
            datastore.Datastoreservice.ReadResponse.newBuilder();
            
        if (value != null) {
            // Convert string to ASCII values
            for (char c : value.toCharArray()) {
                responseBuilder.addData((int)c);
            }
        }
        
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void write(datastore.Datastoreservice.WriteRequest request, 
                     io.grpc.stub.StreamObserver<datastore.Datastoreservice.WriteResponse> responseObserver) {
        // Implement write operation using dataStore
        String key = request.getDestination();
        String value = request.getWriteData();
        boolean success = dataStore.write(key, value);
        
        datastore.Datastoreservice.WriteResponse response = 
            datastore.Datastoreservice.WriteResponse.newBuilder()
                .setStatus(success ? WriteResponseStatus.SUCCESS : WriteResponseStatus.FAILURE)
                .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
