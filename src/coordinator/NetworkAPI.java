package coordinator;

// Use fully qualified annotation name to avoid naming conflict
@project.annotations.NetworkAPI
public interface NetworkAPI {
    /**
     * Starts a computation job with the given parameters.
     * 
     * @param inputPath Path to the input file
     * @param outputPath Path to write output to
     * @param delimiter Character used to separate values in the input file
     */
    void startComputation(String inputPath, String outputPath, char delimiter);
}