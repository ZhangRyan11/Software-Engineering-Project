package api;
import project.exceptions.ValidationException;
import java.io.File;

public class UserComputeAPIException{
    // Getting 3 blank variables to use later
    private String inputSource;
    private String outputDestination;
    private String delimiters;

    public void setInputSource(String source) {
        // Below 2 lines are exception to not allow a null or empty source
        if (source == null || source.trim().isEmpty()) {
            throw new ValidationException("Input source cannot be null or empty");
        }
        // Below 2 lines are exception to take care of a file that does not exist
        if (!new File(source).exists()) {
            throw new ValidationException("Input source file does not exist: " + source);
        }
        // If exceptions are not thrown, the source is set 
        this.inputSource = source;
    }
    
    public void setOutputDestination(String destination) {
        // Throws same exception from earlier not allowing destination to be null or empty
        if (destination == null || destination.trim().isEmpty()) {
            throw new ValidationException("Output destination cannot be null or empty");
        }
        // If the exception is not thrown, destination is set
        this.outputDestination = destination;
    }
    
    public void setDelimiters(String delimiters) {
        // All delimiter values are valid - null or empty means use defaults
        this.delimiters = delimiters;
    }
    
    public String processRequest() {
        try {
            // Validate state before processing (both source and destination)
            if (inputSource == null) {
                return "ERROR: Input source not set";
            }
            if (outputDestination == null) {
                return "ERROR: Output destination not set";
            }
            // Process the request
            return "SUCCESS: Processing complete";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
