package project.api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Retention(RetentionPolicy.RUNTIME)
@interface ConceptualAPI {}

@ConceptualAPI
public interface ComputeEngineAPI {
    // Submit a new job for processing
    String submitJob(String computationData);

    // Get the current status of a job
    JobStatus getJobStatus(String jobId);

    // Get list of all jobs
    List<ComputeJob> getAllJobs();

    // Trigger execution of a specific job
    boolean executeJob(String jobId);

    // Cancel a running or queued job
    boolean cancelJob(String jobId);
}