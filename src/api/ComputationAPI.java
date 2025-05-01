package api;

import java.util.List;
import project.annotations.ConceptualAPI;

/**
 * API for computation operations.
 */
@ConceptualAPI
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
     * Finds the factors of a given number.
     * 
     * @param number The number to find factors for
     * @return List of factors
     */
    List<Integer> findFactors(int number);
    
    /**
     * Calculates the sum of a list of numbers.
     * 
     * @param numbers The list of numbers to sum
     * @return The sum of the numbers
     */
    double calculateSum(List<Double> numbers);
    
    /**
     * Calculates the average of a list of numbers.
     * 
     * @param numbers The list of numbers to average
     * @return The average of the numbers
     */
    double calculateAverage(List<Double> numbers);
    
    /**
     * Finds the minimum value in a list of numbers.
     * 
     * @param numbers The list of numbers to search
     * @return The minimum value
     */
    double findMinimum(List<Double> numbers);
    
    /**
     * Finds the maximum value in a list of numbers.
     * 
     * @param numbers The list of numbers to search
     * @return The maximum value
     */
    double findMaximum(List<Double> numbers);
}

