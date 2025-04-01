package coordinator;

public interface NetworkAPI {
    /**
     * Starts a computation job with the given input path, output path, and delimiter.
     *
     * @param inputPath Path to the input file
     * @param outputPath Path where the output should be written
     * @param delimiter Character used to separate values in the input file
     */
    void startComputation(String inputPath, String outputPath, char delimiter);
}