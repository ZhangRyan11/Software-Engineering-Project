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

public class ComputeEngineAPIImpl implements ComputeEngineAPI {
    private final Map<String, ComputeJob> jobs = new ConcurrentHashMap<>();

    @Override
    public String submitJob(String computationData) {
        String jobId = UUID.randomUUID().toString();
        ComputeJob job = new ComputeJob(jobId, computationData);
        jobs.put(jobId, job);
        return jobId;
    }

    @Override
    public JobStatus getJobStatus(String jobId) {
        ComputeJob job = jobs.get(jobId);
        return job != null ? job.getStatus() : null;
    }

    @Override
    public List<ComputeJob> getAllJobs() {
        return new ArrayList<>(jobs.values());
    }

    @Override
    public boolean executeJob(String jobId) {
        ComputeJob job = jobs.get(jobId);
        if (job != null && job.getStatus() == JobStatus.QUEUED) {
            job.setStatus(JobStatus.RUNNING);
            // Simulate job execution
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simulate work
                    job.setStatus(JobStatus.COMPLETED);
                } catch (InterruptedException e) {
                    job.setStatus(JobStatus.FAILED);
                }
            }).start();
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelJob(String jobId) {
        ComputeJob job = jobs.get(jobId);
        if (job != null && (job.getStatus() == JobStatus.QUEUED || job.getStatus() == JobStatus.RUNNING)) {
            job.setStatus(JobStatus.FAILED);
            return true;
        }
        return false;
    }
}
