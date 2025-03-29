package api;

// Define the interface that the prototype implements
public interface UserComputeAPI {
    void setInputSource(String source);
    void setOutputDestination(String destination);
    void setDelimiters(String delimiters);
    String processRequest();
}
