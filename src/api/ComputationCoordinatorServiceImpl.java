package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import coordinatorservice.JobStatus.JobStatusType;
import coordinatorservice.NumberListRequest;
import coordinatorservice.StatusRequest;
import coordinatorservice.StatusResponse;
import coordinatorservice.UpdateCoordinatorRequest;
import coordinatorservice.UpdateCoordinatorResponse;
import io.grpc.stub.StreamObserver;

public class ComputationCoordinatorServiceImpl extends ComputationCoordinatorImplBase {
    private final ConcurrentHashMap<String, JobStatus> jobStatus = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Double>> jobResults = new ConcurrentHashMap<>();
    
    private final ComputationAPI computationEngine;
    private final StorageAPI dataStorage;
    private final ExecutorService executorService;
    
    public ComputationCoordinatorServiceImpl() {
        this.computationEngine = new ComputationEngineImpl();
        this.dataStorage = new FileDataStorage();
        this.executorService = Executors.newFixedThreadPool(4);
    }
    
    @Override
    public void getCoordinator(GetCoordinatorRequest request, StreamObserver<GetCoordinatorResponse> responseObserver) {
        GetCoordinatorResponse response = GetCoordinatorResponse.newBuilder()
            .setCoordinator(Coordinator.newBuilder().setId(request.getId()).build())
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createCoordinator(CreateCoordinatorRequest request, StreamObserver<CreateCoordinatorResponse> responseObserver) {
        CreateCoordinatorResponse response = CreateCoordinatorResponse.newBuilder()
            .setCoordinator(request.getCoordinator())
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCoordinator(UpdateCoordinatorRequest request, StreamObserver<UpdateCoordinatorResponse> responseObserver) {
        UpdateCoordinatorResponse response = UpdateCoordinatorResponse.newBuilder()
            .setCoordinator(request.getCoordinator())
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCoordinator(DeleteCoordinatorRequest request, StreamObserver<DeleteCoordinatorResponse> responseObserver) {
        DeleteCoordinatorResponse response = DeleteCoordinatorResponse.newBuilder()
            .setSuccess(true)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitNumberList(NumberListRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        
        JobStatus status = JobStatus.newBuilder()
            .setStatus(JobStatusType.PENDING)
            .setMessage("Job queued")
            .setProgress(0)
            .build();
        jobStatus.put(jobId, status);
        
        executorService.submit(() -> processNumberList(jobId, request));
        
        ComputationResponse response = ComputationResponse.newBuilder()
            .setJobId(jobId)
            .setSuccess(true)
            .setMessage("Job submitted successfully")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitFile(FileRequest request, StreamObserver<ComputationResponse> responseObserver) {
        String jobId = UUID.randomUUID().toString();
        
        JobStatus status = JobStatus.newBuilder()
            .setStatus(JobStatusType.PENDING)
            .setMessage("File job queued")
            .setProgress(0)
            .build();
        jobStatus.put(jobId, status);
        
        executorService.submit(() -> processFile(jobId, request));
        
        ComputationResponse response = ComputationResponse.newBuilder()
            .setJobId(jobId)
            .setSuccess(true)
            .setMessage("File job submitted successfully")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getStatus(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
        String jobId = request.getJobId();
                .setSuccess(false)
        JobStatus status = jobStatus.getOrDefault(jobId, JobStatus.newBuilder()
            .setStatus(JobStatusType.FAILED)
            .setMessage("Job not found")
            .setCompleted(true)
            .setSuccess(false)
            .build());
        
        StatusResponse.Builder responseBuilder = StatusResponse.newBuilder()
            .setJobStatus(status)
            .setCompleted(status.getCompleted())
            .setSuccess(status.getSuccess());
        
        if (status.getCompleted() && status.getSuccess() && jobResults.containsKey(jobId)) {
            responseBuilder.addAllResults(jobResults.get(jobId));
        }
        
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
    
    private void processNumberList(String jobId, NumberListRequest request) {
        try {
            updateJobStatus(jobId, JobStatusType.PROCESSING, "Processing number list", 10, false, false);
            
            List<Double> numbers = request.getNumbersList();
            List<Double> results = processNumbers(numbers);
            
            if (request.hasOutputFile()) {
                writeResultsToFile(request.getOutputFile(), results, request.getDelimiter());
            }
            
            jobResults.put(jobId, results);
            
            updateJobStatus(jobId, JobStatusType.COMPLETED, "Processing complete", 100, true, true);
            
        } catch (Exception e) {
            updateJobStatus(jobId, JobStatusType.FAILED, "Processing failed: " + e.getMessage(), 100, true, false);
        }
    }
    
    private void processFile(String jobId, FileRequest request) {
        try {
            updateJobStatus(jobId, JobStatusType.PROCESSING, "Processing file", 10, false, false);
            
            List<Double> numbers = readNumbersFromFile(request.getFilePath(), request.getDelimiter());
            
            List<Double> results = processNumbers(numbers);
            
            writeResultsToFile(request.getOutputFile(), results, request.getDelimiter());
            
            jobResults.put(jobId, results);
            
            updateJobStatus(jobId, JobStatusType.COMPLETED, "File processing complete", 100, true, true);
            
        } catch (Exception e) {
            updateJobStatus(jobId, JobStatusType.FAILED, "File processing failed: " + e.getMessage(), 100, true, false);
        }
    }
    
    private void updateJobStatus(String jobId, JobStatusType statusType, String message, double progress,
            boolean completed, boolean success) {
        JobStatus newStatus = JobStatus.newBuilder()
            .setStatus(statusType)
            .setMessage(message)
            .setProgress(progress)
            .setCompleted(completed)
            .setSuccess(success)
            .build();
        
        jobStatus.put(jobId, newStatus);
    }
    
    private List<Double> readNumbersFromFile(String filePath, String delimiter) throws IOException {
        List<Double> numbers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (delimiter != null && !delimiter.isEmpty()) {
                        String[] parts = line.split(delimiter);
                        for (String part : parts) {
                            part = part.trim();
                            if (!part.isEmpty()) {
                                numbers.add(Double.parseDouble(part));
                            }
                        }
                    } else {
                        numbers.add(Double.parseDouble(line));
                    }
                }
            }
        }
        
        return numbers;
    }
    
    private List<Double> processNumbers(List<Double> numbers) {
        List<Double> results = new ArrayList<>();
        
        double sum = computationEngine.calculateSum(numbers);
        results.add(sum);
        
        double avg = computationEngine.calculateAverage(numbers);
        results.add(avg);
        
        double min = computationEngine.findMinimum(numbers);
        results.add(min);
        
        double max = computationEngine.findMaximum(numbers);
        results.add(max);
        
        return results;
    }
    
    private void writeResultsToFile(String outputFile, List<Double> results, String delimiter) {
        if (delimiter == null || delimiter.isEmpty()) {
            delimiter = ",";
        }
        
        StringBuilder content = new StringBuilder();
        content.append("Sum: ").append(results.get(0)).append("\n");
        content.append("Average: ").append(results.get(1)).append("\n");
        content.append("Min: ").append(results.get(2)).append("\n");
        content.append("Max: ").append(results.get(3)).append("\n");
        
        dataStorage.writeData(outputFile, content.toString());
    }
}
