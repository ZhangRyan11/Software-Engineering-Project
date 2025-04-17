package api;

import com.example.datastore.DataStorageGrpc.DataStorageImplBase;
import com.example.datastore.DataRequest;
import com.example.datastore.DataResponse; 
import com.example.datastore.Data;
import com.example.datastore.SaveDataResponse;

import io.grpc.stub.StreamObserver;

public class DataStorageImpl extends DataStorageImplBase {
    private final DataStoreImpl dataStore;
    
    public DataStorageImpl(DataStoreImpl dataStore) {
        this.dataStore = dataStore;
    }
    
    @Override
    public void getData(DataRequest request, StreamObserver<DataResponse> responseObserver) {
        String id = request.getId();
        String value = dataStore.read(id);
        
        Data data = Data.newBuilder()
            .setId(id)
            .setValue(value)
            .build();
            
        DataResponse response = DataResponse.newBuilder()
            .setData(data)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void saveData(Data request, StreamObserver<SaveDataResponse> responseObserver) {
        String id = request.getId();
        String value = request.getValue();
        boolean success = dataStore.write(id, value);
        
        SaveDataResponse response = SaveDataResponse.newBuilder()
            .setSuccess(success)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
