package api;
import java.util.ArrayList;
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
            List<Integer> factors = findFactorsOptimized(number);
            return new ComputationResultImpl(true, factors);
        } catch (NumberFormatException e) {
            return new ComputationResultImpl(false, null);
        }
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
