package api;


public interface ComputeRequest {
    String getSourcePath();
    String getDestinationPath();
    String[] getDelimiters();
	String getSource();
	String getDestination();
}
