package project.annotations;
import project.exceptions.ValidationException;
import java.io.File;

// Once file is complete we may need to get rid of older version of this program

public class UserComputeAPIPrototype {
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
        // Thows same exception from earlier not allowing destination to be null or empty
        if (destination == null || destination.trim().isEmpty()) {
            throw new ValidationException("Output destination cannot be null or empty");
        }
        // If the exceprtion is not thrown, destination is set
        this.outputDestination = destination;
    }
}
