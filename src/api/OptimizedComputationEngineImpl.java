package api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An optimized implementation of ComputationAPI with improved performance.
 */
public class OptimizedComputationEngineImpl implements ComputationAPI {
    
    /**
     * Computes results based on input data and specified delimiters.
     * 
     * @param inputData The input data to process
     * @param delimiters The delimiters used in the input data
     * @return The computation result
     */
    @Override
    public ComputationResult compute(String inputData, String[] delimiters) {
        try {
            int number = parseInput(inputData);
            List<Integer> factors = findFactorsOptimized(number);
            return new ComputationResultImpl(true, factors);
        } catch (NumberFormatException e) {
            return new ComputationResultImpl(false, null);
        }
    }
    
    /**
     * Finds the maximum value in a list of numbers.
     * 
     * @param numbers List of numbers to find maximum from
     * @return The maximum value or null if the list is empty
     */
    @Override
    public Double findMaximum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return null;
        }
        return Collections.max(numbers);
    }

    // Parse input string to integer
    private int parseInput(String input) {
        return Integer.parseInt(input.trim());
    }

    /**
     * Optimized method to find factors by only checking up to the square root.
     * For each factor found, its pair (number/factor) is also a factor.
     * 
     * @param number The number to find factors for
     * @return List of all factors of the number
     */
    private List<Integer> findFactorsOptimized(int number) {
        List<Integer> factors = new ArrayList<>();
        
        // Only need to check up to the square root
        int sqrt = (int) Math.sqrt(number);
        
        // Find factors up to square root
        for (int i = 1; i <= sqrt; i++) {
            if (number % i == 0) {
                // Add the factor
                factors.add(i);
                
                // Add the corresponding factor (except when they're the same)
                if (i != number / i) {
                    factors.add(number / i);
                }
            }
        }
        
        // Sort the factors for consistent output
        factors.sort(Integer::compareTo);
        
        return factors;
    }
}
