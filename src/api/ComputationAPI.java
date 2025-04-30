package api;

import java.util.List;
import project.annotations.ProcessAPI;

@ProcessAPI
public interface ComputationAPI {
    /**
     * Finds all factors of a given number.
     * 
     * @param number The number to find factors for
     * @return List of factors
     */
    List<Integer> findFactors(int number);
    
    /**
     * Calculates the sum of a list of numbers.
     * 
     * @param numbers List of numbers to sum
     * @return The sum
     */
    double calculateSum(List<Double> numbers);
    
    /**
     * Calculates the average of a list of numbers.
     * 
     * @param numbers List of numbers to average
     * @return The average
     */
    double calculateAverage(List<Double> numbers);
    
    /**
     * Finds the minimum value in a list of numbers.
     * 
     * @param numbers List to search
     * @return The minimum value
     */
    double findMinimum(List<Double> numbers);
    
    /**
     * Finds the maximum value in a list of numbers.
     * 
     * @param numbers List to search
     * @return The maximum value
     */
    double findMaximum(List<Double> numbers);
}

