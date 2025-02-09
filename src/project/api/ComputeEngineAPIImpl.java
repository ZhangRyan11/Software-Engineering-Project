package project.api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConceptualAPI
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
