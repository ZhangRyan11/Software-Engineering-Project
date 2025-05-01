package api;

import java.util.List;

/**
 * API for computation operations.
 */
public interface ComputationAPI {
    /**
     * Computes results based on input data and specified delimiters.
     * 
     * @param inputData The input data to process
     * @param delimiters The delimiters used in the input data
     * @return The computation result
     */
    ComputationResult compute(String inputData, String[] delimiters);
    
    /**
     * Finds the maximum value in a list of numbers.
     * 
     * @param numbers The list of numbers to search
     * @return The maximum value
     */
    double findMaximum(List<Double> numbers);
}

