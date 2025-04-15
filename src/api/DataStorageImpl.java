package api;

import io.grpc.stub.StreamObserver;
import datastore.DataStorageGrpc;
import datastore.DatastoreService.ReadRequest;
import datastore.DatastoreService.ReadResponse;
import datastore.DatastoreService.WriteRequest;
import datastore.DatastoreService.WriteResponse;

public class DataStorageImpl extends DataStorageGrpc.DataStorageImplBase {
    private final DataStoreImpl dataStore;
    
    public DataStorageImpl(DataStoreImpl dataStore) {
        this.dataStore = dataStore;
    }
    
    @Override
    public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
        // Implement read operation using dataStore
        String key = request.getKey();
        String value = dataStore.read(key);
        
        ReadResponse response = ReadResponse.newBuilder()
            .setValue(value)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void write(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {
        // Implement write operation using dataStore
        String key = request.getKey();
        String value = request.getValue();
        boolean success = dataStore.write(key, value);
        
        WriteResponse response = WriteResponse.newBuilder()
            .setSuccess(success)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
