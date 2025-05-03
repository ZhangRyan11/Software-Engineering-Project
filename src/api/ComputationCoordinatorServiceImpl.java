package api;

import coordinator.AbstractCoordinator;
import coordinator.MultiThreadedCoordinator; // Use your existing multithreaded coordinator
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import coordinatorservice.ComputationCoordinatorGrpc.ComputationCoordinatorImplBase;
import coordinatorservice.ComputationResponse;
import coordinatorservice.Coordinator;
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
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationCoordinatorServiceImpl extends ComputationCoordinatorImplBase {
    private final ConcurrentHashMap<String, JobStatus> jobStatus = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    
    // Use your existing MultiThreadedCoordinator
    private final MultiThreadedCoordinator coordinator = new MultiThreadedCoordinator();
    
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
        request.getId();
        DeleteCoordinatorResponse response = DeleteCoordinatorResponse.newBuilder()
            .setSuccess(true)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitNumberList(NumberListRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        
        try {
            // Create a temporary file for input
            String tempInputPath = "temp_input_" + jobId + ".txt";
            String outputPath = request.getOutputFile();
            
            // Write numbers to the file
            try (FileOutputStream out = new FileOutputStream(tempInputPath)) {
                for (double number : request.getNumbersList()) {
                    out.write((String.valueOf(number) + "\n").getBytes());
                }
            }
            
            // Update job status
            JobStatus status = JobStatus.newBuilder()
                    .setStatus(JobStatus.JobStatusType.PENDING)
                    .setMessage("Job queued for processing")
                    .setProgress(0.0)
                    .setCompleted(false)
                    .build();
            jobStatus.put(jobId, status);
            
            // Submit the job to be processed by the executor service
            executor.submit(() -> {
                try {
                    // Update status to processing
                    jobStatus.put(jobId, JobStatus.newBuilder()
                            .setStatus(JobStatus.JobStatusType.PROCESSING)
                            .setMessage("Processing computation")
                            .setProgress(0.5)
                            .setCompleted(false)
                            .build());
                    
                    // Use existing coordinator to process the computation
                    coordinator.startComputation(tempInputPath, outputPath, 
                            request.getDelimiter().charAt(0));
                    
                    // Update status to completed
                    jobStatus.put(jobId, JobStatus.newBuilder()
                            .setStatus(JobStatus.JobStatusType.COMPLETED)
                            .setMessage("Computation completed successfully")
                            .setProgress(1.0)
                            .setCompleted(true)
                            .setSuccess(true)
                            .build());
                    
                    // Clean up temporary input file
                    Files.deleteIfExists(Paths.get(tempInputPath));
                    
                } catch (Exception e) {
                    jobStatus.put(jobId, JobStatus.newBuilder()
                            .setStatus(JobStatus.JobStatusType.FAILED)
                            .setMessage("Computation failed: " + e.getMessage())
                            .setProgress(1.0)
                            .setCompleted(true)
                            .setSuccess(false)
                            .build());
                }
            });
            
            // Send success response with job ID
            responseObserver.onNext(ComputationResponse.newBuilder()
                    .setJobId(jobId)
                    .setSuccess(true)
                    .setMessage("Job submitted successfully")
                    .build());
            
        } catch (Exception e) {
            // Send failure response
            responseObserver.onNext(ComputationResponse.newBuilder()
                    .setJobId(jobId)
                    .setSuccess(false)
                    .setMessage("Failed to submit job: " + e.getMessage())
                    .build());
        }
        
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
        JobStatus status = jobStatus.getOrDefault(jobId, JobStatus.newBuilder()
                .setStatus(JobStatus.JobStatusType.UNKNOWN)
                .setMessage("Job ID not found")
                .setCompleted(true)
                .setSuccess(false)
                .build());
        
        StatusResponse.Builder responseBuilder = StatusResponse.newBuilder()
                .setJobStatus(status)
                .setCompleted(status.getCompleted())
                .setSuccess(status.getSuccess());
        
        // If job completed successfully, read the results from the output file
        if (status.getCompleted() && status.getSuccess()) {
            // Here we would add the results to the response
            // This is simplified as the actual results would come from parsing the output file
            // ...
        }
        
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
