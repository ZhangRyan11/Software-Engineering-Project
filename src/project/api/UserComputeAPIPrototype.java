import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Define the annotation for marking prototype classes
@Retention(RetentionPolicy.RUNTIME)
@interface NetworkAPIPrototype {}

// Add JobStatus enum before the UserComputeAPI interface
enum JobStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED
}

// Define the interface that the prototype implements
interface UserComputeAPI {
    void setInputSource(String source);
    void setOutputDestination(String destination);
    void setDelimiters(String delimiters);
    String processRequest();
    String submitJob(String computationData);
    JobStatus getJobStatus(String jobId);
    boolean executeJob(String jobId);
}

// Implement the prototype class
@NetworkAPIPrototype
public class UserComputeAPIPrototype implements UserComputeAPI {
    private String inputSource = "default_input";
    private String outputDestination = "default_output";
    private String delimiters = ",";
    private Map<String, JobStatus> jobs = new ConcurrentHashMap<>();
    private Map<String, String> jobData = new ConcurrentHashMap<>();

    @Override
    public void setInputSource(String source) {
        this.inputSource = source;
    }

    @Override
    public void setOutputDestination(String destination) {
        this.outputDestination = destination;
    }

    @Override
    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters;
    }

    @Override
    public String processRequest() {
        // Update to use job-based processing
        String jobId = submitJob("default_computation");
        executeJob(jobId);
        return "Processing request with prototype - Job ID: " + jobId;
    }

    @Override
    public String submitJob(String computationData) {
        String jobId = UUID.randomUUID().toString();
        jobs.put(jobId, JobStatus.QUEUED);
        jobData.put(jobId, computationData);
        return jobId;
    }

    @Override
    public JobStatus getJobStatus(String jobId) {
        return jobs.get(jobId);
    }

    @Override
    public boolean executeJob(String jobId) {
        if (!jobs.containsKey(jobId)) {
            return false;
        }
        
        jobs.put(jobId, JobStatus.RUNNING);
        new Thread(() -> {
            try {
                // Simulate processing
                Thread.sleep(1000);
                processJobData(jobId);
                jobs.put(jobId, JobStatus.COMPLETED);
            } catch (Exception e) {
                jobs.put(jobId, JobStatus.FAILED);
            }
        }).start();
        return true;
    }

    private void processJobData(String jobId) {
        String data = jobData.get(jobId);
        // Process the job data using existing settings
        // (inputSource, outputDestination, delimiters)
    }

    // Method to test if updates work correctly
    public String getStatus() {
        // Update status to include job information
        return String.format("InputSource: %s, OutputDestination: %s, Delimiters: %s, Active Jobs: %d",
            inputSource, outputDestination, delimiters, jobs.size());
    }

    // Main method for quick testing
    public static void main(String[] args) {
        UserComputeAPIPrototype api = new UserComputeAPIPrototype();
        String jobId = api.submitJob("test computation");
        System.out.println("Submitted job: " + jobId);
        api.executeJob(jobId);
        System.out.println("Job status: " + api.getJobStatus(jobId));
        System.out.println(api.getStatus());
    }
}
