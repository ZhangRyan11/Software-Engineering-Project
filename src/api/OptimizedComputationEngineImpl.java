package api;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Optimized implementation of the ComputationAPI that uses a more efficient
 * algorithm for finding factors by only checking up to the square root of the input number.
 */
public class OptimizedComputationEngineImpl implements ComputationAPI {
    
    @Override
    public ComputationResult compute(String inputData, String[] delimiters) {
        try {
            int number = parseInput(inputData);
            List<Integer> factors = findFactors(number);
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
    
    @Override
    public List<Integer> findFactors(int number) {
        return findFactorsOptimized(number);
    }
    
    @Override
    public double calculateSum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (Double num : numbers) {
            sum += num;
        }
        return sum;
    }
    
    @Override
    public double calculateAverage(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        return calculateSum(numbers) / numbers.size();
    }
    
    @Override
    public double findMinimum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        return Collections.min(numbers);
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
