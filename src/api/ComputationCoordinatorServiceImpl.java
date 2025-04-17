package api;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import coordinatorservice.ComputationCoordinatorGrpc.ComputationCoordinatorImplBase;
import coordinatorservice.ComputationResponse;
import coordinatorservice.CreateCoordinatorRequest;
import coordinatorservice.CreateCoordinatorResponse;
import coordinatorservice.DeleteCoordinatorRequest;
import coordinatorservice.DeleteCoordinatorResponse;
import coordinatorservice.FileRequest;
import coordinatorservice.GetCoordinatorRequest;
import coordinatorservice.GetCoordinatorResponse;
import coordinatorservice.JobStatus;
import coordinatorservice.NumberListRequest;
import coordinatorservice.StatusRequest;
import coordinatorservice.StatusResponse;
import coordinatorservice.UpdateCoordinatorRequest;
import coordinatorservice.UpdateCoordinatorResponse;
import coordinatorservice.Coordinator;

import io.grpc.stub.StreamObserver;

public class ComputationCoordinatorServiceImpl extends ComputationCoordinatorImplBase {
    private final ConcurrentHashMap<String, JobStatus> jobStatus = new ConcurrentHashMap<>();
    
    @Override
    public void getCoordinator(GetCoordinatorRequest request, StreamObserver<GetCoordinatorResponse> responseObserver) {
        String id = request.getId();
        Coordinator coordinator = Coordinator.newBuilder()
            .setId(id)
            .setName("DefaultCoordinator")
            .setEmail("coordinator@example.com")
            .build();
            
        GetCoordinatorResponse response = GetCoordinatorResponse.newBuilder()
            .setCoordinator(coordinator)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createCoordinator(CreateCoordinatorRequest request, StreamObserver<CreateCoordinatorResponse> responseObserver) {
        Coordinator coordinator = request.getCoordinator();
        CreateCoordinatorResponse response = CreateCoordinatorResponse.newBuilder()
            .setCoordinator(coordinator)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCoordinator(UpdateCoordinatorRequest request, StreamObserver<UpdateCoordinatorResponse> responseObserver) {
        Coordinator coordinator = request.getCoordinator();
        UpdateCoordinatorResponse response = UpdateCoordinatorResponse.newBuilder()
            .setCoordinator(coordinator)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCoordinator(DeleteCoordinatorRequest request, StreamObserver<DeleteCoordinatorResponse> responseObserver) {
        String id = request.getId();
        DeleteCoordinatorResponse response = DeleteCoordinatorResponse.newBuilder()
            .setSuccess(true)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitNumberList(NumberListRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        ComputationResponse response = ComputationResponse.newBuilder()
            .setJobId(jobId)
            .setSuccess(true)
            .setMessage("Job submitted successfully")
            .build();
            
        // Store initial status
        jobStatus.put(jobId, JobStatus.newBuilder()
            .setStatus(JobStatus.JobStatusType.PENDING)
            .setMessage("Processing")
            .setProgress(0.0)
            .setCompleted(false)
            .setSuccess(false)
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
        JobStatus status = jobStatus.getOrDefault(jobId, 
            JobStatus.newBuilder()
                .setCompleted(false)
                .setSuccess(false)
                .setMessage("Job not found")
                .build());
                
        responseObserver.onNext(StatusResponse.newBuilder().setJobStatus(status).build());
        responseObserver.onCompleted();
    }
}
