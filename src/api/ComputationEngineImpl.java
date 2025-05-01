package api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Standard implementation of the ComputationAPI.
 */
public class ComputationEngineImpl implements ComputationAPI {

    @Override
    public ComputationResult compute(String inputData, String[] delimiters) {
        try {
            int number = parseInput(inputData);
            List<Integer> factors = findFactors(number);
            return new ComputationResultImpl(true, factors);
        } catch (NumberFormatException e) {
            return new ComputationResultImpl(false, null, "Invalid input data: " + e.getMessage());
        }
    }

    @Override
    public List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
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
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        double max = Double.NEGATIVE_INFINITY;
        for (Double number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    protected int parseInput(String inputData) {
        return Integer.parseInt(inputData.trim());
    }

    protected List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }
}
