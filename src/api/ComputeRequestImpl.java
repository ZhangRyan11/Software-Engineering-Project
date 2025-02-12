package api;

public class ComputeRequestImpl implements ComputeRequest {
    private String sourcePath;
    private String destinationPath;
    private String[] delimiters;
    private String source;
    private String destination;

    public ComputeRequestImpl(String sourcePath, String destinationPath, String[] delimiters, String source, String destination) {
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
        this.delimiters = delimiters;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String getSourcePath() {
        return sourcePath;
    }

    @Override
    public String getDestinationPath() {
        return destinationPath;
    }

    @Override
    public String[] getDelimiters() {
        return delimiters;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getDestination() {
        return destination;
    }
}
