package api;

// Define the interface that the prototype implements
public interface UserComputeAPI {
    void setInputSource(String source) throws ValidationException, project.annotations.ValidationException;
    void setOutputDestination(String destination) throws ValidationException, project.annotations.ValidationException;
    void setDelimiters(String delimiters);
    String processRequest();
}
