package api;


public interface ComputeRequest {
    String getSourcePath();
    String getDestinationPath();
    String[] getDelimiters();
}
