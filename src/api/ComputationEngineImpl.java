package api;

import java.util.ArrayList;
import java.util.List;

public class ComputationEngineImpl implements ComputationAPI {
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

    private int parseInput(String input) {
        return Integer.parseInt(input.trim());
    }

    private List<Integer> findFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        int square = number * number;
        
        for (int i = 1; i <= square; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }
}

class ComputationResultImpl implements ComputationResult {
    private final boolean success;
    private final List<Integer> factors;

    public ComputationResultImpl(boolean success, List<Integer> factors) {
        this.success = success;
        this.factors = factors;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Integer> getFactors() {
        return factors;
    }
}
