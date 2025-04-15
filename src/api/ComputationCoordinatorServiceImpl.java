package grpc.example;

import io.grpc.stub.StreamObserver;
import coordinatorservice.CoordinatorServiceProto.*;
import coordinatorservice.ComputationCoordinatorGrpc.ComputationCoordinatorImplBase;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComputationCoordinatorServiceImpl extends ComputationCoordinatorImplBase {
    private final ConcurrentHashMap<String, StatusResponse> jobStatus = new ConcurrentHashMap<>();

    @Override
    public void submitNumberList(NumberListRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        ComputationResponse response = ComputationResponse.newBuilder()
            .setJobId(jobId)
            .setSuccess(true)
            .setMessage("Job submitted successfully")
            .build();
            
        // Store initial status
        jobStatus.put(jobId, StatusResponse.newBuilder()
            .setCompleted(false)
            .setSuccess(false)
            .setMessage("Processing")
            .build());
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitFile(FileRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        ComputationResponse response = ComputationResponse.newBuilder()
            .setJobId(jobId)
            .setSuccess(true)
            .setMessage("File submitted successfully")
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getStatus(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
        String jobId = request.getJobId();
        StatusResponse status = jobStatus.getOrDefault(jobId, 
            StatusResponse.newBuilder()
                .setCompleted(false)
                .setSuccess(false)
                .setMessage("Job not found")
                .build());
                
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }
}
