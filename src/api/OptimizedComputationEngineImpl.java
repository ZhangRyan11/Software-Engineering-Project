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
     * @param numbers The list of numbers to search
     * @return The maximum value
     */
    @Override
    public double findMaximum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        return Collections.max(numbers);
    }
    
    private int parseInput(String inputData) {
        return Integer.parseInt(inputData.trim());
    }
    
    private List<Integer> findFactorsOptimized(int number) {
        List<Integer> factors = new ArrayList<>();
        int sqrt = (int) Math.sqrt(number);
        
        for (int i = 1; i <= sqrt; i++) {
            if (number % i == 0) {
                factors.add(i);
                if (i != number / i) {
                    factors.add(number / i);
                }
            }
        }
        Collections.sort(factors);
        return factors;
    }
}
