package api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Standard implementation of the ComputationAPI for performing calculations.
 */
public class ComputationEngineImpl implements ComputationAPI {

    @Override
    public List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        
        // Find all factors
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        
        return factors;
    }

    @Override
    public double calculateSum(List<Double> numbers) {
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
        double sum = calculateSum(numbers);
        return sum / numbers.size();
    }

    @Override
    public double findMinimum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        return Collections.min(numbers);
    }

    @Override
    public double findMaximum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        return Collections.max(numbers);
    }
}
