package api;
import java.util.ArrayList;
import java.util.List;

public class ComputationEngineImpl implements ComputationAPI {
    @Override
    // compute takes input number and returns the computation result
    public ComputationResult compute(String inputData, String[] delimiters) {
        try {
            int number = parseInput(inputData);
            List<Integer> factors = findFactors(number);
            return new ComputationResultImpl(true, factors);
        } catch (NumberFormatException e) {
            return new ComputationResultImpl(false, null);
        }
    }

    // trims extra white space in inputs
    private int parseInput(String input) {
        return Integer.parseInt(input.trim());
    }

    // method to find all factors of given number
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
